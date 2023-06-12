package ldev.myNotifier.presentation.fragments.today

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavGraph
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import ldev.myNotifier.NavGraphDirections
import ldev.myNotifier.databinding.FragmentTodayBinding
import ldev.myNotifier.presentation.appComponent
import ldev.myNotifier.utils.BaseFragment
import ldev.myNotifier.utils.HorizontalItemDecorator
import ldev.myNotifier.utils.VerticalItemDecorator
import ldev.myNotifier.utils.dpToPixels
import ldev.myNotifier.utils.formatAsFullDayFullMonthFullYear
import ldev.myNotifier.utils.recyclerView.FingerprintAdapter
import java.util.Date
import javax.inject.Inject

class TodayFragment : BaseFragment<FragmentTodayBinding>() {

    private lateinit var viewModel: TodayViewModel
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var _notificationAdapter: FingerprintAdapter? = FingerprintAdapter(listOf(NotificationFingerprint()))
    private val notificationAdapter = _notificationAdapter!!

    override fun getContentInflater(): (LayoutInflater, ViewGroup?, Boolean) -> FragmentTodayBinding {
        return FragmentTodayBinding::inflate
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)

        viewModel = ViewModelProvider(this, viewModelFactory)[TodayViewModel::class.java].apply {
            getNotifications()
        }
        lifecycleScope.launchWhenStarted {
            viewModel.state.observe(viewLifecycleOwner) {
                lifecycleScope.launchWhenResumed {
                    notificationAdapter.submitList(it.notifications)
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.today.text = Date().formatAsFullDayFullMonthFullYear()
        binding.addNotificationBtn.setOnClickListener {
            //findNavController().navigate(NavGraphDirections.actionGlobalToEditOneTimeNotificationFragment())
            findNavController().navigate(NavGraphDirections.actionGlobalToEditPeriodicNotificationFragment())
        }
        with(binding.rvNotifications) {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false).apply {
                isSmoothScrollbarEnabled = false
            }
            adapter = notificationAdapter
            addItemDecoration(
                VerticalItemDecorator(
                    innerDivider = dpToPixels(4),
                    outerDivider = dpToPixels(4)
                )
            )
            //addItemDecoration(HorizontalItemDecorator(dpToPixels(4)))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _notificationAdapter = null
    }

}