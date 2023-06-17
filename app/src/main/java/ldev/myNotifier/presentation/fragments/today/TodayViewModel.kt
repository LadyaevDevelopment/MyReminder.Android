package ldev.myNotifier.presentation.fragments.today

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ldev.myNotifier.domain.entities.TodayNotification
import ldev.myNotifier.domain.repositories.NotificationRepository
import javax.inject.Inject

class TodayViewModel @Inject constructor(
    private val notificationRepository: NotificationRepository
) : ViewModel() {

    private val _state = MutableLiveData(TodayUiState.initial())
    val state: LiveData<TodayUiState> = _state

    fun getNotifications() {
        viewModelScope.launch {
            notificationRepository.getNotificationsForToday().collect { result ->
                withContext(Dispatchers.Main) {
                    _state.postValue(_state.value!!.copy(
                        notifications = result.data.orEmpty(),
                        errorMessage = result.errorMessage
                    ))
                }
            }
        }
    }

}

data class TodayUiState(
    val notifications: List<TodayNotification>,
    val errorMessage: String?
) {
    companion object {
        fun initial() : TodayUiState = TodayUiState(
            notifications = listOf(),
            errorMessage = null
        )
    }
}