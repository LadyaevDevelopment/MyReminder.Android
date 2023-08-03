package ldev.myNotifier.di

import dagger.Module
import dagger.Provides
import ldev.myNotifier.debugSettings.DataProvider
import ldev.myNotifier.debugSettings.DebugDataProvider
import ldev.myNotifier.debugSettings.DebugSettings
import ldev.myNotifier.debugSettings.EditPeriodicNotificationSettings
import javax.inject.Singleton
import ldev.myNotifier.BuildConfig
import ldev.myNotifier.debugSettings.EditOneTimeNotificationSettings
import ldev.myNotifier.debugSettings.ReleaseDataProvider
import ldev.myNotifier.domain.entities.OneTimeNotification
import ldev.myNotifier.domain.entities.PeriodicNotificationWithRules

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
                notificationWithRules = dataProvider.provide(PeriodicNotificationWithRules::class.java)
            ),
            editOneTimeNotificationSettings = EditOneTimeNotificationSettings(
                notification = dataProvider.provide(OneTimeNotification::class.java)
            )
        )
    }
}