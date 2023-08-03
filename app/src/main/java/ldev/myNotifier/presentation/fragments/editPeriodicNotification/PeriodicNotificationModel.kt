package ldev.myNotifier.presentation.fragments.editPeriodicNotification

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import ldev.myNotifier.domain.entities.PeriodicNotification
import ldev.myNotifier.domain.entities.PeriodicNotificationWithRules

@Parcelize
data class PeriodicNotificationModel(
    val id: Long,
    val title: String,
    val text: String,
    val rules: List<NotificationRuleModel>
) : Parcelable

fun PeriodicNotificationWithRules.toUiModel(): PeriodicNotificationModel {
    return PeriodicNotificationModel(
        id = notification.id,
        title = notification.title,
        text = notification.text,
        rules = rules.map { it.toUiModel() }
    )
}

fun PeriodicNotificationModel.toDomainEntity(): PeriodicNotificationWithRules {
    return PeriodicNotificationWithRules(
        notification = PeriodicNotification(
            id = id,
            title = title,
            text = text
        ),
        rules = rules.map { it.toDomainEntity() }
    )
}