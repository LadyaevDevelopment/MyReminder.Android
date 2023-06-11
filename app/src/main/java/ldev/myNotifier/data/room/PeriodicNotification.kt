package ldev.myNotifier.data.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import ldev.myNotifier.data.room.PeriodicNotification.Companion.ID
import ldev.myNotifier.data.room.PeriodicNotification.Companion.TABLE_NAME

@Entity(
    tableName = TABLE_NAME,
    indices = [
        Index(ID)
    ]
)
data class PeriodicNotification(
    @[PrimaryKey(true) ColumnInfo(name = ID)]
    val id: Long,
    @ColumnInfo(name = TITLE)
    val title: String,
    @ColumnInfo(name = TEXT)
    val text: String,
) {
    companion object {
        const val TABLE_NAME = "periodic_notifications"
        const val ID = "id"
        const val TITLE = "title"
        const val TEXT = "text"
    }
}