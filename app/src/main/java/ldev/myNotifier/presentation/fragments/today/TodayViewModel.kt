package ldev.myNotifier.presentation.fragments.today

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ldev.myNotifier.domain.entities.NotificationType
import ldev.myNotifier.domain.entities.OneTimeNotification
import ldev.myNotifier.domain.entities.PeriodicNotificationWithRules
import ldev.myNotifier.domain.entities.TodayNotification
import ldev.myNotifier.domain.repositories.NotificationRepository
import ldev.myNotifier.utils.SingleEvent
import javax.inject.Inject

class TodayViewModel @Inject constructor(
    private val notificationRepository: NotificationRepository
) : ViewModel() {

    private val _state = MutableLiveData(UiState.initial())
    val state: LiveData<UiState> = _state

    private val _command: MutableLiveData<SingleEvent<UiAction>> = MutableLiveData(SingleEvent(UiAction.GetNotifications))
    val command: LiveData<SingleEvent<UiAction>> = _command

    fun getNotifications() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = notificationRepository.getNotificationsForToday()
            withContext(Dispatchers.Main) {
                _state.postValue(_state.value!!.copy(
                    notifications = result.data.orEmpty(),
                    errorMessage = result.errorMessage
                ))
            }
        }
    }

    fun tapNotification(notification: TodayNotification) {
        viewModelScope.launch(Dispatchers.IO) {
            when (notification.type) {
                NotificationType.OneTime -> {
                    val result = notificationRepository.getOneTimeNotification(notification.id)
                    if (result.success) {
                        _command.postValue(
                            SingleEvent(UiAction.GoToEditOneTimeNotification(
                                notification = result.data!!
                            ))
                        )
                    }
                }
                NotificationType.Periodic -> {
                    val result = notificationRepository.getPeriodicNotification(notification.id)
                    if (result.success) {
                        _command.postValue(
                            SingleEvent(UiAction.GoToEditPeriodicNotification(
                                notification = result.data!!
                            ))
                        )
                    }
                }
            }
        }
    }

    fun tapCreateOneTimeNotificationBtn() {
        _command.postValue(
            SingleEvent(
                UiAction.GoToEditOneTimeNotification(
                    notification = null
                )
            )
        )
    }

    fun tapCreatePeriodicNotificationBtn() {
        _command.postValue(
            SingleEvent(
                UiAction.GoToEditPeriodicNotification(
                    notification = null
                )
            )
        )
    }

    data class UiState(
        val notifications: List<TodayNotification>,
        val errorMessage: String?
    ) {
        companion object {
            fun initial() : UiState = UiState(
                notifications = listOf(),
                errorMessage = null
            )
        }
    }

    sealed class UiAction {
        object GetNotifications: UiAction()
        data class GoToEditOneTimeNotification(val notification: OneTimeNotification?): UiAction()
        data class GoToEditPeriodicNotification(val notification: PeriodicNotificationWithRules?): UiAction()
    }
}