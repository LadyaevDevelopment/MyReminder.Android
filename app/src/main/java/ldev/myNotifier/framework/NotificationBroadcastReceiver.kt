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

    @SuppressLint("MissingPermission")
    private fun handleOneTimeNotification(context: Context, oneTimeNotification: NotificationModel.OneTime) {
        if (!possibleToShowNotification(context)) {
            return
        }
        // TODO: change notification icon
        val notification = createNotification(
            context,
            R.drawable.ic_launcher_foreground,
            oneTimeNotification.title,
            oneTimeNotification.text
        )

        with(NotificationManagerCompat.from(context)) {
            notify(oneTimeNotification.id, notification)
        }
    }

    @SuppressLint("MissingPermission")
    private fun handlePeriodicNotification(context: Context, periodicNotification: NotificationModel.Periodic) {
        if (!possibleToShowNotification(context)) {
            return
        }
        // TODO: change notification icon
        val notification = createNotification(
            context,
            R.drawable.ic_launcher_foreground,
            periodicNotification.title,
            periodicNotification.text
        )

        with(NotificationManagerCompat.from(context)) {
            notify(-periodicNotification.ruleId, notification)
        }
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

    companion object {
        private const val NOTIFICATION = "notification"
        private const val notificationChannelId = "mainChannelId"
        private const val notificationChannelName = "mainChannel"

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