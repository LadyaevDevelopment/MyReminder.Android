package ldev.myNotifier

import android.app.Application
import ldev.myNotifier.di.AppComponent
import ldev.myNotifier.di.DaggerAppComponent

class MainApplication: Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder()
            .withApplication(this)
            .withContext(this)
            .build()
    }

}