package ldev.myNotifier.domain.repositories

import ldev.myNotifier.domain.util.DataResult
import ldev.myNotifier.domain.entities.OneTimeNotification
import ldev.myNotifier.domain.entities.PeriodicNotificationWithRules
import ldev.myNotifier.domain.entities.TodayNotification
import ldev.myNotifier.domain.util.OperationResult

interface NotificationRepository {

    suspend fun getNotificationsForToday(): DataResult<List<TodayNotification>>

    suspend fun getOneTimeNotification(id: Int): DataResult<OneTimeNotification?>

    suspend fun getPeriodicNotification(id: Int): DataResult<PeriodicNotificationWithRules?>

    suspend fun saveOneTimeNotification(notification: OneTimeNotification): DataResult<OneTimeNotification>

    suspend fun savePeriodicNotification(notificationWithRules: PeriodicNotificationWithRules): DataResult<PeriodicNotificationWithRules>

}