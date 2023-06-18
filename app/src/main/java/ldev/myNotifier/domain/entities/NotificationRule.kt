package ldev.myNotifier.domain.entities

import java.time.DayOfWeek

data class NotificationRule(
    val id: Long,
    val time: Time,
    val dayOfWeek: DayOfWeek
)