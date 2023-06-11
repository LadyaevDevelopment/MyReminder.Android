package ldev.myNotifier.data.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import ldev.myNotifier.data.room.PeriodicNotificationRule.Companion.ID
import ldev.myNotifier.data.room.PeriodicNotificationRule.Companion.NOTIFICATION_ID
import ldev.myNotifier.data.room.PeriodicNotificationRule.Companion.TABLE_NAME
import java.time.DayOfWeek
import java.util.Date

@Entity(
    tableName = TABLE_NAME,
    indices = [
        Index(ID, NOTIFICATION_ID)
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
    val notificationId: Long,
    @ColumnInfo(name = DAY_OF_WEEK)
    val dayOfWeek: DayOfWeek,
    @ColumnInfo(name = TIME)
    @TypeConverters(DateConverter::class)
    val time: Date,
    @ColumnInfo(name = POSTPONED_TIME)
    @TypeConverters(DateConverter::class)
    val postponedTime: Date?
) {
    companion object {
        const val TABLE_NAME = "periodic_notification_rules"
        const val ID = "id"
        const val NOTIFICATION_ID = "notification_id"
        const val DAY_OF_WEEK = "day_of_week"
        const val TIME = "time"
        const val POSTPONED_TIME = "postponed_time"
    }
}