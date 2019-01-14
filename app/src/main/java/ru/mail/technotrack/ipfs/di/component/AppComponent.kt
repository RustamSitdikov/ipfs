package ru.mail.technotrack.ipfs.di.component

import android.app.Application
import dagger.Component
import ru.mail.technotrack.ipfs.data.service.IPFSService
import ru.mail.technotrack.ipfs.di.module.AppModule
import ru.mail.technotrack.ipfs.di.module.IPFSModule
import ru.mail.technotrack.ipfs.di.module.NetworkModule
import ru.mail.technotrack.ipfs.ui.activity.MainActivity
import ru.mail.technotrack.ipfs.ui.activity.SplashActivity
import ru.mail.technotrack.ipfs.ui.fragment.DashboardFragment
import javax.inject.Singleton


@Singleton
@Component(modules = [ AppModule::class, NetworkModule::class, IPFSModule::class ])
interface AppComponent {
    fun inject(application: Application)
    fun inject(mainActivity: MainActivity)
    fun inject(splashActivity: SplashActivity)
    fun inject(dashboardFragment: DashboardFragment)
}