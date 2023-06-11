package ldev.myNotifier.presentation.fragments.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import ldev.myNotifier.R
import ldev.myNotifier.databinding.FragmentDashboardBinding
import ldev.myNotifier.presentation.fragments.settings.SettingsFragment
import ldev.myNotifier.presentation.fragments.today.TodayFragment
import ldev.myNotifier.presentation.fragments.all.AllFragment
import ldev.myNotifier.utils.BaseFragment
import ldev.myNotifier.utils.reduceDragSensitivity
import ldev.myNotifier.utils.reversed
import ldev.myNotifier.utils.smoothScrollTo

class DashboardFragment : BaseFragment<FragmentDashboardBinding>() {

    private val menuItems = arrayOf(
        R.id.action_today,
        R.id.action_all,
        R.id.action_settings
    ).mapIndexed{ index, item -> Pair(index, item) }.associate { it }

    private val menuItemsFragments = arrayOf(
        { TodayFragment() },
        { AllFragment() },
        { SettingsFragment() }
    ).mapIndexed{ index, item -> Pair(index, item) }.associate { it }

    override fun getContentInflater(): (LayoutInflater, ViewGroup?, Boolean) -> FragmentDashboardBinding {
        return FragmentDashboardBinding::inflate
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewPager.adapter = object : FragmentStateAdapter(this@DashboardFragment) {
            override fun getItemCount() = menuItems.size
            override fun createFragment(position: Int): Fragment {
                return menuItemsFragments[position]!!()
            }
        }
        binding.viewPager.isSaveEnabled = false
        binding.viewPager.apply {
            (getChildAt(0) as RecyclerView).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        }
        binding.viewPager.reduceDragSensitivity(4)

        binding.bottomNavigation.setOnItemSelectedListener { menuItem ->
            val newPosition = menuItems.reversed()[menuItem.itemId]!!
            binding.viewPager.smoothScrollTo(newPosition)
            true
        }

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                binding.bottomNavigation.selectedItemId = menuItems[position]!!
            }
        })
    }

}