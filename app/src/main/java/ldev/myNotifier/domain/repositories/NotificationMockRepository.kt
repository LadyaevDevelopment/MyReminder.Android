package ldev.myNotifier.domain.repositories

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import ldev.myNotifier.domain.util.DataResult
import ldev.myNotifier.domain.entities.Notification
import ldev.myNotifier.domain.entities.OneTimeNotification
import java.util.Date

class NotificationMockRepository : NotificationRepository {

    private val notifications = listOf<Notification>(
        OneTimeNotification(id = 1, title = "1", text = "It's notification", time = Date()),
        OneTimeNotification(id = 2, title = "2", text = "It's notification", time = Date()),
        OneTimeNotification(id = 3, title = "3", text = "It's notification", time = Date()),
        OneTimeNotification(id = 4, title = "4", text = "It's notification", time = Date()),
        OneTimeNotification(id = 5, title = "5", text = "It's notification", time = Date()),
        OneTimeNotification(id = 6, title = "6", text = "It's notification", time = Date()),
    )

    override fun getNotificationsForToday(): Flow<DataResult<List<Notification>>> {
        return flow {
            emit(
                DataResult(success = true, data = notifications)
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