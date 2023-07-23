package ldev.myNotifier.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import ldev.myNotifier.data.mock.NotificationMockRepository
import ldev.myNotifier.data.room.NotificationLocalRepository
import ldev.myNotifier.domain.repositories.NotificationRepository
import javax.inject.Singleton

@Module
interface RepositoryModule {

    @Singleton
    @Binds
    fun bindNotificationRepository(repository: NotificationLocalRepository): NotificationRepository

}