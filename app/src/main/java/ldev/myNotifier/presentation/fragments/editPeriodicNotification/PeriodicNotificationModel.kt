package ldev.myNotifier.presentation.fragments.editPeriodicNotification

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PeriodicNotificationModel(
    val id: Long,
    val title: String,
    val text: String,
    val rules: List<NotificationRuleModel>
) : Parcelable