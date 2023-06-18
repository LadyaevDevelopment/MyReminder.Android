package ldev.myNotifier.presentation.fragments.editPeriodicNotification

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import ldev.myNotifier.domain.entities.NotificationRule
import ldev.myNotifier.domain.entities.PeriodicNotification
import ldev.myNotifier.domain.entities.Time
import ldev.myNotifier.domain.repositories.NotificationRepository
import java.time.DayOfWeek

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

    fun markDayOfWeek(dayOfWeek: DayOfWeek, checked: Boolean) {
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

    fun addTimeToDayOfWeek(dayOfWeek: DayOfWeek, time: Time) {
        val dayOfWeekData = _state.value!!.daysOfWeek[dayOfWeek]!!
        _state.postValue(_state.value!!.copy(
            daysOfWeek = _state.value!!.daysOfWeek.toMutableMap().apply {
                this[dayOfWeek] = dayOfWeekData.copy(
                    times = dayOfWeekData.times.toMutableList().apply {
                        add(
                            NotificationTimeModel(
                                id = 0,
                                time = Time(
                                    hour = time.hour,
                                    minute = time.minute
                                )
                            )
                        )
                    }
                )
            }
        ))
    }

    fun addTimeToAllSelected(time: Time) {
        _state.postValue(_state.value!!.copy(
            daysOfWeek = _state.value!!.daysOfWeek.toMutableMap().apply {
                for ((dayOfWeek, dayOfWeekState) in this) {
                    if (dayOfWeekState.checked) {
                        this[dayOfWeek] = dayOfWeekState.copy(
                            times = dayOfWeekState.times.toMutableList().apply {
                                add(NotificationTimeModel(
                                    id = 0,
                                    time = Time(
                                        hour = time.hour,
                                        minute = time.minute
                                    )
                                ))
                            }
                        )
                    }
                }
            }
        ))
    }

    fun removeTimeFromDayOfWeek(dayOfWeek: DayOfWeek, time: Time) {
        val dayOfWeekData = _state.value!!.daysOfWeek[dayOfWeek]!!
        _state.postValue(_state.value!!.copy(
            daysOfWeek = _state.value!!.daysOfWeek.toMutableMap().apply {
                this[dayOfWeek] = dayOfWeekData.copy(
                    times = dayOfWeekData.times.toMutableList().apply {
                        removeIf { it.time == time }
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

            val daysOfWeek = _state.value!!.daysOfWeek.toMutableMap()
            for (dayOfWeek in DayOfWeek.values()) {
                daysOfWeek[dayOfWeek] = DayOfWeekState(
                    checked = false,
                    times = notification.rules
                        .filter { it.dayOfWeek == dayOfWeek }
                        .map { NotificationTimeModel(id = it.id, time = it.time) }
                )
            }
        }
    }

    fun save() {
        val state = _state.value!!
        val rules = state.daysOfWeek.entries.map { (dayOfWeek, dayOfWeekData) ->
            dayOfWeekData.times.map { time ->
                NotificationRule(
                    id = time.id,
                    time = time.time,
                    dayOfWeek = dayOfWeek
                )
            }
        }.flatten()

        if (rules.isEmpty()) {
            _state.postValue(_state.value!!.copy(
                errorMessage = "You must specify at least one notification time rule"
            ))
            return
        }
        notificationRepository.savePeriodicNotification(
            notification = PeriodicNotification(
                id = notification?.id ?: 0,
                title = state.title,
                text = state.text,
                rules = rules
            )
        )
    }

    data class UiState(
        val title: String,
        val text: String,
        val errorMessage: String?,
        val allDaysOfWeekChecked: Boolean,
        val daysOfWeek: Map<DayOfWeek, DayOfWeekState>,
    ) {
        val areControlButtonsVisible: Boolean get() = daysOfWeek.any { it.value.checked }

        companion object {
            fun initial() : UiState = UiState(
                title = "",
                text = "",
                errorMessage = null,
                allDaysOfWeekChecked = false,
                daysOfWeek = DayOfWeek.values()
                    .map { Pair(it, DayOfWeekState(checked = false, times = listOf())) }
                    .associate { it },
            )
        }
    }

    data class DayOfWeekState(
        val checked: Boolean,
        val times: List<NotificationTimeModel>
    )

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