package ldev.myNotifier.core

import ldev.myNotifier.domain.entities.OneTimeNotification
import ldev.myNotifier.domain.entities.PeriodicNotification
import ldev.myNotifier.domain.entities.PeriodicNotificationRule
import ldev.myNotifier.domain.entities.PeriodicNotificationWithRules

interface AlarmProxy {
    suspend fun setupAlarmWithOneTimeNotification(notification: OneTimeNotification)
    suspend fun setupAlarmWithPeriodicNotification(notificationWithRules: PeriodicNotificationWithRules)
    suspend fun reschedulePeriodicNotificationRule(notification: PeriodicNotification, notificationRule: PeriodicNotificationRule)
}