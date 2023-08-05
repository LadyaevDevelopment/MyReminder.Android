package ldev.myNotifier.data.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import ldev.myNotifier.data.room.PeriodicNotificationRule.Companion.ID
import ldev.myNotifier.data.room.PeriodicNotificationRule.Companion.NOTIFICATION_ID
import ldev.myNotifier.data.room.PeriodicNotificationRule.Companion.TABLE_NAME
import ldev.myNotifier.domain.entities.Time
import java.time.DayOfWeek

@Entity(
    tableName = TABLE_NAME,
    indices = [
        Index(ID),
        Index(NOTIFICATION_ID)
    ],
    foreignKeys = [
        ForeignKey(
            entity = PeriodicNotification::class,
            parentColumns = [PeriodicNotification.ID],
            childColumns = [NOTIFICATION_ID],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class PeriodicNotificationRule(
    @[PrimaryKey(true) ColumnInfo(name = ID)]
    val id: Long,
    @ColumnInfo(name = NOTIFICATION_ID)
    val notificationId: Int,
    @ColumnInfo(name = DAY_OF_WEEK)
    val dayOfWeek: DayOfWeek,
    @ColumnInfo(name = TIME)
    val time: Time,
) {
    companion object {
        const val TABLE_NAME = "periodic_notification_rules"
        const val ID = "periodic_notification_rule_id"
        const val NOTIFICATION_ID = "notification_id"
        const val DAY_OF_WEEK = "day_of_week"
        const val TIME = "time"
    }
}