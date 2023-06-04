package ldev.myNotifier.domain.repositories

import kotlinx.coroutines.flow.Flow
import ldev.myNotifier.domain.entities.Notification

interface NotificationRepository {

    fun getNotificationsForToday(): Flow<List<Notification>>

    fun getAllNotifications(): Flow<List<Notification>>

    fun getNotificationInfo(): Flow<Notification?>

}