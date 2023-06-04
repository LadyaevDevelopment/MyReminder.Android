package ldev.myNotifier.presentation.fragments.today

import android.view.LayoutInflater
import android.view.ViewGroup
import ldev.myNotifier.databinding.FragmentTodayBinding
import ldev.myNotifier.utils.BaseFragment

class TodayFragment : BaseFragment<FragmentTodayBinding>() {

    override fun getContentInflater(): (LayoutInflater, ViewGroup?, Boolean) -> FragmentTodayBinding {
        return FragmentTodayBinding::inflate
    }

}