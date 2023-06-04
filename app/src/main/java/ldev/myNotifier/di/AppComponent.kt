package ldev.myNotifier.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import ldev.myNotifier.presentation.MainActivity
import ldev.myNotifier.presentation.fragments.all.AllFragment
import ldev.myNotifier.presentation.fragments.settings.SettingsFragment
import ldev.myNotifier.presentation.fragments.today.TodayFragment
import javax.inject.Singleton

@Singleton
@Component(modules = [RepositoryModule::class, ViewModelModule::class])
interface AppComponent {

    fun inject(activity: MainActivity)
    fun inject(fragment: TodayFragment)
    fun inject(fragment: AllFragment)
    fun inject(fragment: SettingsFragment)

    @Component.Builder
    interface Builder {

        fun build(): AppComponent

        @BindsInstance
        fun withContext(context: Context): Builder

    }

}