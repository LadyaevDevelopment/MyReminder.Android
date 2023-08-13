package ldev.myNotifier.di

import android.app.Application
import android.content.Context
import dagger.BindsInstance
import dagger.Component
import ldev.myNotifier.framework.BootCompletedBroadcastReceiver
import ldev.myNotifier.framework.NotificationBroadcastReceiver
import ldev.myNotifier.presentation.MainActivity
import ldev.myNotifier.presentation.fragments.all.AllFragment
import ldev.myNotifier.presentation.fragments.editOneTimeNotification.EditOneTimeNotificationFragment
import ldev.myNotifier.presentation.fragments.editPeriodicNotification.EditPeriodicNotificationFragment
import ldev.myNotifier.presentation.fragments.settings.SettingsFragment
import ldev.myNotifier.presentation.fragments.today.TodayFragment
import javax.inject.Singleton

@Singleton
@Component(modules = [RepositoryModule::class, ViewModelModule::class, DebugModule::class, RoomModule::class, FrameworkModule::class])
interface AppComponent {

    fun inject(activity: MainActivity)
    fun inject(receiver: NotificationBroadcastReceiver)
    fun inject(receiver: BootCompletedBroadcastReceiver)
    fun inject(fragment: TodayFragment)
    fun inject(fragment: AllFragment)
    fun inject(fragment: SettingsFragment)
    fun inject(fragment: EditOneTimeNotificationFragment)
    fun inject(fragment: EditPeriodicNotificationFragment)

    @Component.Builder
    interface Builder {
        fun build(): AppComponent
        @BindsInstance
        fun withApplication(app: Application): Builder
        @BindsInstance
        fun withContext(context: Context): Builder
    }

}