package ldev.myNotifier.di

import dagger.Module
import dagger.Provides
import ldev.myNotifier.debugSettings.DataProvider
import ldev.myNotifier.debugSettings.DebugDataProvider
import ldev.myNotifier.debugSettings.DebugSettings
import ldev.myNotifier.debugSettings.EditPeriodicNotificationSettings
import ldev.myNotifier.domain.entities.PeriodicNotification
import javax.inject.Singleton
import ldev.myNotifier.BuildConfig
import ldev.myNotifier.debugSettings.ReleaseDataProvider

@Module
class DebugModule {
    @Singleton
    @Provides
    fun provideDataProvider() : DataProvider {
        return if (BuildConfig.DEBUG) DebugDataProvider() else ReleaseDataProvider()
    }

    @Singleton
    @Provides
    fun provideDebugSettings(dataProvider: DataProvider) : DebugSettings {
        return DebugSettings(
            editPeriodicNotificationSettings = EditPeriodicNotificationSettings(
                notification = dataProvider.provide(PeriodicNotification::class.java)
            )
        )
    }
}