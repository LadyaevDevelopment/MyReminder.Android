package ldev.myNotifier.data.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import ldev.myNotifier.data.room.OneTimeNotification.Companion.ID
import ldev.myNotifier.data.room.OneTimeNotification.Companion.TABLE_NAME
import java.util.Date

@Entity(
    tableName = TABLE_NAME,
    indices = [
        Index(ID)
    ]
)
data class OneTimeNotification(
    @[PrimaryKey(true) ColumnInfo(name = ID)]
    val id: Long,
    @ColumnInfo(name = TITLE)
    val title: String,
    @ColumnInfo(name = TEXT)
    val text: String,
    @ColumnInfo(name = DATE)
    val date: Date,
    @ColumnInfo(name = IS_ACTIVE)
    val isActive: Boolean,
) {
    companion object {
        const val TABLE_NAME = "one_time_notifications"
        const val ID = "one_time_notification_id"
        const val TITLE = "title"
        const val TEXT = "text"
        const val DATE = "date"
        const val IS_ACTIVE = "is_active"
    }
}