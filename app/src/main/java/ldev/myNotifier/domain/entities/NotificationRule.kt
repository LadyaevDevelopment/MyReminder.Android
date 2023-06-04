package ldev.myNotifier.domain.entities

import java.sql.Time
import java.time.DayOfWeek

data class NotificationRule(
    val time: Time,
    val dayOfWeek: DayOfWeek?
)