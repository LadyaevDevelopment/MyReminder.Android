package ldev.myNotifier.presentation.fragments.editPeriodicNotification

import androidx.annotation.StringRes
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
import ldev.myNotifier.R
import ldev.myNotifier.core.NotificationBusinessLogic
import ldev.myNotifier.domain.entities.PeriodicNotificationRule
import ldev.myNotifier.domain.entities.PeriodicNotification
import ldev.myNotifier.domain.entities.PeriodicNotificationWithRules
import ldev.myNotifier.domain.entities.Time
import ldev.myNotifier.utils.SingleLiveData
import ldev.myNotifier.utils.SingleMutableLiveData

import java.time.DayOfWeek

class EditPeriodicNotificationViewModel @AssistedInject constructor(
    private val notificationBusinessLogic: NotificationBusinessLogic,
    @Assisted private val notificationWithRules: PeriodicNotificationWithRules?
) : ViewModel() {

    private val _state: MutableLiveData<UiState>
    val state: LiveData<UiState> get() = _state

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
        val initialState = UiState.initial()
        if (notificationWithRules != null) {
            val daysOfWeek = initialState.daysOfWeek.toMutableMap()
            for (dayOfWeek in DayOfWeek.values()) {
                daysOfWeek[dayOfWeek] = DayOfWeekState(
                    checked = false,
                    times = notificationWithRules.rules
                        .filter { it.dayOfWeek == dayOfWeek }
                        .map { NotificationTimeModel(id = it.id, time = it.time) }
                )
            }
            _state = MutableLiveData(
                initialState.copy(
                    title = notificationWithRules.notification.title,
                    text = notificationWithRules.notification.text,
                    daysOfWeek = daysOfWeek
                )
            )
        } else {
            _state = MutableLiveData(initialState)
        }
    }

    fun save() {
        val state = _state.value!!
        val rules = state.daysOfWeek.entries.map { (dayOfWeek, dayOfWeekData) ->
            dayOfWeekData.times.map { time ->
                PeriodicNotificationRule(
                    id = time.id,
                    time = time.time,
                    dayOfWeek = dayOfWeek
                )
            }
        }.flatten()

        if (rules.isEmpty()) {
            _action.applyValue(UiAction.ShowToast(R.string.noPeriodicNoificationRules))
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            notificationBusinessLogic.savePeriodicNotification(
                PeriodicNotificationWithRules(
                    notification = PeriodicNotification(
                        id = notificationWithRules?.notification?.id ?: 0,
                        title = state.title,
                        text = state.text,
                    ),
                    rules = rules
                )
            )
            _action.applyValue(UiAction.Back)
        }
    }

    data class UiState(
        val title: String,
        val text: String,
        val allDaysOfWeekChecked: Boolean,
        val daysOfWeek: Map<DayOfWeek, DayOfWeekState>,
    ) {
        val areControlButtonsVisible: Boolean get() = daysOfWeek.any { it.value.checked }

        companion object {
            fun initial() : UiState = UiState(
                title = "",
                text = "",
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

    sealed class UiAction {
        object Back: UiAction()
        data class ShowToast(@StringRes val messageRes: Int): UiAction()
    }

    @AssistedFactory
    interface EditPeriodicNotificationViewModelFactory {
        fun create(
            @Assisted notificationWithRules: PeriodicNotificationWithRules?,
        ): EditPeriodicNotificationViewModel
    }

    @Suppress("UNCHECKED_CAST")
    companion object {
        fun providesFactory(
            assistedFactory: EditPeriodicNotificationViewModelFactory,
            notificationWithRules: PeriodicNotificationWithRules?,
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return assistedFactory.create(notificationWithRules) as T
            }
        }
    }

}