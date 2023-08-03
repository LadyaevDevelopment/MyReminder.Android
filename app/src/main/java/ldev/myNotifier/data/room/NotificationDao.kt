package ldev.myNotifier.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import java.time.DayOfWeek
import java.util.Date

@Dao
interface NotificationDao {
    @Transaction
    @Query("SELECT * FROM one_time_notifications WHERE DATE(DATETIME(ROUND(date / 1000), 'unixepoch')) = DATE(DATETIME(ROUND(:date / 1000), 'unixepoch'))")
    suspend fun getOneTimeNotificationsForDate(date: Date): List<OneTimeNotification>

    @Transaction
    @Query("SELECT * FROM periodic_notifications WHERE periodic_notification_id = :id")
    suspend fun getPeriodicNotification(id: Long): PeriodicNotificationWithRules?

    @Query("SELECT * FROM one_time_notifications WHERE one_time_notification_id = :id")
    suspend fun getOneTimeNotification(id: Long): OneTimeNotification?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addOrUpdateOneTimeNotification(notification: OneTimeNotification): Long

    @Query("SELECT * FROM periodic_notifications")
    suspend fun getPeriodicNotifications(): List<PeriodicNotificationWithRules>

    @Query("SELECT * FROM periodic_notification_rules WHERE day_of_week = :dayOfWeek")
    suspend fun getPeriodicNotificationRulesForDayOfWeek(dayOfWeek: DayOfWeek): List<RuleWithPeriodicNotification>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addOrUpdatePeriodicNotification(notification: PeriodicNotification): Long

    @Query("DELETE FROM periodic_notification_rules WHERE notification_id = :periodicNotificationId AND periodic_notification_rule_id NOT IN (:ruleIdsToKeep)")
    suspend fun deleteNotificationRulesExcept(periodicNotificationId: Long, ruleIdsToKeep: List<Long>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addOrUpdatePeriodicNotificationRules(notificationRules: List<PeriodicNotificationRule>)
}