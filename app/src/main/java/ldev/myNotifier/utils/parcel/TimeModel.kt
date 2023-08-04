package ldev.myNotifier.utils.parcel

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import ldev.myNotifier.domain.entities.Time

@Parcelize
data class TimeModel(
    val hour: Int,
    val minute: Int
) : Parcelable

fun Time.toUIModel(): TimeModel {
    return TimeModel(
        hour = hour,
        minute = minute
    )
}

fun TimeModel.toDomainEntity(): Time {
    return Time(
        hour = hour,
        minute = minute
    )
}