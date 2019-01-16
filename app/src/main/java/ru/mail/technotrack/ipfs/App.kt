package ru.mail.technotrack.ipfs

import android.app.Application
import ru.mail.technotrack.ipfs.di.component.AppComponent
import ru.mail.technotrack.ipfs.di.component.DaggerAppComponent
import ru.mail.technotrack.ipfs.di.module.AppModule
import ru.mail.technotrack.ipfs.di.module.DatabaseModule
import ru.mail.technotrack.ipfs.di.module.IPFSModule
import ru.mail.technotrack.ipfs.di.module.NetworkModule
import ru.mail.technotrack.ipfs.viewmodel.IPFSViewModel


open class App: Application() {

    internal lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .networkModule(NetworkModule())
            .iPFSModule(IPFSModule())
            .databaseModule(DatabaseModule())
            .build()
        appComponent.inject(this)
    }

    fun getAppComponent(): AppComponent {
        return appComponent
    }
}