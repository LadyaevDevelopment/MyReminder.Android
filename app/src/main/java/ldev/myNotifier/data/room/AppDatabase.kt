package ldev.myNotifier.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    version = 1,
    entities = [
        OneTimeNotification::class,
        PeriodicNotification::class,
        PeriodicNotificationRule::class,
        DbLog::class
    ],
)
@TypeConverters(DateConverter::class, DayOfWeekConverter::class, TimeConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getNotificationDao(): NotificationDao

    abstract fun getDbLogDao(): DbLogDao

    companion object {
        const val databaseName = "app_database.db"
    }

}