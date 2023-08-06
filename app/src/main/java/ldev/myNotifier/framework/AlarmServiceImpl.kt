package ldev.myNotifier.framework

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import android.util.Log
import ldev.myNotifier.core.AlarmService
import ldev.myNotifier.domain.entities.OneTimeNotification
import ldev.myNotifier.domain.entities.PeriodicNotificationRule
import ldev.myNotifier.domain.entities.PeriodicNotificationWithRules
import java.util.Calendar
import javax.inject.Inject
import ldev.myNotifier.utils.findNextDateByDayOfWeek

class AlarmServiceImpl @Inject constructor(
    private val context: Context
) : AlarmService {
    override suspend fun setupAlarmWithOneTimeNotification(notification: OneTimeNotification) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        // check can setup exact time alarm
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !alarmManager.canScheduleExactAlarms()) {
            return
        }

        val intent = NotificationBroadcastReceiver.makeIntent(context, notification)
        val pendingIntent = PendingIntent.getBroadcast(context, getRequestCode(notification), intent, PendingIntent.FLAG_IMMUTABLE)

        val calendar = Calendar.getInstance().apply {
            time = notification.date
        }

        Log.d(TAG, "One time notification activated" +
                "\n id = ${notification.id}" +
                "\n title = ${notification.title}" +
                "\n time = ${calendar.time}")

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
            val pendingIntent = PendingIntent.getBroadcast(context, getRequestCode(notificationRule), intent, PendingIntent.FLAG_IMMUTABLE)

            val calendar = Calendar.getInstance().apply {
                time = findNextDateByDayOfWeek(notificationRule.dayOfWeek, notificationRule.time)
            }

            Log.d(TAG, "Periodic notification rule activated" +
                    "\n notification_id = ${notificationWithRules.notification.id}" +
                    "\n notification_title = ${notificationWithRules.notification.title}" +
                    "\n rule_id = ${notificationRule.id}" +
                    "\n time = ${calendar.time}")

            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )
        }
    }

    private fun getRequestCode(oneTimeNotification: OneTimeNotification): Int {
        return oneTimeNotification.id
    }

    private fun getRequestCode(notificationRule: PeriodicNotificationRule): Int {
        return -notificationRule.id
    }

    companion object {
        private const val TAG = "ALARM_SERVICE"
    }
}