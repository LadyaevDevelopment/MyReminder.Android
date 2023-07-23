package ldev.myNotifier.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.Transaction
import androidx.sqlite.db.SupportSQLiteQuery
import java.util.Date

@Dao
interface NotificationDao {
    @Transaction
    @Query("SELECT * FROM one_time_notifications WHERE date(date) = date(:date)")
    suspend fun getOneTimeNotificationsForDate(date: Date): List<OneTimeNotification>

    @Query("SELECT * FROM periodic_notifications WHERE periodic_notification_id = :id")
    suspend fun getPeriodicNotification(id: Long): PeriodicNotificationWithRules?

    @Query("SELECT * FROM one_time_notifications WHERE one_time_notification_id = :id")
    suspend fun getOneTimeNotification(id: Long): OneTimeNotification?

    @Query("SELECT * FROM periodic_notifications")
    suspend fun getPeriodicNotifications(): List<PeriodicNotificationWithRules>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addOrUpdatePeriodicNotification(notification: PeriodicNotification): Long

    @Query("DELETE FROM periodic_notification_rules WHERE notification_id = :periodicNotificationId AND periodic_notification_rule_id NOT IN (:ruleIdsToKeep)")
    suspend fun deleteNotificationRulesExcept(periodicNotificationId: Long, ruleIdsToKeep: List<Long>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addOrUpdatePeriodicNotificationRules(notificationRules: List<PeriodicNotificationRule>)

    @RawQuery
    fun getLastInsertedId(query: SupportSQLiteQuery): Long
}