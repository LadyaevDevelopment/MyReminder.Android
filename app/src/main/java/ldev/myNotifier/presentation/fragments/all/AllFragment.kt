package ldev.myNotifier.presentation.fragments.all

import android.view.LayoutInflater
import android.view.ViewGroup
import ldev.myNotifier.databinding.FragmentAllBinding
import ldev.myNotifier.utils.BaseFragment

class AllFragment : BaseFragment<FragmentAllBinding>() {

    override fun getContentInflater(): (LayoutInflater, ViewGroup?, Boolean) -> FragmentAllBinding {
        return FragmentAllBinding::inflate
    }

}