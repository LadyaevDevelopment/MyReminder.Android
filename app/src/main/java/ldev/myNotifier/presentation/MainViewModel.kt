package ldev.myNotifier.presentation

import android.app.Application
import android.app.NotificationManager
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ldev.myNotifier.utils.SingleEvent
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val application: Application
) : AndroidViewModel(application) {

    private val _action: MutableLiveData<SingleEvent<UiAction>> = MutableLiveData()
    val action: LiveData<SingleEvent<UiAction>> = _action

    init {
        requestPostNotificationsPermissionIfNeeded()
    }

    private fun requestPostNotificationsPermissionIfNeeded() {
        if (!areNotificationsEnabled(application)) {
            _action.value = SingleEvent(UiAction.ShowPostNotificationsPermissionExplanationDialog)
        }
    }

    fun postNotificationsPermissionExplanationDialogDismissed() {
        _action.value = SingleEvent(UiAction.AskPostNotificationsPermission)
    }

    fun postNotificationsPermissionDeniedForever() {
        _action.value = SingleEvent(UiAction.ShowNeedToOpenNotificationSettingsDialog)
    }

    fun cannotOpenNotificationSettings() {
        _action.value = SingleEvent((UiAction.ShowCannotOpenNotificationSettingsToast))
    }

    private fun areNotificationsEnabled(context: Context): Boolean {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        return notificationManager.areNotificationsEnabled()
    }

    sealed class UiAction {
        object ShowPostNotificationsPermissionExplanationDialog: UiAction()
        object ShowNeedToOpenNotificationSettingsDialog: UiAction()
        object ShowCannotOpenNotificationSettingsToast: UiAction()
        object AskPostNotificationsPermission: UiAction()
    }
}