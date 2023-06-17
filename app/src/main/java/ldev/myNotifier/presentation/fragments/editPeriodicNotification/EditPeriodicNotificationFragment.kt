package ldev.myNotifier.presentation.fragments.editPeriodicNotification

import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.launch
import ldev.myNotifier.databinding.FragmentEditPeriodicNotificationBinding
import ldev.myNotifier.databinding.LayoutDayOfWeekBinding
import ldev.myNotifier.presentation.appComponent
import ldev.myNotifier.utils.BaseFragment
import ldev.myNotifier.utils.addChildView
import ldev.myNotifier.utils.atLeastTwoDigits
import ldev.myNotifier.utils.dpToPixels
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

    override fun getContentInflater(): (LayoutInflater, ViewGroup?, Boolean) -> FragmentEditPeriodicNotificationBinding {
        return FragmentEditPeriodicNotificationBinding::inflate
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
                viewModel.addTimeToAll(
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

                    binding.daysOfWeek.removeAllViews()
                    state.daysOfWeek.map { (dayOfWeek, dayOfWeekState) ->
                        layoutSections(dayOfWeek, dayOfWeekState.checked, dayOfWeekState.times)
                    }
                }
            }
        }
    }

    private fun layoutSections(name: String, checked: Boolean, timeButtons: List<EditPeriodicNotificationViewModel.Time>) {
        val viewBinding = LayoutDayOfWeekBinding.inflate(LayoutInflater.from(requireContext()))
        viewBinding.title.text = name
        viewBinding.buttons.isGone = true
        viewBinding.check.isChecked = checked
        viewBinding.check.setOnCheckedChangeListener { _, isChecked ->
            viewModel.markDayOfWeek(name, isChecked)
        }
        viewBinding.addBtn.setOnClickListener {
            showTimePicker { hour, minute ->
                viewModel.addTimeToDayOfWeek(
                    name,
                    EditPeriodicNotificationViewModel.Time(
                        hour = hour,
                        minute = minute
                    )
                )
            }
        }
        viewBinding.buttons.isVisible = timeButtons.isNotEmpty()
        for (time in timeButtons) {
            viewBinding.buttons.addChildView(createButton().apply {
                text = run { "${time.hour.atLeastTwoDigits()} : ${time.minute.atLeastTwoDigits()}" }
                setOnClickListener {
                    viewModel.removeTimeFromDayOfWeek(name, time)
                }
            })
        }
        binding.daysOfWeek.addView(viewBinding.root)
        (viewBinding.root.layoutParams as MarginLayoutParams).bottomMargin = dpToPixels(8)
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

    private fun createButton(): MaterialButton {
        val button = MaterialButton(requireContext())
        button.minWidth = 0
        button.minHeight = 0
        button.minimumWidth = 0
        button.minimumHeight = 0
        button.outlineProvider = null
        button.insetTop = 0
        button.insetBottom = 0
        return button
    }

}