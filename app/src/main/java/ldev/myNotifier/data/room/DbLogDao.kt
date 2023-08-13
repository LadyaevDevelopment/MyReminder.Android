package ldev.myNotifier.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy

@Dao
interface DbLogDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun addLog(log: DbLog)
}