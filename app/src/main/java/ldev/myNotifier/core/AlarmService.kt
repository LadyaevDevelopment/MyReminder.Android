package ldev.myNotifier.core

import ldev.myNotifier.domain.entities.OneTimeNotification
import ldev.myNotifier.domain.entities.PeriodicNotificationWithRules

interface AlarmService {
    suspend fun setupAlarmWithOneTimeNotification(notification: OneTimeNotification)
    suspend fun setupAlarmWithPeriodicNotification(notificationWithRules: PeriodicNotificationWithRules)
}