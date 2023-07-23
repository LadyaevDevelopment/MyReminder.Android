package ldev.myNotifier.presentation.fragments.editOneTimeNotification

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class OneTimeNotificationModel(
    val id: Long,
    val title: String,
    val text: String,
    val time: Date
) : Parcelable