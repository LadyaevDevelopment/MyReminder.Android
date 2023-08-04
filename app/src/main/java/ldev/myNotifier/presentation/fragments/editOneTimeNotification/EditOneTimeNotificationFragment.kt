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
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import kotlinx.coroutines.launch
import ldev.myNotifier.R
import ldev.myNotifier.databinding.FragmentEditOneTimeNotificationBinding
import ldev.myNotifier.databinding.LayoutIntervalPickerBinding
import ldev.myNotifier.debugSettings.DebugSettings
import ldev.myNotifier.presentation.appComponent
import ldev.myNotifier.utils.BaseFragment
import ldev.myNotifier.utils.atLeastTwoDigits
import ldev.myNotifier.utils.formatAsFullDayFullMonthFullYear
import ldev.myNotifier.utils.formatAsHoursMinutes
import java.util.Calendar
import javax.inject.Inject

class EditOneTimeNotificationFragment : BaseFragment<FragmentEditOneTimeNotificationBinding>() {

    private val args: EditOneTimeNotificationFragmentArgs by navArgs()

    @Inject
    lateinit var debugSettings: DebugSettings

    @Inject
    lateinit var viewModelFactory: EditOneTimeNotificationViewModel.EditOneTimeNotificationViewModelFactory
    private val viewModel: EditOneTimeNotificationViewModel by viewModels {
        EditOneTimeNotificationViewModel.providesFactory(
            assistedFactory = viewModelFactory,
            notification = args.notification?.toDomainEntity() ?: debugSettings.editOneTimeNotificationSettings.notification
        )
    }

    override fun getContentInflater(): (LayoutInflater, ViewGroup?, Boolean) -> FragmentEditOneTimeNotificationBinding {
        return FragmentEditOneTimeNotificationBinding::inflate
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
        binding.afterBtn.setOnClickListener {
            showIntervalPicker()
        }
        binding.exactTimeBtn.setOnClickListener {
            showDateTimePicker()
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
                    binding.interval.text = when (state.notificationTime) {
                        is EditOneTimeNotificationViewModel.NotificationTime.DateTime -> {
                            val date = state.notificationTime.dateTime
                            "${date.formatAsFullDayFullMonthFullYear()}, ${date.formatAsHoursMinutes()}"
                        }
                        is EditOneTimeNotificationViewModel.NotificationTime.Interval -> {
                            val interval = state.notificationTime
                            "${getString(R.string.afterInterval)} ${interval.hours.atLeastTwoDigits()} : ${interval.minutes.atLeastTwoDigits()}"
                        }
                        null -> getString(R.string.timeNotChosen)
                    }
                }
            }
        }
    }

    private fun showIntervalPicker() {
        val intervalPickerBinding = LayoutIntervalPickerBinding.inflate(LayoutInflater.from(requireContext()))

        configureNumberPicker(intervalPickerBinding.hourPicker, 0, 23)
        configureNumberPicker(intervalPickerBinding.minutePicker, 1, 59)

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.chooseInterval))
        builder.setView(intervalPickerBinding.root)
        builder.setPositiveButton(getString(R.string.ok)) { _, _ ->
            viewModel.setNotificationTime(
                EditOneTimeNotificationViewModel.NotificationTime.Interval(
                    hours = intervalPickerBinding.hourPicker.value,
                    minutes = intervalPickerBinding.minutePicker.value
                )
            )
        }
        builder.setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }

    private fun showDateTimePicker() {
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
                        selectedDateTime.set(year, monthOfYear, dayOfMonth, hourOfDay, minute, 0)
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