package ldev.myNotifier.framework

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ldev.myNotifier.MainApplication
import ldev.myNotifier.core.AlarmProxy
import ldev.myNotifier.core.CustomLogBusinessLogic
import ldev.myNotifier.domain.repositories.NotificationRepository
import javax.inject.Inject

class BootCompletedBroadcastReceiver : BroadcastReceiver() {

    @Inject
    lateinit var alarmProxy: AlarmProxy

    @Inject
    lateinit var notificationRepository: NotificationRepository

    @Inject
    lateinit var customLogBusinessLogic: CustomLogBusinessLogic

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != Intent.ACTION_BOOT_COMPLETED) {
            return
        }
        (context.applicationContext as MainApplication).appComponent.inject(this)

        val pendingResult = goAsync()
        CoroutineScope(Dispatchers.IO).launch {
            customLogBusinessLogic.logDebug(TAG, "Boot completed broadcast receiver activated")

            val oneTimeNotificationsResult = notificationRepository.getOneTimeNotificationsWithActive(true)
            if (oneTimeNotificationsResult.success) {
                for (oneTimeNotification in oneTimeNotificationsResult.data!!) {
                    alarmProxy.setupAlarmWithOneTimeNotification(oneTimeNotification)
                }
            } else {
                customLogBusinessLogic.logDebug(TAG, "Unable to get one time notifications" +
                        "\n error message: ${oneTimeNotificationsResult.errorMessage}")
            }

            val periodicNotificationsResult = notificationRepository.getPeriodicNotifications()
            if (periodicNotificationsResult.success) {
                for (periodicNotificationWithRules in periodicNotificationsResult.data!!) {
                    alarmProxy.setupAlarmWithPeriodicNotification(periodicNotificationWithRules)
                }
            } else {
                customLogBusinessLogic.logDebug(TAG, "Unable to get periodic notifications" +
                        "\n error message: ${oneTimeNotificationsResult.errorMessage}")
            }

            customLogBusinessLogic.logDebug(TAG, "All active notifications are rescheduled")

            pendingResult.finish()
        }
    }

    companion object {
        private const val TAG = "BOOT_COMPLETED_BROADCAST_RECEIVER"
    }
}