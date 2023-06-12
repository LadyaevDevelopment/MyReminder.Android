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
import ldev.myNotifier.databinding.FragmentEditOneTimeNotificationBinding
import ldev.myNotifier.databinding.LayoutIntervalPickerBinding
import ldev.myNotifier.utils.BaseFragment
import ldev.myNotifier.utils.atLeastTwoDigits
import ldev.myNotifier.utils.formatAsFullDayFullMonthFullYear
import ldev.myNotifier.utils.formatAsHoursMinutes
import java.util.Calendar

class EditOneTimeNotificationFragment : BaseFragment<FragmentEditOneTimeNotificationBinding>() {

    override fun getContentInflater(): (LayoutInflater, ViewGroup?, Boolean) -> FragmentEditOneTimeNotificationBinding {
        return FragmentEditOneTimeNotificationBinding::inflate
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.afterBtn.setOnClickListener {
            val intervalPickerBinding = LayoutIntervalPickerBinding.inflate(LayoutInflater.from(requireContext()))

            configureNumberPicker(intervalPickerBinding.hourPicker, 0, 23)
            configureNumberPicker(intervalPickerBinding.minutePicker, 1, 59)

            val builder = AlertDialog.Builder(context)
            builder.setTitle("Выберите интервал")
            builder.setView(intervalPickerBinding.root)
            builder.setPositiveButton("OK") { dialog, which ->
                val hours = intervalPickerBinding.hourPicker.value
                val minutes = intervalPickerBinding.minutePicker.value
                binding.interval.text = run { "Через ${hours.atLeastTwoDigits()} : ${minutes.atLeastTwoDigits()}" }
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
                            binding.interval.text = run {
                                "${selectedDateTime.time.formatAsFullDayFullMonthFullYear()}, ${selectedDateTime.time.formatAsHoursMinutes()}"
                            }
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

        picker.minValue = minValue
        picker.maxValue = maxValue
        picker.wrapSelectorWheel = false
        picker.setOnValueChangedListener { _, _, _ ->
            handler.post(vibrationRunnable)
        }
    }

}