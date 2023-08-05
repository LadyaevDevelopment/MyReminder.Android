package ldev.myNotifier.presentation.fragments.editOneTimeNotification

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import ldev.myNotifier.domain.entities.OneTimeNotification
import java.util.Date

@Parcelize
data class OneTimeNotificationModel(
    val id: Int,
    val title: String,
    val text: String,
    val date: Date
) : Parcelable

fun OneTimeNotification.toUiModel() : OneTimeNotificationModel {
    return OneTimeNotificationModel(
        id = id,
        title = title,
        text = text,
        date = date
    )
}

fun OneTimeNotificationModel.toDomainEntity(): OneTimeNotification {
    return OneTimeNotification(
        id = id,
        title = title,
        text = text,
        date = date
    )
}