package ldev.myNotifier.data.room

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface NotificationDao {
    @Transaction
    @Query("SELECT * FROM one_time_notifications WHERE date(date) = date(:date)")
    fun getOneTimeNotificationsForDate(date: Date): Flow<List<OneTimeNotification>>

    @Query("SELECT * FROM periodic_notifications " +
            "INNER JOIN periodic_notification_rules ON periodic_notifications.id = periodic_notification_rules.notification_id " +
            "WHERE date(periodic_notification_rules.postponed_time) = date(:date)")
    fun getPeriodicNotifications(date: Date): Flow<List<PeriodicNotificationWithRules>>
}