package ru.mail.technotrack.ipfs

import android.app.Application
import ru.mail.technotrack.ipfs.di.component.AppComponent
import ru.mail.technotrack.ipfs.di.component.DaggerAppComponent


open class App: Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.builder().build()
        appComponent.inject(this)
    }
}