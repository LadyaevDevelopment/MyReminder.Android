package ldev.myNotifier.framework

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import ldev.myNotifier.R
import ldev.myNotifier.domain.entities.OneTimeNotification
import ldev.myNotifier.domain.entities.PeriodicNotification
import ldev.myNotifier.domain.entities.PeriodicNotificationRule

class NotificationBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val notification = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(NOTIFICATION, NotificationModel::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(NOTIFICATION) as? NotificationModel
        }

        when (notification) {
            is NotificationModel.OneTime -> {
                handleOneTimeNotification(context, notification)
            }
            is NotificationModel.Periodic -> {
                handlePeriodicNotification(context, notification)
            }
            null -> return
        }
    }

    private fun handleOneTimeNotification(context: Context, oneTimeNotification: NotificationModel.OneTime) {
        // Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
        val channel = NotificationChannel("channel_id", "channel_name", NotificationManager.IMPORTANCE_DEFAULT)
        val notificationManager = context.getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)

        // TODO: change notification icon
        val notification = NotificationCompat.Builder(context, "channel_id")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(oneTimeNotification.title)
            .setContentText(oneTimeNotification.text)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        with(NotificationManagerCompat.from(context)) {
            // check can post notifications
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                val permissionResult = ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
                if (permissionResult != PackageManager.PERMISSION_GRANTED) {
                    return
                }
            }
            notify(NumberIncrementer.next(), notification)
        }
    }

    private fun handlePeriodicNotification(context: Context, periodicNotification: NotificationModel.Periodic) {
        // Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
        val channel = NotificationChannel("channel_id", "channel_name", NotificationManager.IMPORTANCE_DEFAULT)
        val notificationManager = context.getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)

        // TODO: change notification icon
        val notification = NotificationCompat.Builder(context, "channel_id")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(periodicNotification.title)
            .setContentText(periodicNotification.text)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        with(NotificationManagerCompat.from(context)) {
            // check can post notifications
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                val permissionResult = ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
                if (permissionResult != PackageManager.PERMISSION_GRANTED) {
                    return
                }
            }
            notify(NumberIncrementer.next(), notification)
        }
    }

    companion object {
        private const val NOTIFICATION = "notification"
        fun makeIntent(context: Context, oneTimeNotification: OneTimeNotification): Intent {
            return Intent(context, NotificationBroadcastReceiver::class.java).apply {
                putExtra(NOTIFICATION, NotificationModel.fromDomainEntity(oneTimeNotification))
            }
        }
        fun makeIntent(
            context: Context,
            periodicNotification: PeriodicNotification,
            notificationRule: PeriodicNotificationRule
        ): Intent {
            return Intent(context, NotificationBroadcastReceiver::class.java).apply {
                putExtra(NOTIFICATION, NotificationModel.fromDomainEntity(periodicNotification, notificationRule))
            }
        }
    }
}