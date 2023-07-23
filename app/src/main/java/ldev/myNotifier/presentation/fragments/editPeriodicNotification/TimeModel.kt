package ldev.myNotifier.presentation.fragments.editPeriodicNotification

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TimeModel(
    val hour: Int,
    val minute: Int
) : Parcelable