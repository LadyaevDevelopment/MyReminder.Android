package ldev.myNotifier.domain.entities

import java.util.Date

data class OneTimeNotification(
    val id: Int,
    val title: String,
    val text: String,
    val date: Date,
    val isActive: Boolean
)

data class PeriodicNotification(
    val id: Int,
    val title: String,
    val text: String
)

data class PeriodicNotificationWithRules(
    val notification: PeriodicNotification,
    val rules: List<PeriodicNotificationRule>
)