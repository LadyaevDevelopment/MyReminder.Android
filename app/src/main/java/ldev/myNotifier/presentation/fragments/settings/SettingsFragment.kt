package ldev.myNotifier.presentation.fragments.settings

import android.view.LayoutInflater
import android.view.ViewGroup
import ldev.myNotifier.databinding.FragmentSettingsBinding
import ldev.myNotifier.utils.BaseFragment

class SettingsFragment : BaseFragment<FragmentSettingsBinding>() {

    override fun getContentInflater(): (LayoutInflater, ViewGroup?, Boolean) -> FragmentSettingsBinding {
        return FragmentSettingsBinding::inflate
    }

}