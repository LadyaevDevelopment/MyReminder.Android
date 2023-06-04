package ldev.myNotifier.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component

@Component(modules = [RepositoryModule::class])
interface AppComponent {

    @Component.Builder
    interface Builder {

        fun build(): AppComponent

        @BindsInstance
        fun withContext(context: Context): Builder

    }

}