package ldev.myNotifier.domain.entities

import java.util.Date

data class TodayNotification(
    val id: Int,
    val title: String,
    val time: Time,
    val type: NotificationType,
    val status: TodayNotificationStatus
) {
    companion object
}

enum class TodayNotificationStatus {
    Completed,
    Postponed,
    Pending
}