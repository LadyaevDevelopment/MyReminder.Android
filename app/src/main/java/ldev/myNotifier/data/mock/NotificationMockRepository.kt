package ldev.myNotifier.data.mock

import ldev.myNotifier.domain.util.DataResult
import ldev.myNotifier.domain.entities.NotificationType
import ldev.myNotifier.domain.entities.OneTimeNotification
import ldev.myNotifier.domain.entities.PeriodicNotificationWithRules
import ldev.myNotifier.domain.entities.TodayNotification
import ldev.myNotifier.domain.entities.TodayNotificationStatus
import ldev.myNotifier.domain.repositories.NotificationRepository
import java.util.Date

class NotificationMockRepository : NotificationRepository {

//    private val notifications = listOf<Notification>(
//        OneTimeNotification(id = 1, title = "1", text = "It's notification", date = Date()),
//        OneTimeNotification(id = 2, title = "2", text = "It's notification", date = Date()),
//        OneTimeNotification(id = 3, title = "3", text = "It's notification", date = Date()),
//        OneTimeNotification(id = 4, title = "4", text = "It's notification", date = Date()),
//        OneTimeNotification(id = 5, title = "5", text = "It's notification", date = Date()),
//        OneTimeNotification(id = 6, title = "6", text = "It's notification", date = Date()),
//        OneTimeNotification(id = 7, title = "7", text = "It's notification", date = Date()),
//        OneTimeNotification(id = 8, title = "8", text = "It's notification", date = Date()),
//        OneTimeNotification(id = 9, title = "9", text = "It's notification", date = Date()),
//        OneTimeNotification(id = 10, title = "10", text = "It's notification", date = Date()),
//        OneTimeNotification(id = 11, title = "11", text = "It's notification", date = Date()),
//        OneTimeNotification(id = 12, title = "12", text = "It's notification", date = Date()),
//        OneTimeNotification(id = 13, title = "13", text = "It's notification", date = Date()),
//        OneTimeNotification(id = 14, title = "14", text = "It's notification", date = Date()),
//        OneTimeNotification(id = 15, title = "15", text = "It's notification", date = Date()),
//        OneTimeNotification(id = 16, title = "16", text = "It's notification", date = Date()),
//        OneTimeNotification(id = 17, title = "17", text = "It's notification", date = Date()),
//    )

    private val notificationsForToday = listOf(
        TodayNotification(id = 1, title = "Notification 1", date = Date(), status = TodayNotificationStatus.Postponed, type = NotificationType.OneTime),
        TodayNotification(id = 2, title = "Notification 2", date = Date(), status = TodayNotificationStatus.Postponed, type = NotificationType.OneTime),
        TodayNotification(id = 3, title = "Notification 3", date = Date(), status = TodayNotificationStatus.Postponed, type = NotificationType.OneTime),
        TodayNotification(id = 4, title = "Notification 4", date = Date(), status = TodayNotificationStatus.Postponed, type = NotificationType.OneTime),
        TodayNotification(id = 5, title = "Notification 5", date = Date(), status = TodayNotificationStatus.Postponed, type = NotificationType.OneTime),
        TodayNotification(id = 6, title = "Notification 6", date = Date(), status = TodayNotificationStatus.Postponed, type = NotificationType.OneTime),
        TodayNotification(id = 7, title = "Notification 7", date = Date(), status = TodayNotificationStatus.Pending, type = NotificationType.OneTime),
        TodayNotification(id = 8, title = "Notification 8", date = Date(), status = TodayNotificationStatus.Pending, type = NotificationType.OneTime),
        TodayNotification(id = 9, title = "Notification 9", date = Date(), status = TodayNotificationStatus.Pending, type = NotificationType.OneTime),
        TodayNotification(id = 10, title = "Notification 10", date = Date(), status = TodayNotificationStatus.Pending, type = NotificationType.OneTime),
        TodayNotification(id = 11, title = "Notification 11", date = Date(), status = TodayNotificationStatus.Pending, type = NotificationType.OneTime),
        TodayNotification(id = 12, title = "Notification 12", date = Date(), status = TodayNotificationStatus.Pending, type = NotificationType.OneTime),
        TodayNotification(id = 13, title = "Notification 13", date = Date(), status = TodayNotificationStatus.Pending, type = NotificationType.OneTime),
        TodayNotification(id = 14, title = "Notification 14", date = Date(), status = TodayNotificationStatus.Completed, type = NotificationType.OneTime),
        TodayNotification(id = 15, title = "Notification 15", date = Date(), status = TodayNotificationStatus.Completed, type = NotificationType.OneTime),
        TodayNotification(id = 16, title = "Notification 16", date = Date(), status = TodayNotificationStatus.Completed, type = NotificationType.OneTime),
        TodayNotification(id = 17, title = "Notification 17", date = Date(), status = TodayNotificationStatus.Completed, type = NotificationType.OneTime),
    )

    override suspend fun getNotificationsForToday(): DataResult<List<TodayNotification>> {
        return DataResult(success = true, data = notificationsForToday)
    }

//    override suspend fun getAllNotifications(): DataResult<List<Notification>> {
//        return DataResult(success = true, data = notifications)
//    }

    override suspend fun getOneTimeNotification(id: Int): DataResult<OneTimeNotification?> {
        TODO("Not yet implemented")
    }

    override suspend fun getPeriodicNotification(id: Int): DataResult<PeriodicNotificationWithRules?> {
        TODO("Not yet implemented")
    }

    override suspend fun saveOneTimeNotification(notification: OneTimeNotification): DataResult<OneTimeNotification> {
        return DataResult(success = true)
    }

    override suspend fun savePeriodicNotification(notificationWithRules: PeriodicNotificationWithRules): DataResult<PeriodicNotificationWithRules> {
        return DataResult(success = true)
    }

}