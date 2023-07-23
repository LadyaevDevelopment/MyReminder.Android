package ldev.myNotifier.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import ldev.myNotifier.presentation.MainActivity
import ldev.myNotifier.presentation.fragments.all.AllFragment
import ldev.myNotifier.presentation.fragments.editOneTimeNotification.EditOneTimeNotificationFragment
import ldev.myNotifier.presentation.fragments.editPeriodicNotification.EditPeriodicNotificationFragment
import ldev.myNotifier.presentation.fragments.settings.SettingsFragment
import ldev.myNotifier.presentation.fragments.today.TodayFragment
import javax.inject.Singleton

@Singleton
@Component(modules = [RepositoryModule::class, ViewModelModule::class, DebugModule::class, RoomModule::class])
interface AppComponent {

    fun inject(activity: MainActivity)
    fun inject(fragment: TodayFragment)
    fun inject(fragment: AllFragment)
    fun inject(fragment: SettingsFragment)
    fun inject(fragment: EditOneTimeNotificationFragment)
    fun inject(fragment: EditPeriodicNotificationFragment)

    @Component.Builder
    interface Builder {
        fun build(): AppComponent
        @BindsInstance
        fun withContext(context: Context): Builder
    }

}