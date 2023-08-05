package ldev.myNotifier.presentation.fragments.editPeriodicNotification

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import ldev.myNotifier.domain.entities.PeriodicNotificationRule
import ldev.myNotifier.utils.parcel.TimeModel
import ldev.myNotifier.utils.parcel.toDomainEntity
import ldev.myNotifier.utils.parcel.toUIModel
import java.time.DayOfWeek

@Parcelize
data class NotificationRuleModel(
    val id: Int,
    val dayOfWeek: DayOfWeek,
    val time: TimeModel
) : Parcelable

fun PeriodicNotificationRule.toUiModel(): NotificationRuleModel {
    return NotificationRuleModel(
        id = id,
        dayOfWeek = dayOfWeek,
        time = time.toUIModel()
    )
}

fun NotificationRuleModel.toDomainEntity(): PeriodicNotificationRule {
    return PeriodicNotificationRule(
        id = id,
        dayOfWeek = dayOfWeek,
        time = time.toDomainEntity()
    )
}