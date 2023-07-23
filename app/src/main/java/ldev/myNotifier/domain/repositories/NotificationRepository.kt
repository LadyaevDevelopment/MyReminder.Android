package ldev.myNotifier.domain.repositories

import kotlinx.coroutines.flow.Flow
import ldev.myNotifier.domain.util.DataResult
import ldev.myNotifier.domain.entities.Notification
import ldev.myNotifier.domain.entities.OneTimeNotification
import ldev.myNotifier.domain.entities.PeriodicNotification
import ldev.myNotifier.domain.entities.TodayNotification
import ldev.myNotifier.domain.util.OperationResult

interface NotificationRepository {

    suspend fun getNotificationsForToday(): DataResult<List<TodayNotification>>

    suspend fun getAllNotifications(): DataResult<List<Notification>>

    suspend fun getNotificationInfo(): DataResult<Notification>

    suspend fun saveOneTimeNotification(notification: OneTimeNotification): OperationResult

    suspend fun savePeriodicNotification(notification: PeriodicNotification): OperationResult

}