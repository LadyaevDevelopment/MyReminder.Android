package ldev.myNotifier.framework

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import ldev.myNotifier.core.AlarmService
import ldev.myNotifier.data.room.toDate
import ldev.myNotifier.domain.entities.OneTimeNotification
import ldev.myNotifier.domain.entities.PeriodicNotificationWithRules
import java.util.Calendar
import javax.inject.Inject
import ldev.myNotifier.utils.findNextDateByDayOfWeek

class AlarmManagerService @Inject constructor(
    private val context: Context
) : AlarmService {
    override suspend fun setupAlarmWithOneTimeNotification(notification: OneTimeNotification) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        // check can setup exact time alarm
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !alarmManager.canScheduleExactAlarms()) {
            return
        }

        val intent = NotificationBroadcastReceiver.makeIntent(context, notification)
        // TODO: fix request code
        val pendingIntent = PendingIntent.getBroadcast(context, notification.id.toInt(), intent, PendingIntent.FLAG_IMMUTABLE)

        val calendar = Calendar.getInstance().apply {
            time = notification.date
        }

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            pendingIntent
        )
    }

    override suspend fun setupAlarmWithPeriodicNotification(notificationWithRules: PeriodicNotificationWithRules) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        // check can setup exact time alarm
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !alarmManager.canScheduleExactAlarms()) {
            return
        }

        for (notificationRule in notificationWithRules.rules) {
            val intent = NotificationBroadcastReceiver.makeIntent(
                context,
                notificationWithRules.notification,
                notificationRule
            )
            // TODO: fix request code
            val pendingIntent = PendingIntent.getBroadcast(context, -notificationRule.id.toInt(), intent, PendingIntent.FLAG_IMMUTABLE)

            val calendar = Calendar.getInstance().apply {
                time = findNextDateByDayOfWeek(notificationRule.dayOfWeek, notificationRule.time).toDate()
            }

            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )
        }
    }
}