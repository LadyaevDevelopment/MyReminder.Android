package ldev.myNotifier.data.room

import ldev.myNotifier.domain.entities.OneTimeNotification
import ldev.myNotifier.domain.entities.PeriodicNotificationWithRules
import ldev.myNotifier.domain.entities.TodayNotification
import ldev.myNotifier.domain.repositories.NotificationRepository
import ldev.myNotifier.domain.util.DataResult
import java.time.Instant
import javax.inject.Inject

class NotificationLocalRepository @Inject constructor(
    private val notificationDao: NotificationDao
) : NotificationRepository {

    override suspend fun getNotificationsForToday(): DataResult<List<TodayNotification>> {
        val instant = Instant.now()

        val oneTimeNotifications = notificationDao.getOneTimeNotificationsForDate(instant.toDate())
            .map { TodayNotification.oneTimeNotification(it.toDomainEntity()) }

        val periodicNotifications = notificationDao.getPeriodicNotificationRulesForDayOfWeek(instant.toLocalDate().dayOfWeek)
            .map { TodayNotification.periodicNotification(it.notification.toDomainEntity(), it.rule.toDomainEntity()) }

        val todayNotifications = (oneTimeNotifications + periodicNotifications).sortedBy { it.date }

        return DataResult(success = true, data = todayNotifications)
    }

    override suspend fun getOneTimeNotification(id: Long): DataResult<OneTimeNotification?> {
        return DataResult(
            success = true,
            data = notificationDao.getOneTimeNotification(id)?.toDomainEntity()
        )
    }

    override suspend fun getPeriodicNotification(id: Long): DataResult<PeriodicNotificationWithRules?> {
        return DataResult(
            success = true,
            data = notificationDao.getPeriodicNotification(id)?.let {
                PeriodicNotificationWithRules(
                    notification = it.notification.toDomainEntity(),
                    rules = it.rules.map { rule -> rule.toDomainEntity() }
                )
            }
        )
    }

    override suspend fun saveOneTimeNotification(notification: OneTimeNotification): DataResult<OneTimeNotification> {
        return try {
            val notificationId = notificationDao.addOrUpdateOneTimeNotification(
                notification.toRoomEntity()
            )
            DataResult(success = true, data = notification.copy(id = notificationId))
        } catch (ex: Throwable) {
            DataResult(success = false, errorMessage = ex.message)
        }
    }

    override suspend fun savePeriodicNotification(notificationWithRules: PeriodicNotificationWithRules): DataResult<PeriodicNotificationWithRules> {
        return try {
            if (notificationWithRules.notification.id != 0L) {
                notificationDao.deleteNotificationRulesExcept(
                    periodicNotificationId = notificationWithRules.notification.id,
                    ruleIdsToKeep = notificationWithRules.rules.map { it.id }.distinct().toList()
                )
            }
            val notificationId = notificationDao.addOrUpdatePeriodicNotification(notificationWithRules.notification.toRoomEntity())
            notificationDao.addOrUpdatePeriodicNotificationRules(
                notificationWithRules.rules.map { it.toRoomEntity(notificationId) }
            )

            val updatedNotificationWithRules = notificationDao.getPeriodicNotification(notificationId)!!

            DataResult(
                success = true,
                data = PeriodicNotificationWithRules(
                    notification = updatedNotificationWithRules.notification.toDomainEntity(),
                    rules = updatedNotificationWithRules.rules.map { it.toDomainEntity() }
                )
            )
        } catch (ex: Throwable) {
            DataResult(success = false, errorMessage = ex.message)
        }
    }
}