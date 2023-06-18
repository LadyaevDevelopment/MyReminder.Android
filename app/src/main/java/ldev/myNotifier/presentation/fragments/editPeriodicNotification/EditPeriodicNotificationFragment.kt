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
import ldev.myNotifier.presentation.appComponent
import ldev.myNotifier.utils.BaseFragment
import ldev.myNotifier.utils.VerticalItemDecorator
import ldev.myNotifier.utils.dpToPixels
import ldev.myNotifier.utils.recyclerView.FingerprintAdapter
import java.util.Calendar
import javax.inject.Inject

class EditPeriodicNotificationFragment : BaseFragment<FragmentEditPeriodicNotificationBinding>() {

    @Inject
    lateinit var viewModelFactory: EditPeriodicNotificationViewModel.EditPeriodicNotificationViewModelFactory
    private val viewModel: EditPeriodicNotificationViewModel by viewModels {
        EditPeriodicNotificationViewModel.providesFactory(
            assistedFactory = viewModelFactory,
            notification = null
        )
    }

    private var _daysOfWeekAdapter: FingerprintAdapter? = FingerprintAdapter(listOf(
        DayOfWeekFingerprint(
            onAddButtonTapped = fun (dayOfWeek) {
                showTimePicker { hour, minute ->
                    viewModel.addTimeToDayOfWeek(
                        dayOfWeek,
                        EditPeriodicNotificationViewModel.Time(
                            hour = hour,
                            minute = minute
                        )
                    )
                }
            },
            onMarkItem = fun (dayOfWeek, isChecked) {
                viewModel.markDayOfWeek(dayOfWeek, isChecked)
            },
            onRemoveTime = fun (dayOfWeek, time) {
                viewModel.removeTimeFromDayOfWeek(
                    dayOfWeek,
                    EditPeriodicNotificationViewModel.Time(hour = time.hour, minute = time.minute)
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
            showTimePicker { hour, minute ->
                viewModel.addTimeToAllSelected(
                    EditPeriodicNotificationViewModel.Time(
                        hour = hour,
                        minute = minute
                    )
                )
            }
        }
        binding.removeBtn.setOnClickListener {
            viewModel.clearSelectedDaysOfWeek()
        }

        subscribeToViewModel()
    }

    private fun subscribeToViewModel() {
        viewModel.textState.observe(viewLifecycleOwner) { state ->
            lifecycleScope.launch {
                lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                    if (binding.titleInput.text.toString() != state.title) {
                        binding.titleInput.setText(state.title)
                    }
                    if (binding.textInput.text.toString() != state.text) {
                        binding.textInput.setText(state.text)
                    }
                }
            }
        }
        viewModel.rulesState.observe(viewLifecycleOwner) { state ->
            lifecycleScope.launch {
                lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                    binding.checkAll.isChecked = state.allDaysOfWeekChecked
                    binding.controlButtons.isVisible = state.areControlButtonsVisible

                    daysOfWeekAdapter.submitList(
                        state.daysOfWeek.map { (dayOfWeek, dayOfWeekState) ->
                            DayOfWeekModel(
                                dayOfWeek = dayOfWeek,
                                isChecked = dayOfWeekState.checked,
                                times = dayOfWeekState.times.map {
                                    NotificationTime(id = 0, hour = it.hour, minute = it.minute)
                                }
                            )
                        }
                    )
                }
            }
        }
    }

    private fun showTimePicker(onAddTime: (hour: Int, minute: Int) -> Unit) {
        val calendar = Calendar.getInstance()
        val initialHour = calendar.get(Calendar.HOUR_OF_DAY)
        val initialMinute = calendar.get(Calendar.MINUTE)
        val timePickerDialog = TimePickerDialog(
            requireContext(),
            { _, hour, minute ->
                onAddTime(hour, minute)
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