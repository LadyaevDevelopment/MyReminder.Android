package ldev.myNotifier.data.room

import androidx.room.Embedded
import androidx.room.Relation

data class RuleWithPeriodicNotification(
    @Embedded val rule: PeriodicNotificationRule,
    @Relation(
        parentColumn = PeriodicNotificationRule.NOTIFICATION_ID,
        entityColumn = PeriodicNotification.ID
    )
    val notification: PeriodicNotification
)