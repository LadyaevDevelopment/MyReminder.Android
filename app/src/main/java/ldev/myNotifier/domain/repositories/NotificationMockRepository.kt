package ldev.myNotifier.domain.repositories

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ldev.myNotifier.domain.DataResult
import ldev.myNotifier.domain.entities.Notification
import ldev.myNotifier.domain.entities.OneTimeNotification
import java.util.Date

class NotificationMockRepository : NotificationRepository {

    private val notifications = listOf(
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
        }
    }

    override fun getAllNotifications(): Flow<DataResult<List<Notification>>> {
        return flow {
            emit(
                DataResult(success = true, data = notifications)
            )
        }
    }

    override fun getNotificationInfo(): Flow<DataResult<Notification>> {
        return flow {
            emit(
                DataResult(success = true, data = notifications[3])
            )
        }
    }

}