package ldev.myNotifier.di

import dagger.Binds
import dagger.Module
import ldev.myNotifier.data.room.CustomLogLocalRepository
import ldev.myNotifier.data.room.NotificationLocalRepository
import ldev.myNotifier.domain.repositories.CustomLogRepository
import ldev.myNotifier.domain.repositories.NotificationRepository
import javax.inject.Singleton

@Module
interface RepositoryModule {

    @Singleton
    @Binds
    fun bindNotificationRepository(repository: NotificationLocalRepository): NotificationRepository

    @Singleton
    @Binds
    fun bindCustomLogRepository(repository: CustomLogLocalRepository): CustomLogRepository

}