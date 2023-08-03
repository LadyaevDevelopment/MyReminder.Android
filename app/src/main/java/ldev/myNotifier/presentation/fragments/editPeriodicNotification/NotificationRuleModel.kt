package ldev.myNotifier.presentation.fragments.editPeriodicNotification

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import ldev.myNotifier.domain.entities.PeriodicNotificationRule
import java.time.DayOfWeek

@Parcelize
data class NotificationRuleModel(
    val id: Long,
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