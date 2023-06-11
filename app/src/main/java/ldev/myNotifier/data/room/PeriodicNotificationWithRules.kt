package ldev.myNotifier.data.room

import androidx.room.Embedded
import androidx.room.Relation

data class PeriodicNotificationWithRules(
    @Embedded val notification: PeriodicNotification,
    @Relation(
        parentColumn = PeriodicNotification.ID,
        entityColumn = PeriodicNotificationRule.NOTIFICATION_ID
    )
    val rules: List<PeriodicNotificationRule>
)