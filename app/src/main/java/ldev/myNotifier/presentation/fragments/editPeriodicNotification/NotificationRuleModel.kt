package ldev.myNotifier.presentation.fragments.editPeriodicNotification

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.DayOfWeek

@Parcelize
data class NotificationRuleModel(
    val id: Long,
    val dayOfWeek: DayOfWeek,
    val time: TimeModel
) : Parcelable