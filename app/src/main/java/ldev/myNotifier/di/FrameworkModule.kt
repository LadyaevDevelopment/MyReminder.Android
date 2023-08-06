package ldev.myNotifier.di

import dagger.Binds
import dagger.Module
import ldev.myNotifier.core.AlarmService
import ldev.myNotifier.framework.AlarmServiceImpl
import javax.inject.Singleton

@Module
interface FrameworkModule {
    @Singleton
    @Binds
    fun alarmService(service: AlarmServiceImpl) : AlarmService
}