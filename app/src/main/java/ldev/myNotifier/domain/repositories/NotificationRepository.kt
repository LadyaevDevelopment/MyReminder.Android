package ldev.myNotifier.domain.repositories

import ldev.myNotifier.domain.util.DataResult
import ldev.myNotifier.domain.entities.OneTimeNotification
import ldev.myNotifier.domain.entities.PeriodicNotificationWithRules
import ldev.myNotifier.domain.entities.TodayNotification
import ldev.myNotifier.domain.util.OperationResult

interface NotificationRepository {

    suspend fun getNotificationsForToday(): DataResult<List<TodayNotification>>

//    suspend fun getAllNotifications(): DataResult<List<Notification>>

    suspend fun getOneTimeNotification(id: Long): DataResult<OneTimeNotification?>

    suspend fun getPeriodicNotification(id: Long): DataResult<PeriodicNotificationWithRules?>

    suspend fun saveOneTimeNotification(notification: OneTimeNotification): OperationResult

    suspend fun savePeriodicNotification(notificationWithRules: PeriodicNotificationWithRules): OperationResult

}