package ldev.myNotifier.data.room

import ldev.myNotifier.domain.entities.Notification
import ldev.myNotifier.domain.entities.OneTimeNotification
import ldev.myNotifier.domain.entities.PeriodicNotification
import ldev.myNotifier.domain.entities.TodayNotification
import ldev.myNotifier.domain.entities.TodayNotificationStatus
import ldev.myNotifier.domain.repositories.NotificationRepository
import ldev.myNotifier.domain.util.DataResult
import ldev.myNotifier.domain.util.OperationResult
import java.util.Date
import javax.inject.Inject

class NotificationLocalRepository @Inject constructor(
    private val notificationDao: NotificationDao
) : NotificationRepository {

    override suspend fun getNotificationsForToday(): DataResult<List<TodayNotification>> {
        val notifications = notificationDao.getPeriodicNotifications()
        return DataResult(success = true, data = notifications.map {
            TodayNotification(
                id = it.notification.id,
                title = it.notification.title,
                time = Date(),
                initialTime = Date(),
                status = TodayNotificationStatus.Pending
            )
        })
    }

    override suspend fun getAllNotifications(): DataResult<List<Notification>> {
        val notifications = notificationDao.getPeriodicNotifications()
        return DataResult(success = true, data = notifications.map {
            PeriodicNotification(
                id = it.notification.id,
                title = it.notification.title,
                text = it.notification.text,
                rules = listOf()
            )
        })
    }

    override suspend fun getNotificationInfo(): DataResult<Notification> {
        TODO("Not yet implemented")
    }

    override suspend fun saveOneTimeNotification(notification: OneTimeNotification): OperationResult {
        TODO("Not yet implemented")
    }

    override suspend fun savePeriodicNotification(notification: PeriodicNotification): OperationResult {
        if (notification.id != 0L) {
            notificationDao.deleteNotificationRulesExcept(
                periodicNotificationId = notification.id,
                ruleIdsToKeep = notification.rules.map { it.id }.distinct().toList()
            )
        }
        val notificationId = notificationDao.addOrUpdatePeriodicNotification(PeriodicNotification(
            id = notification.id,
            title = notification.title,
            text = notification.text
        ))
        notificationDao.addOrUpdatePeriodicNotificationRules(
            notification.rules.map {
                PeriodicNotificationRule(
                    id = it.id,
                    notificationId = notificationId,
                    dayOfWeek = it.dayOfWeek,
                    time = it.time,
                    postponedTime = null
                )
            }
        )
        return OperationResult(success = true)
    }
}