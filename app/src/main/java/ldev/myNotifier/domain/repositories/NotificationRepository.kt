package ldev.myNotifier.domain.repositories

import kotlinx.coroutines.flow.Flow
import ldev.myNotifier.domain.util.DataResult
import ldev.myNotifier.domain.entities.Notification
import ldev.myNotifier.domain.entities.TodayNotification

interface NotificationRepository {

    fun getNotificationsForToday(): Flow<DataResult<List<TodayNotification>>>

    fun getAllNotifications(): Flow<DataResult<List<Notification>>>

    fun getNotificationInfo(): Flow<DataResult<Notification>>

}