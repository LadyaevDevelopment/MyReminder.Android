package ldev.myNotifier.data.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import ldev.myNotifier.data.room.DbLog.Companion.TABLE_NAME

@Entity(
    tableName = TABLE_NAME,
    indices = [
        Index(DbLog.ID)
    ]
)
data class DbLog(
    @[PrimaryKey(true) ColumnInfo(name = ID)]
    val id: Long,
    @ColumnInfo(name = TAG)
    val tag: String,
    @ColumnInfo(name = TEXT)
    val text: String,
    @ColumnInfo(name = DATE_GMT)
    val dateGmt: String
) {
    companion object {
        const val TABLE_NAME = "db_logs"
        const val ID = "db_log_id"
        const val TAG = "tag"
        const val TEXT = "text"
        const val DATE_GMT = "date_gmt"
    }
}