package ldev.myNotifier.di

import dagger.Binds
import dagger.Module
import ldev.myNotifier.core.AlarmProxy
import ldev.myNotifier.core.LogcatProxy
import ldev.myNotifier.framework.AlarmProxyImpl
import ldev.myNotifier.framework.LogcatProxyImpl
import javax.inject.Singleton

@Module
interface FrameworkModule {
    @Singleton
    @Binds
    fun alarmProxy(proxy: AlarmProxyImpl) : AlarmProxy

    @Singleton
    @Binds
    fun logcatProxy(proxy: LogcatProxyImpl) : LogcatProxy
}