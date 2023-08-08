package ldev.myNotifier.presentation.fragments.editOneTimeNotification

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ldev.myNotifier.core.NotificationCoordinator
import ldev.myNotifier.domain.entities.OneTimeNotification
import ldev.myNotifier.utils.SingleLiveData
import ldev.myNotifier.utils.SingleMutableLiveData
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.Date

class EditOneTimeNotificationViewModel @AssistedInject constructor(
    private val notificationCoordinator: NotificationCoordinator,
    @Assisted private val notification: OneTimeNotification?
) : ViewModel() {

    private val _state = MutableLiveData(UiState.initial())
    val state: LiveData<UiState> = _state

    private val _action = SingleMutableLiveData<UiAction>()
    val action: SingleLiveData<UiAction> = _action

    fun setTitle(title: String) {
        _state.postValue(_state.value!!.copy(
            title = title
        ))
    }

    fun setText(text: String) {
        _state.postValue(_state.value!!.copy(
            text = text
        ))
    }

    fun setNotificationTime(notificationTime: NotificationTime) {
        _state.postValue(_state.value!!.copy(
            notificationTime = notificationTime
        ))
    }

    init {
        if (notification != null) {
            _state.postValue(_state.value!!.copy(
                title = notification.title,
                text = notification.text,
                notificationTime = NotificationTime.DateTime(notification.date)
            ))
        }
    }

    fun save() {
        val state = _state.value!!
        if (state.notificationTime == null) {
            _state.postValue(_state.value!!.copy(
                errorMessage = "Notification time is not specified"
            ))
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            notificationCoordinator.saveOneTimeNotification(
                notification = OneTimeNotification(
                    id = notification?.id ?: 0,
                    title = state.title,
                    text = state.text,
                    date = resolveNotificationTime(state.notificationTime)
                )
            )
            _action.applyValue(UiAction.Back)
        }
    }

    private fun resolveNotificationTime(notificationTime: NotificationTime): Date {
        return when (notificationTime) {
            is NotificationTime.DateTime -> notificationTime.dateTime
            is NotificationTime.Interval -> Date.from(
                Instant.now()
                    .plus(notificationTime.hours.toLong(), ChronoUnit.HOURS)
                    .plus(notificationTime.minutes.toLong(), ChronoUnit.MINUTES)
            )
        }
    }

    sealed class NotificationTime {
        data class Interval(val hours: Int, val minutes: Int): NotificationTime()
        data class DateTime(val dateTime: Date): NotificationTime()
    }

    data class UiState(
        val title: String,
        val text: String,
        val notificationTime: NotificationTime?,
        val errorMessage: String?
    ) {
        companion object {
            fun initial() : UiState = UiState(
                title = "",
                text = "",
                notificationTime = null,
                errorMessage = null
            )
        }
    }

    sealed class UiAction {
        object Back: UiAction()
    }

    @AssistedFactory
    interface EditOneTimeNotificationViewModelFactory {
        fun create(
            @Assisted notification: OneTimeNotification?,
        ): EditOneTimeNotificationViewModel
    }

    @Suppress("UNCHECKED_CAST")
    companion object {
        fun providesFactory(
            assistedFactory: EditOneTimeNotificationViewModelFactory,
            notification: OneTimeNotification?,
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return assistedFactory.create(notification) as T
            }
        }
    }
}