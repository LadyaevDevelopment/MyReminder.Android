package ldev.myNotifier.domain.entities

import java.util.Date

sealed class Notification(
    open val id: Long,
    open val title: String,
    open val text: String,
    open val type: NotificationType
)

data class OneTimeNotification(
    override val id: Long,
    override val title: String,
    override val text: String,
    val time: Date
): Notification(id, title, text, NotificationType.OneTime)

data class PeriodicNotification(
    override val id: Long,
    override val title: String,
    override val text: String,
    val rules: List<NotificationRule>
): Notification(id, title, text, NotificationType.Periodic)