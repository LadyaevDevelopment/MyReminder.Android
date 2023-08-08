package ldev.myNotifier.presentation

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch
import ldev.myNotifier.MainApplication
import ldev.myNotifier.R
import ldev.myNotifier.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var viewModel: MainViewModel

    private val requestPostNotificationsPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
        ::onGotPostNotificationsPermissionResult
    )

    private fun onGotPostNotificationsPermissionResult(granted: Boolean) {
        if (!granted && !shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
            viewModel.postNotificationsPermissionDeniedForever()
        }
    }

    val appComponent by lazy {
        (application as MainApplication).appComponent
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                subscribeToViewModel()
            }
        }
    }

    private fun subscribeToViewModel() {
        viewModel.action.observe(this) { action ->
            lifecycleScope.launch {
                lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    action.getIfNotConsumed()?.let { action ->
                        when (action) {
                            MainViewModel.UiAction.AskPostNotificationsPermission -> {
                                askPostNotificationsPermission()
                            }
                            MainViewModel.UiAction.ShowNeedToOpenNotificationSettingsDialog -> {
                                showNeedToOpenNotificationSettingsDialog()
                            }
                            MainViewModel.UiAction.ShowPostNotificationsPermissionExplanationDialog -> {
                                showPostNotificationsPermissionExplanationDialog()
                            }
                            MainViewModel.UiAction.ShowCannotOpenNotificationSettingsToast -> {
                                showCannotOpenNotificationSettingsToast()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun showPostNotificationsPermissionExplanationDialog() {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle(R.string.attention)
        alertDialogBuilder.setMessage(R.string.needToGrandPostNotificationsPermission)
        alertDialogBuilder.setPositiveButton(R.string.ok) { _, _ ->

        }
        alertDialogBuilder.setOnDismissListener {
            viewModel.postNotificationsPermissionExplanationDialogDismissed()
        }
        alertDialogBuilder.create().show()
    }

    private fun showNeedToOpenNotificationSettingsDialog() {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle(R.string.attention)
        alertDialogBuilder.setMessage(R.string.needToOpenNotificationSettings)
        alertDialogBuilder.setPositiveButton(R.string.open) { _, _ ->
            openNotificationSettings()
        }
        alertDialogBuilder.create().show()
    }

    private fun showCannotOpenNotificationSettingsToast() {
        Toast.makeText(this, R.string.cannotOpenNotificationSettings, Toast.LENGTH_LONG).show()
    }

    private fun askPostNotificationsPermission() {
        if (Build.VERSION.SDK_INT >= 33) {
            requestPostNotificationsPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            openNotificationSettings()
        }
    }

    private fun openNotificationSettings() {
        val appSettingsIntent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", packageName, null)
        )
        val resolveActivity = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            packageManager.resolveActivity(
                appSettingsIntent,
                PackageManager.ResolveInfoFlags.of(PackageManager.MATCH_DEFAULT_ONLY.toLong())
            )
        } else {
            @Suppress("DEPRECATION")
            packageManager.resolveActivity(
                appSettingsIntent,
                PackageManager.MATCH_DEFAULT_ONLY
            )
        }
        if (resolveActivity == null) {
            viewModel.cannotOpenNotificationSettings()
        } else {
            startActivity(appSettingsIntent)
        }
    }
}

val Fragment.appComponent get() = (requireActivity() as MainActivity).appComponent