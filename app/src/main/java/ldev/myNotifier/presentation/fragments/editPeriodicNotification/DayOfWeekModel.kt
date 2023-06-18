package ldev.myNotifier.presentation.fragments.editPeriodicNotification

import java.time.DayOfWeek

data class DayOfWeekModel(
    val dayOfWeek: DayOfWeek,
    val isChecked: Boolean,
    val times: List<NotificationTime>
)

data class NotificationTime(
    val id: Int,
    val hour: Int,
    val minute: Int
)