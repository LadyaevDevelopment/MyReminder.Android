package ldev.myNotifier.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ldev.myNotifier.presentation.fragments.all.AllViewModel
import ldev.myNotifier.presentation.fragments.settings.SettingsViewModel
import ldev.myNotifier.presentation.fragments.today.TodayViewModel

@Module
abstract class ViewModelModule {

    @Binds
    abstract fun viewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(AllViewModel::class)
    abstract fun allViewModel(viewModel: AllViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SettingsViewModel::class)
    abstract fun settingsViewModel(viewModel: SettingsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TodayViewModel::class)
    abstract fun todayViewModel(viewModel: TodayViewModel): ViewModel

}