package ldev.myNotifier.di

import dagger.Module
import dagger.Provides
import ldev.myNotifier.domain.repositories.NotificationMockRepository
import ldev.myNotifier.domain.repositories.NotificationRepository
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Singleton
    @Provides
    fun provideNotificationRepository(): NotificationRepository {
        return NotificationMockRepository()
    }

}