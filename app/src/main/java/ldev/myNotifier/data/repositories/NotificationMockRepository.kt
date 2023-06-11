package ldev.myNotifier.data.repositories

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import ldev.myNotifier.domain.util.DataResult
import ldev.myNotifier.domain.entities.Notification
import ldev.myNotifier.domain.entities.OneTimeNotification
import ldev.myNotifier.domain.entities.TodayNotification
import ldev.myNotifier.domain.entities.TodayNotificationStatus
import ldev.myNotifier.domain.repositories.NotificationRepository
import java.util.Date

class NotificationMockRepository : NotificationRepository {

    private val notifications = listOf<Notification>(
        OneTimeNotification(id = 1, title = "1", text = "It's notification", time = Date()),
        OneTimeNotification(id = 2, title = "2", text = "It's notification", time = Date()),
        OneTimeNotification(id = 3, title = "3", text = "It's notification", time = Date()),
        OneTimeNotification(id = 4, title = "4", text = "It's notification", time = Date()),
        OneTimeNotification(id = 5, title = "5", text = "It's notification", time = Date()),
        OneTimeNotification(id = 6, title = "6", text = "It's notification", time = Date()),
        OneTimeNotification(id = 7, title = "7", text = "It's notification", time = Date()),
        OneTimeNotification(id = 8, title = "8", text = "It's notification", time = Date()),
        OneTimeNotification(id = 9, title = "9", text = "It's notification", time = Date()),
        OneTimeNotification(id = 10, title = "10", text = "It's notification", time = Date()),
        OneTimeNotification(id = 11, title = "11", text = "It's notification", time = Date()),
        OneTimeNotification(id = 12, title = "12", text = "It's notification", time = Date()),
        OneTimeNotification(id = 13, title = "13", text = "It's notification", time = Date()),
        OneTimeNotification(id = 14, title = "14", text = "It's notification", time = Date()),
        OneTimeNotification(id = 15, title = "15", text = "It's notification", time = Date()),
        OneTimeNotification(id = 16, title = "16", text = "It's notification", time = Date()),
        OneTimeNotification(id = 17, title = "17", text = "It's notification", time = Date()),
    )

    private val notificationsForToday = listOf(
        TodayNotification(id = 1, title = "1", time = Date(), status = TodayNotificationStatus.Postponed),
        TodayNotification(id = 2, title = "2", time = Date(), status = TodayNotificationStatus.Postponed),
        TodayNotification(id = 3, title = "3", time = Date(), status = TodayNotificationStatus.Postponed),
        TodayNotification(id = 4, title = "4", time = Date(), status = TodayNotificationStatus.Postponed),
        TodayNotification(id = 5, title = "5", time = Date(), status = TodayNotificationStatus.Postponed),
        TodayNotification(id = 6, title = "6", time = Date(), status = TodayNotificationStatus.Postponed),
        TodayNotification(id = 7, title = "7", time = Date(), status = TodayNotificationStatus.Pending),
        TodayNotification(id = 8, title = "8", time = Date(), status = TodayNotificationStatus.Pending),
        TodayNotification(id = 9, title = "9", time = Date(), status = TodayNotificationStatus.Pending),
        TodayNotification(id = 10, title = "10", time = Date(), status = TodayNotificationStatus.Pending),
        TodayNotification(id = 11, title = "11", time = Date(), status = TodayNotificationStatus.Pending),
        TodayNotification(id = 12, title = "12", time = Date(), status = TodayNotificationStatus.Pending),
        TodayNotification(id = 13, title = "13", time = Date(), status = TodayNotificationStatus.Pending),
        TodayNotification(id = 14, title = "14", time = Date(), status = TodayNotificationStatus.Completed),
        TodayNotification(id = 15, title = "15", time = Date(), status = TodayNotificationStatus.Completed),
        TodayNotification(id = 16, title = "16", time = Date(), status = TodayNotificationStatus.Completed),
        TodayNotification(id = 17, title = "17", time = Date(), status = TodayNotificationStatus.Completed),
    )

    override fun getNotificationsForToday(): Flow<DataResult<List<TodayNotification>>> {
        return flow {
            emit(
                DataResult(success = true, data = notificationsForToday)
            )
        }.flowOn(Dispatchers.IO)
    }

    override fun getAllNotifications(): Flow<DataResult<List<Notification>>> {
        return flow {
            emit(
                DataResult(success = true, data = notifications)
            )
        }.flowOn(Dispatchers.IO)
    }

    override fun getNotificationInfo(): Flow<DataResult<Notification>> {
        return flow {
            emit(
                DataResult(success = true, data = notifications[3])
            )
        }.flowOn(Dispatchers.IO)
    }

}