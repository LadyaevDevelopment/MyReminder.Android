package ldev.myNotifier.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import java.time.DayOfWeek

@Dao
interface NotificationDao {
    @Query("SELECT * FROM one_time_notifications WHERE date BETWEEN :startTimeMillis AND :endTimeMillis - 1")
    suspend fun getOneTimeNotificationsInDateRange(startTimeMillis: Long, endTimeMillis: Long): List<OneTimeNotification>

    @Transaction
    @Query("SELECT * FROM periodic_notifications WHERE periodic_notification_id = :id")
    suspend fun getPeriodicNotification(id: Int): PeriodicNotificationWithRules?

    @Query("SELECT * FROM one_time_notifications WHERE one_time_notification_id = :id")
    suspend fun getOneTimeNotification(id: Int): OneTimeNotification?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addOrUpdateOneTimeNotification(notification: OneTimeNotification): Long

    @Transaction
    @Query("SELECT * FROM periodic_notifications")
    suspend fun getPeriodicNotifications(): List<PeriodicNotificationWithRules>

    @Transaction
    @Query("SELECT * FROM periodic_notification_rules WHERE day_of_week = :dayOfWeek")
    suspend fun getPeriodicNotificationRulesForDayOfWeek(dayOfWeek: DayOfWeek): List<RuleWithPeriodicNotification>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addOrUpdatePeriodicNotification(notification: PeriodicNotification): Long

    @Query("DELETE FROM periodic_notification_rules WHERE notification_id = :periodicNotificationId AND periodic_notification_rule_id NOT IN (:ruleIdsToKeep)")
    suspend fun deleteNotificationRulesExcept(periodicNotificationId: Int, ruleIdsToKeep: List<Int>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addOrUpdatePeriodicNotificationRules(notificationRules: List<PeriodicNotificationRule>)
}