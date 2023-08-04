package ldev.myNotifier.core

import ldev.myNotifier.domain.entities.OneTimeNotification
import ldev.myNotifier.domain.entities.PeriodicNotificationWithRules
import ldev.myNotifier.domain.repositories.NotificationRepository
import javax.inject.Inject

class NotificationCoordinator @Inject constructor(
    private val alarmService: AlarmService,
    private val notificationRepository: NotificationRepository
) {
    suspend fun saveOneTimeNotification(notification: OneTimeNotification) {
        val result = notificationRepository.saveOneTimeNotification(notification)
        if (result.success) {
            alarmService.setupAlarmWithOneTimeNotification(result.data!!)
        }
    }

    suspend fun savePeriodicNotification(notificationWithRules: PeriodicNotificationWithRules) {
        val result = notificationRepository.savePeriodicNotification(notificationWithRules)
        if (result.success) {
            alarmService.setupAlarmWithPeriodicNotification(result.data!!)
        }
    }
}