package ldev.myNotifier.presentation.fragments.editPeriodicNotification

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import ldev.myNotifier.domain.entities.PeriodicNotification
import ldev.myNotifier.domain.repositories.NotificationRepository

class EditPeriodicNotificationViewModel @AssistedInject constructor(
    private val notificationRepository: NotificationRepository,
    @Assisted private val notification: PeriodicNotification?
) : ViewModel() {

    private val _state = MutableLiveData(UiState.initial())
    val state: LiveData<UiState> = _state

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

    fun markAll(checked: Boolean) {
        _state.postValue(_state.value!!.copy(
            allDaysOfWeekChecked = checked,
            daysOfWeek = _state.value!!.daysOfWeek.toMutableMap().apply {
                for ((dayOfWeek, _) in this) {
                    this[dayOfWeek] = this[dayOfWeek]!!.copy(checked = checked)
                }
            }
        ))
    }

    fun markDayOfWeek(dayOfWeek: String, checked: Boolean) {
        val daysOfWeek = _state.value!!.daysOfWeek.toMutableMap().apply {
            this[dayOfWeek] = this[dayOfWeek]!!.copy(checked = checked)
        }

        val allDaysOfWeekChecked = when {
            daysOfWeek.all { it.value.checked } -> true
            daysOfWeek.all { !it.value.checked } -> false
            else -> _state.value!!.allDaysOfWeekChecked
        }

        _state.postValue(_state.value!!.copy(
            allDaysOfWeekChecked = allDaysOfWeekChecked,
            daysOfWeek = daysOfWeek
        ))
    }

    fun addTimeToDayOfWeek(dayOfWeek: String, time: Time) {
        val dayOfWeekData = _state.value!!.daysOfWeek[dayOfWeek]!!
        _state.postValue(_state.value!!.copy(
            daysOfWeek = _state.value!!.daysOfWeek.toMutableMap().apply {
                this[dayOfWeek] = dayOfWeekData.copy(
                    times = dayOfWeekData.times.toMutableList().apply {
                        add(Time(hour = time.hour, minute = time.minute))
                    }
                )
            }
        ))
    }

    fun addTimeToAll(time: Time) {
        _state.postValue(_state.value!!.copy(
            daysOfWeek = _state.value!!.daysOfWeek.toMutableMap().apply {
                for ((dayOfWeek, dayOfWeekState) in this) {
                    this[dayOfWeek] = dayOfWeekState.copy(
                        times = dayOfWeekState.times.toMutableList().apply {
                            add(Time(hour = time.hour, minute = time.minute))
                        }
                    )
                }
            }
        ))
    }

    fun removeTimeFromDayOfWeek(dayOfWeek: String, time: Time) {
        val dayOfWeekData = _state.value!!.daysOfWeek[dayOfWeek]!!
        _state.postValue(_state.value!!.copy(
            daysOfWeek = _state.value!!.daysOfWeek.toMutableMap().apply {
                this[dayOfWeek] = dayOfWeekData.copy(
                    times = dayOfWeekData.times.toMutableList().apply {
                        remove(time)
                    }
                )
            }
        ))
    }

    fun clearSelectedDaysOfWeek() {
        _state.postValue(_state.value!!.copy(
            daysOfWeek = _state.value!!.daysOfWeek.toMutableMap().apply {
                for ((dayOfWeek, dayOfWeekState) in this) {
                    if (dayOfWeekState.checked) {
                        this[dayOfWeek] = dayOfWeekState.copy(
                            times = listOf()
                        )
                    }
                }
            }
        ))
    }

    init {
        if (notification != null) {
            setTitle(notification.title)
            setText(notification.text)
        }
    }

    data class UiState(
        val title: String,
        val text: String,
        val allDaysOfWeekChecked: Boolean,
        val daysOfWeek: Map<String, DayOfWeekState>,
        val errorMessage: String?
    ) {
        val areControlButtonsVisible: Boolean get() = daysOfWeek.any { it.value.checked }

        companion object {
            fun initial() : UiState = UiState(
                title = "",
                text = "",
                allDaysOfWeekChecked = false,
                daysOfWeek = arrayOf(
                    Pair("ПН", DayOfWeekState(checked = false, times = listOf())),
                    Pair("ВТ", DayOfWeekState(checked = false, times = listOf())),
                    Pair("СР", DayOfWeekState(checked = false, times = listOf())),
                    Pair("ЧТ", DayOfWeekState(checked = false, times = listOf())),
                    Pair("ПТ", DayOfWeekState(checked = false, times = listOf())),
                    Pair("СБ", DayOfWeekState(checked = false, times = listOf())),
                    Pair("ВС", DayOfWeekState(checked = false, times = listOf())),
                ).associate { it },
                errorMessage = null
            )
        }
    }

    data class DayOfWeekState(
        val checked: Boolean,
        val times: List<Time>
    )

    data class Time(val hour: Int, val minute: Int)

    @AssistedFactory
    interface EditPeriodicNotificationViewModelFactory {
        fun create(
            @Assisted notification: PeriodicNotification?,
        ): EditPeriodicNotificationViewModel
    }

    @Suppress("UNCHECKED_CAST")
    companion object {
        fun providesFactory(
            assistedFactory: EditPeriodicNotificationViewModelFactory,
            notification: PeriodicNotification?,
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return assistedFactory.create(notification) as T
            }
        }
    }

}