package ldev.myNotifier.data.mock

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import ldev.myNotifier.domain.util.DataResult
import ldev.myNotifier.domain.entities.Notification
import ldev.myNotifier.domain.entities.OneTimeNotification
import ldev.myNotifier.domain.entities.PeriodicNotification
import ldev.myNotifier.domain.entities.TodayNotification
import ldev.myNotifier.domain.entities.TodayNotificationStatus
import ldev.myNotifier.domain.repositories.NotificationRepository
import ldev.myNotifier.domain.util.OperationResult
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
        TodayNotification(id = 1, title = "Notification 1", time = Date(), initialTime = Date(), status = TodayNotificationStatus.Postponed),
        TodayNotification(id = 2, title = "Notification 2", time = Date(), initialTime = Date(), status = TodayNotificationStatus.Postponed),
        TodayNotification(id = 3, title = "Notification 3", time = Date(), initialTime = Date(), status = TodayNotificationStatus.Postponed),
        TodayNotification(id = 4, title = "Notification 4", time = Date(), initialTime = Date(), status = TodayNotificationStatus.Postponed),
        TodayNotification(id = 5, title = "Notification 5", time = Date(), initialTime = Date(), status = TodayNotificationStatus.Postponed),
        TodayNotification(id = 6, title = "Notification 6", time = Date(), initialTime = Date(), status = TodayNotificationStatus.Postponed),
        TodayNotification(id = 7, title = "Notification 7", time = Date(), initialTime = Date(), status = TodayNotificationStatus.Pending),
        TodayNotification(id = 8, title = "Notification 8", time = Date(), initialTime = Date(), status = TodayNotificationStatus.Pending),
        TodayNotification(id = 9, title = "Notification 9", time = Date(), initialTime = Date(), status = TodayNotificationStatus.Pending),
        TodayNotification(id = 10, title = "Notification 10", time = Date(), initialTime = Date(), status = TodayNotificationStatus.Pending),
        TodayNotification(id = 11, title = "Notification 11", time = Date(), initialTime = Date(), status = TodayNotificationStatus.Pending),
        TodayNotification(id = 12, title = "Notification 12", time = Date(), initialTime = Date(), status = TodayNotificationStatus.Pending),
        TodayNotification(id = 13, title = "Notification 13", time = Date(), initialTime = Date(), status = TodayNotificationStatus.Pending),
        TodayNotification(id = 14, title = "Notification 14", time = Date(), initialTime = Date(), status = TodayNotificationStatus.Completed),
        TodayNotification(id = 15, title = "Notification 15", time = Date(), initialTime = Date(), status = TodayNotificationStatus.Completed),
        TodayNotification(id = 16, title = "Notification 16", time = Date(), initialTime = Date(), status = TodayNotificationStatus.Completed),
        TodayNotification(id = 17, title = "Notification 17", time = Date(), initialTime = Date(), status = TodayNotificationStatus.Completed),
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

    override fun saveOneTimeNotification(notification: OneTimeNotification): Flow<OperationResult> {
        return flow {
            emit(OperationResult(success = true))
        }.flowOn(Dispatchers.IO)
    }

    override fun savePeriodicNotification(notification: PeriodicNotification): Flow<OperationResult> {
        return flow {
            emit(OperationResult(success = true))
        }.flowOn(Dispatchers.IO)
    }

}