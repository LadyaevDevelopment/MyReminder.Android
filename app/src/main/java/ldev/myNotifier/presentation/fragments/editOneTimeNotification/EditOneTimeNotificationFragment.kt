package ldev.myNotifier.presentation.fragments.editOneTimeNotification

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ldev.myNotifier.R
import ldev.myNotifier.databinding.FragmentEditOneTimeNotificationBinding
import ldev.myNotifier.utils.BaseFragment

class EditOneTimeNotificationFragment : BaseFragment<FragmentEditOneTimeNotificationBinding>() {

    override fun getContentInflater(): (LayoutInflater, ViewGroup?, Boolean) -> FragmentEditOneTimeNotificationBinding {
        return FragmentEditOneTimeNotificationBinding::inflate
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.intervalPicker.minValue = 5
        binding.intervalPicker.maxValue = 24
        binding.intervalPicker.wrapSelectorWheel = false
    }

}