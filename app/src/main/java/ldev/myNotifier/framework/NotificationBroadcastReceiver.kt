package ldev.myNotifier.framework

import android.Manifest
import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.DrawableRes
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ldev.myNotifier.MainApplication
import ldev.myNotifier.R
import ldev.myNotifier.core.AlarmProxy
import ldev.myNotifier.core.CustomLogBusinessLogic
import ldev.myNotifier.domain.entities.OneTimeNotification
import ldev.myNotifier.domain.entities.PeriodicNotification
import ldev.myNotifier.domain.entities.PeriodicNotificationRule
import javax.inject.Inject

class NotificationBroadcastReceiver : BroadcastReceiver() {

    @Inject
    lateinit var alarmProxy: AlarmProxy

    @Inject
    lateinit var customLogBusinessLogic: CustomLogBusinessLogic

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    override fun onReceive(context: Context, intent: Intent) {
        (context.applicationContext as MainApplication).appComponent.inject(this)

        val notification = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(PARCELABLE_NOTIFICATION, NotificationModel::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(PARCELABLE_NOTIFICATION) as? NotificationModel
        }

        val pendingResult = goAsync()
        coroutineScope.launch {
            when (notification) {
                is NotificationModel.OneTime -> {
                    handleOneTimeNotification(context, notification)
                }
                is NotificationModel.Periodic -> {
                    handlePeriodicNotification(context, notification)
                }
                null -> {

                }
            }

            customLogBusinessLogic.logDebug(TAG, "Notification broadcast receiver activated" +
                    "\n notification_id = ${notification?.id}" +
                    "\n notification_title = ${notification?.title}")

            pendingResult.finish()
        }
    }

    @SuppressLint("MissingPermission")
    private fun handleOneTimeNotification(context: Context, oneTimeNotification: NotificationModel.OneTime) {
        if (!possibleToShowNotification(context)) {
            return
        }
        val notification = createNotification(
            context,
            R.drawable.notification,
            oneTimeNotification.title,
            oneTimeNotification.text
        )

        with(NotificationManagerCompat.from(context)) {
            notify(getNotificationId(oneTimeNotification), notification)
        }
    }

    @SuppressLint("MissingPermission")
    private suspend fun handlePeriodicNotification(context: Context, periodicNotification: NotificationModel.Periodic) {
        if (!possibleToShowNotification(context)) {
            return
        }
        val notification = createNotification(
            context,
            R.drawable.notification,
            periodicNotification.title,
            periodicNotification.text
        )

        with(NotificationManagerCompat.from(context)) {
            notify(getNotificationId(periodicNotification), notification)
        }

        val (notificationToReschedule, notificationRule) = periodicNotification.notificationWithRule()
        alarmProxy.reschedulePeriodicNotificationRule(notificationToReschedule, notificationRule)
    }

    private fun createNotification(
        context: Context,
        @DrawableRes iconDrawableRes: Int,
        title: String,
        text: String
    ): Notification {
        // Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
        val channel = NotificationChannel(notificationChannelId, notificationChannelName, NotificationManager.IMPORTANCE_DEFAULT)
        val notificationManager = context.getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)

        return NotificationCompat.Builder(context, notificationChannelId)
            .setSmallIcon(iconDrawableRes)
            .setContentTitle(title)
            .setContentText(text)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()
    }

    private fun possibleToShowNotification(context: Context) : Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val permissionResult = ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
            return permissionResult == PackageManager.PERMISSION_GRANTED
        }
        return true
    }

    private fun getNotificationId(oneTimeNotification: NotificationModel.OneTime): Int {
        return oneTimeNotification.id
    }

    private fun getNotificationId(periodicNotification: NotificationModel.Periodic): Int {
        return -periodicNotification.ruleId
    }

    companion object {
        private const val PARCELABLE_NOTIFICATION = "notification"
        private const val notificationChannelId = "mainChannelId"
        private const val notificationChannelName = "mainChannel"

        private const val TAG = "NOTIFICATION_BROADCAST_RECEIVER"

        fun makeIntent(context: Context, oneTimeNotification: OneTimeNotification): Intent {
            return Intent(context, NotificationBroadcastReceiver::class.java).apply {
                putExtra(PARCELABLE_NOTIFICATION, NotificationModel.fromDomainEntity(oneTimeNotification))
            }
        }
        fun makeIntent(
            context: Context,
            periodicNotification: PeriodicNotification,
            notificationRule: PeriodicNotificationRule
        ): Intent {
            return Intent(context, NotificationBroadcastReceiver::class.java).apply {
                putExtra(PARCELABLE_NOTIFICATION, NotificationModel.fromDomainEntity(periodicNotification, notificationRule))
            }
        }
    }
}