package ldev.myNotifier.presentation.fragments.editPeriodicNotification

import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import ldev.myNotifier.databinding.FragmentEditPeriodicNotificationBinding
import ldev.myNotifier.domain.entities.NotificationRule
import ldev.myNotifier.domain.entities.PeriodicNotification
import ldev.myNotifier.domain.entities.Time
import ldev.myNotifier.presentation.appComponent
import ldev.myNotifier.utils.BaseFragment
import ldev.myNotifier.utils.VerticalItemDecorator
import ldev.myNotifier.utils.dpToPixels
import ldev.myNotifier.utils.recyclerView.FingerprintAdapter
import java.time.DayOfWeek
import java.util.Calendar
import javax.inject.Inject

class EditPeriodicNotificationFragment : BaseFragment<FragmentEditPeriodicNotificationBinding>() {

    @Inject
    lateinit var viewModelFactory: EditPeriodicNotificationViewModel.EditPeriodicNotificationViewModelFactory
    private val viewModel: EditPeriodicNotificationViewModel by viewModels {
        EditPeriodicNotificationViewModel.providesFactory(
            assistedFactory = viewModelFactory,
            notification = PeriodicNotification(
                id = 1,
                title = "My first notification",
                text = "Stuff...",
                rules = listOf(
                    NotificationRule(id = 0, dayOfWeek = DayOfWeek.MONDAY, time = Time(hour = 12, minute = 56)),
                    NotificationRule(id = 0, dayOfWeek = DayOfWeek.MONDAY, time = Time(hour = 21, minute = 56)),
                    NotificationRule(id = 0, dayOfWeek = DayOfWeek.WEDNESDAY, time = Time(hour = 20, minute = 56)),
                    NotificationRule(id = 0, dayOfWeek = DayOfWeek.WEDNESDAY, time = Time(hour = 18, minute = 56)),
                    NotificationRule(id = 0, dayOfWeek = DayOfWeek.WEDNESDAY, time = Time(hour = 12, minute = 56)),
                    NotificationRule(id = 0, dayOfWeek = DayOfWeek.FRIDAY, time = Time(hour = 12, minute = 56)),
                    NotificationRule(id = 0, dayOfWeek = DayOfWeek.SUNDAY, time = Time(hour = 4, minute = 56)),
                    NotificationRule(id = 0, dayOfWeek = DayOfWeek.SUNDAY, time = Time(hour = 12, minute = 56)),
                )
            )
        )
    }

    private var _daysOfWeekAdapter: FingerprintAdapter? = FingerprintAdapter(listOf(
        DayOfWeekFingerprint(
            onAddButtonTapped = fun (dayOfWeek) {
                showTimePicker { time ->
                    viewModel.addTimeToDayOfWeek(
                        dayOfWeek,
                        time
                    )
                }
            },
            onMarkItem = fun (dayOfWeek, isChecked) {
                viewModel.markDayOfWeek(dayOfWeek, isChecked)
            },
            onRemoveTime = fun (dayOfWeek, time) {
                viewModel.removeTimeFromDayOfWeek(
                    dayOfWeek,
                    time.time
                )
            },
        )
    ))
    private val daysOfWeekAdapter = _daysOfWeekAdapter!!

    override fun getContentInflater(): (LayoutInflater, ViewGroup?, Boolean) -> FragmentEditPeriodicNotificationBinding {
        return FragmentEditPeriodicNotificationBinding::inflate
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.daysOfWeek.itemAnimator = null
        binding.daysOfWeek.adapter = daysOfWeekAdapter
        binding.daysOfWeek.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.daysOfWeek.addItemDecoration(
            VerticalItemDecorator(
                innerDivider = dpToPixels(8),
                outerDivider = dpToPixels(0)
            )
        )

        binding.titleInput.addTextChangedListener {
            viewModel.setTitle(it.toString())
        }
        binding.textInput.addTextChangedListener {
            viewModel.setText(it.toString())
        }
        binding.checkAll.setOnCheckedChangeListener { _, isChecked ->
            viewModel.markAll(isChecked)
        }
        binding.addBtn.setOnClickListener {
            showTimePicker { time ->
                viewModel.addTimeToAllSelected(time)
            }
        }
        binding.removeBtn.setOnClickListener {
            viewModel.clearSelectedDaysOfWeek()
        }
        binding.saveBtn.setOnClickListener {
            viewModel.save()
        }

        subscribeToViewModel()
    }

    private fun subscribeToViewModel() {
        viewModel.state.observe(viewLifecycleOwner) { state ->
            lifecycleScope.launch {
                lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                    if (binding.titleInput.text.toString() != state.title) {
                        binding.titleInput.setText(state.title)
                    }
                    if (binding.textInput.text.toString() != state.text) {
                        binding.textInput.setText(state.text)
                    }
                    binding.checkAll.isChecked = state.allDaysOfWeekChecked
                    binding.controlButtons.isVisible = state.areControlButtonsVisible
                    daysOfWeekAdapter.submitList(
                        state.daysOfWeek.map { (dayOfWeek, dayOfWeekState) ->
                            DayOfWeekModel(
                                dayOfWeek = dayOfWeek,
                                isChecked = dayOfWeekState.checked,
                                times = dayOfWeekState.times
                            )
                        }
                    )
                }
            }
        }
    }

    private fun showTimePicker(onAddTime: (time: Time) -> Unit) {
        val calendar = Calendar.getInstance()
        val initialHour = calendar.get(Calendar.HOUR_OF_DAY)
        val initialMinute = calendar.get(Calendar.MINUTE)
        val timePickerDialog = TimePickerDialog(
            requireContext(),
            { _, hour, minute ->
                onAddTime(Time(hour = hour, minute = minute))
            },
            initialHour,
            initialMinute,
            DateFormat.is24HourFormat(context)
        )
        timePickerDialog.show()
    }

    override fun onDestroy() {
        _daysOfWeekAdapter = null
        super.onDestroy()
    }

}