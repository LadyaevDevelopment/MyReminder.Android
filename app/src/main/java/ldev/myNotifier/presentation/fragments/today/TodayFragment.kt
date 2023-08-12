package ldev.myNotifier.presentation.fragments.today

import android.app.Dialog
import android.graphics.Color
import android.graphics.RenderEffect
import android.graphics.Shader
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import ldev.myNotifier.NavGraphDirections
import ldev.myNotifier.R
import ldev.myNotifier.databinding.ChooseDayDialogBinding
import ldev.myNotifier.databinding.FragmentTodayBinding
import ldev.myNotifier.presentation.appComponent
import ldev.myNotifier.presentation.fragments.editOneTimeNotification.toUiModel
import ldev.myNotifier.presentation.fragments.editPeriodicNotification.toUiModel
import ldev.myNotifier.utils.BaseFragment
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

    private val notificationFingerPrint = NotificationFingerprint { notification ->
        viewModel.tapNotification(notification)
    }
    private val notificationAdapter: FingerprintAdapter = FingerprintAdapter(
        listOf(notificationFingerPrint)
    )

    override fun getContentInflater(): (LayoutInflater, ViewGroup?, Boolean) -> FragmentTodayBinding {
        return FragmentTodayBinding::inflate
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)

        viewModel = ViewModelProvider(this, viewModelFactory)[TodayViewModel::class.java]

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                subscribeToViewModel()
            }
        }
    }

    private fun subscribeToViewModel() {
        viewModel.state.observe(viewLifecycleOwner) { state ->
            lifecycleScope.launch {
                lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    notificationAdapter.submitList(state.notifications)
                }
            }
        }
        viewModel.action.observe(viewLifecycleOwner) { action ->
            lifecycleScope.launch {
                lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    action.getIfNotConsumed()?.let { action ->
                        when (action) {
                            TodayViewModel.UiAction.GetNotifications -> {
                                viewModel.getNotifications()
                            }
                            is TodayViewModel.UiAction.GoToEditOneTimeNotification -> {
                                findNavController().navigate(
                                    NavGraphDirections.actionGlobalToEditOneTimeNotificationFragment(
                                        action.notification?.toUiModel()
                                    )
                                )
                            }
                            is TodayViewModel.UiAction.GoToEditPeriodicNotification -> {
                                findNavController().navigate(
                                    NavGraphDirections.actionGlobalToEditPeriodicNotificationFragment(
                                        action.notification?.toUiModel()
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.today.text = Date().formatAsFullDayFullMonthFullYear()
        binding.addNotificationBtn.setOnClickListener {
            showNewNotificationDialog()
        }
        with(binding.rvNotifications) {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false).apply {
                isSmoothScrollbarEnabled = false
            }
            adapter = notificationAdapter
            itemAnimator = null
            addItemDecoration(
                VerticalItemDecorator(
                    innerDivider = dpToPixels(4),
                    outerDivider = dpToPixels(4)
                )
            )
        }

        binding.title.setOnClickListener {
            val customDialog = Dialog(requireContext())
            val contentViewBinding = ChooseDayDialogBinding.inflate(LayoutInflater.from(requireContext()), null, false)
            customDialog.setContentView(contentViewBinding.root)
            contentViewBinding.card.apply {
                layoutParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT).apply {
                    leftMargin = requireContext().dpToPixels(16)
                    rightMargin = requireContext().dpToPixels(16)
                }
            }
            customDialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            //customDialog.window?.attributes?.windowAnimations = R.style.animation;
            customDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                customDialog.setOnDismissListener {
                    requireActivity().window!!.decorView.setRenderEffect(null)
                }
                requireActivity().window!!.decorView.setRenderEffect(
                    RenderEffect.createBlurEffect(
                        30f,
                        30f,
                        Shader.TileMode.CLAMP
                    ))
            }

            customDialog.show()
        }
    }

    private fun showNewNotificationDialog() {
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setTitle(getString(R.string.createNotification_title))
        alertDialogBuilder.setMessage("${getString(R.string.createNotification_subtitle)}:")

        alertDialogBuilder.setPositiveButton(getString(R.string.createNotification_oneTime)) { _, _ ->
            viewModel.tapCreateOneTimeNotificationBtn()
        }

        alertDialogBuilder.setNegativeButton(getString(R.string.createNotification_periodic)) { _, _ ->
            viewModel.tapCreatePeriodicNotificationBtn()
        }

        alertDialogBuilder.create().show()
    }

    override fun onDestroyView() {
        binding.rvNotifications.adapter = null
        super.onDestroyView()
    }

}