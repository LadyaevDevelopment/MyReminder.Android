package ldev.myNotifier.domain.entities

import java.time.DayOfWeek

data class PeriodicNotificationRule(
    val id: Long,
    val dayOfWeek: DayOfWeek,
    val time: Time,
)