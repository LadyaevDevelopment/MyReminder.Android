package ldev.myNotifier.presentation.fragments.editPeriodicNotification

import ldev.myNotifier.domain.entities.Time
import java.time.DayOfWeek

data class DayOfWeekModel(
    val dayOfWeek: DayOfWeek,
    val isChecked: Boolean,
    val times: List<NotificationTimeModel>
)

data class NotificationTimeModel(
    val id: Long,
    val time: Time
)