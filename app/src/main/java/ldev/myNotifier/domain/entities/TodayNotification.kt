package ldev.myNotifier.domain.entities

import java.util.Date

data class TodayNotification(
    val id: Long,
    val title: String,
    val time: Date,
    val initialTime: Date,
    val status: TodayNotificationStatus
)

enum class TodayNotificationStatus {
    Completed,
    Postponed,
    Pending
}