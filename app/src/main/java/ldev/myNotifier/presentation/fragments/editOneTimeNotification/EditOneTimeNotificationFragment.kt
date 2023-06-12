package ldev.myNotifier.presentation.fragments.editOneTimeNotification

import android.content.Context
import android.content.Context.VIBRATOR_SERVICE
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import ldev.myNotifier.R
import ldev.myNotifier.databinding.FragmentEditOneTimeNotificationBinding
import ldev.myNotifier.utils.BaseFragment

class EditOneTimeNotificationFragment : BaseFragment<FragmentEditOneTimeNotificationBinding>() {

    override fun getContentInflater(): (LayoutInflater, ViewGroup?, Boolean) -> FragmentEditOneTimeNotificationBinding {
        return FragmentEditOneTimeNotificationBinding::inflate
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

        binding.hourPicker.minValue = 0
        binding.hourPicker.maxValue = 23
        binding.hourPicker.wrapSelectorWheel = false

        binding.hourPicker.setOnValueChangedListener { _, _, _ ->
            handler.post(vibrationRunnable)
        }

        binding.minutePicker.minValue = 1
        binding.minutePicker.maxValue = 59
        binding.minutePicker.wrapSelectorWheel = false
    }

}