package ldev.myNotifier.data.room

import ldev.myNotifier.domain.entities.OneTimeNotification
import ldev.myNotifier.domain.entities.PeriodicNotificationWithRules
import ldev.myNotifier.domain.entities.TodayNotification
import ldev.myNotifier.domain.repositories.NotificationRepository
import ldev.myNotifier.domain.util.DataResult
import java.time.LocalDateTime
import javax.inject.Inject

class NotificationLocalRepository @Inject constructor(
    private val notificationDao: NotificationDao
) : NotificationRepository {

    override suspend fun getNotificationsForToday(): DataResult<List<TodayNotification>> {
        val localDateTime = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0)
        val startTimeMillis = localDateTime.toDate().time
        val endTimeMillis = localDateTime.plusDays(1).toDate().time

        val oneTimeNotifications = notificationDao.getOneTimeNotificationsInDateRange(startTimeMillis, endTimeMillis)
            .map { TodayNotification.oneTimeNotification(it.toDomainEntity()) }

        val periodicNotifications = notificationDao.getPeriodicNotificationRulesForDayOfWeek(localDateTime.dayOfWeek)
            .map { TodayNotification.periodicNotification(it.notification.toDomainEntity(), it.rule.toDomainEntity()) }

        val todayNotifications = (oneTimeNotifications + periodicNotifications).sortedBy { it.date }

        return DataResult(success = true, data = todayNotifications)
    }

    override suspend fun getOneTimeNotification(id: Int): DataResult<OneTimeNotification?> {
        return DataResult(
            success = true,
            data = notificationDao.getOneTimeNotification(id)?.toDomainEntity()
        )
    }

    override suspend fun getPeriodicNotification(id: Int): DataResult<PeriodicNotificationWithRules?> {
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
            ).toInt()

            DataResult(success = true, data = notification.copy(id = notificationId))
        } catch (ex: Throwable) {
            DataResult(success = false, errorMessage = ex.message)
        }
    }

    override suspend fun savePeriodicNotification(notificationWithRules: PeriodicNotificationWithRules): DataResult<PeriodicNotificationWithRules> {
        return try {
            if (notificationWithRules.notification.id != 0) {
                notificationDao.deleteNotificationRulesExcept(
                    periodicNotificationId = notificationWithRules.notification.id,
                    ruleIdsToKeep = notificationWithRules.rules.map { it.id }.distinct().toList()
                )
            }
            val notificationId = notificationDao.addOrUpdatePeriodicNotification(
                notificationWithRules.notification.toRoomEntity()
            ).toInt()

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