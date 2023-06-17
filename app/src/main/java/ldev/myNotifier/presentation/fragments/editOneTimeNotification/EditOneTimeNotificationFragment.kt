package ldev.myNotifier.presentation.fragments.editOneTimeNotification

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Context.VIBRATOR_SERVICE
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.view.LayoutInflater
import android.view.View
import android.text.format.DateFormat
import android.view.ViewGroup
import android.widget.NumberPicker
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch
import ldev.myNotifier.databinding.FragmentEditOneTimeNotificationBinding
import ldev.myNotifier.databinding.LayoutIntervalPickerBinding
import ldev.myNotifier.presentation.appComponent
import ldev.myNotifier.utils.BaseFragment
import ldev.myNotifier.utils.atLeastTwoDigits
import ldev.myNotifier.utils.formatAsFullDayFullMonthFullYear
import ldev.myNotifier.utils.formatAsHoursMinutes
import java.util.Calendar
import javax.inject.Inject

class EditOneTimeNotificationFragment : BaseFragment<FragmentEditOneTimeNotificationBinding>() {

    @Inject
    lateinit var viewModelFactory: EditOneTimeNotificationViewModel.EditOneTimeNotificationViewModelFactory
    private val viewModel: EditOneTimeNotificationViewModel by viewModels {
        EditOneTimeNotificationViewModel.providesFactory(
            assistedFactory = viewModelFactory,
            notification = null
        )
    }

    override fun getContentInflater(): (LayoutInflater, ViewGroup?, Boolean) -> FragmentEditOneTimeNotificationBinding {
        return FragmentEditOneTimeNotificationBinding::inflate
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.observe(viewLifecycleOwner) { state ->
                    lifecycleScope.launch {
                        lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                            binding.titleInput.setText(state.title)
                            binding.textInput.setText(state.text)
                            binding.interval.text = when (state.notificationTime) {
                                is EditOneTimeNotificationViewModel.NotificationTime.DateTime -> {
                                    val date = state.notificationTime.dateTime
                                    "${date.formatAsFullDayFullMonthFullYear()}, ${date.formatAsHoursMinutes()}"
                                }
                                is EditOneTimeNotificationViewModel.NotificationTime.Interval -> {
                                    val interval = state.notificationTime
                                    "Через ${interval.hours.atLeastTwoDigits()} : ${interval.minutes.atLeastTwoDigits()}"
                                }
                                null -> "Время не выбрано"
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.afterBtn.setOnClickListener {
            val intervalPickerBinding = LayoutIntervalPickerBinding.inflate(LayoutInflater.from(requireContext()))

            configureNumberPicker(intervalPickerBinding.hourPicker, 0, 23)
            configureNumberPicker(intervalPickerBinding.minutePicker, 1, 59)

            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Выберите интервал")
            builder.setView(intervalPickerBinding.root)
            builder.setPositiveButton("OK") { dialog, which ->
                viewModel.setNotificationTime(
                    EditOneTimeNotificationViewModel.NotificationTime.Interval(
                        hours = intervalPickerBinding.hourPicker.value,
                        minutes = intervalPickerBinding.minutePicker.value
                    )
                )
            }
            builder.setNegativeButton("Отмена") { dialog, which ->
                dialog.dismiss()
            }
            val dialog = builder.create()
            dialog.show()
        }

        binding.exactTimeBtn.setOnClickListener {
            val calendar = Calendar.getInstance()
            val initialYear = calendar.get(Calendar.YEAR)
            val initialMonth = calendar.get(Calendar.MONTH)
            val initialDay = calendar.get(Calendar.DAY_OF_MONTH)
            val initialHour = calendar.get(Calendar.HOUR_OF_DAY)
            val initialMinute = calendar.get(Calendar.MINUTE)

            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { _, year, monthOfYear, dayOfMonth ->
                    val timePickerDialog = TimePickerDialog(
                        requireContext(),
                        { _, hourOfDay, minute ->
                            val selectedDateTime = Calendar.getInstance()
                            selectedDateTime.set(year, monthOfYear, dayOfMonth, hourOfDay, minute)
                            viewModel.setNotificationTime(
                                EditOneTimeNotificationViewModel.NotificationTime.DateTime(
                                    dateTime = selectedDateTime.time
                                )
                            )
                        },
                        initialHour,
                        initialMinute,
                        DateFormat.is24HourFormat(context)
                    )
                    timePickerDialog.show()
                },
                initialYear,
                initialMonth,
                initialDay
            )
            datePickerDialog.show()
        }
    }

    private fun configureNumberPicker(picker: NumberPicker, minValue: Int, maxValue: Int) {
        // vibrator
        val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = requireActivity().getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            requireActivity().getSystemService(VIBRATOR_SERVICE) as Vibrator
        }
        val handler = Handler(Looper.getMainLooper())
        val vibrationRunnable = Runnable {
            val effect = VibrationEffect.createOneShot(150, 1)
            vibrator.vibrate(effect)
        }
        // picker
        picker.minValue = minValue
        picker.maxValue = maxValue
        picker.wrapSelectorWheel = false
        picker.setOnValueChangedListener { _, _, _ ->
            handler.post(vibrationRunnable)
        }
    }

}