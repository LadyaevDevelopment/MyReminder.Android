package ldev.myNotifier.framework

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import ldev.myNotifier.domain.entities.OneTimeNotification
import ldev.myNotifier.domain.entities.PeriodicNotification
import ldev.myNotifier.domain.entities.PeriodicNotificationRule
import ldev.myNotifier.utils.parcel.TimeModel
import ldev.myNotifier.utils.parcel.toUIModel
import java.time.DayOfWeek
import java.util.Date

@Parcelize
sealed class NotificationModel(
    open val id: Long,
    open val title: String,
    open val text: String
) : Parcelable {
    class OneTime(
        override val id: Long,
        override val title: String,
        override val text: String,
        val date: Date
    ) : NotificationModel(id, title, text)

    class Periodic(
        override val id: Long,
        override val title: String,
        override val text: String,
        val time: TimeModel,
        val dayOfWeek: DayOfWeek
    ) : NotificationModel(id, title, text)

    companion object
}

fun NotificationModel.Companion.fromDomainEntity(
    notification: OneTimeNotification
): NotificationModel.OneTime {
    return NotificationModel.OneTime(
        id = notification.id,
        title = notification.title,
        text = notification.text,
        date = notification.date
    )
}

fun NotificationModel.Companion.fromDomainEntity(
    notification: PeriodicNotification,
    rule: PeriodicNotificationRule
): NotificationModel.Periodic {
    return NotificationModel.Periodic(
        id = notification.id,
        title = notification.title,
        text = notification.text,
        dayOfWeek = rule.dayOfWeek,
        time = rule.time.toUIModel()
    )
}