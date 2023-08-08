package ldev.myNotifier.core

import ldev.myNotifier.domain.entities.OneTimeNotification
import ldev.myNotifier.domain.entities.PeriodicNotification
import ldev.myNotifier.domain.entities.PeriodicNotificationRule
import ldev.myNotifier.domain.entities.PeriodicNotificationWithRules

interface AlarmService {
    fun setupAlarmWithOneTimeNotification(notification: OneTimeNotification)
    fun setupAlarmWithPeriodicNotification(notificationWithRules: PeriodicNotificationWithRules)
    fun reschedulePeriodicNotificationRule(notification: PeriodicNotification, notificationRule: PeriodicNotificationRule)
}