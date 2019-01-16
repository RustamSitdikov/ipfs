package ru.mail.technotrack.ipfs.di.component

import android.app.Application
import dagger.Component
import ru.mail.technotrack.ipfs.data.dao.IPFSDao
import ru.mail.technotrack.ipfs.data.database.IPFSDatabase
import ru.mail.technotrack.ipfs.data.repository.IPFSRepository
import ru.mail.technotrack.ipfs.data.service.IPFSService
import ru.mail.technotrack.ipfs.di.module.*
import ru.mail.technotrack.ipfs.ui.activity.MainActivity
import ru.mail.technotrack.ipfs.ui.activity.SplashActivity
import ru.mail.technotrack.ipfs.ui.fragment.DashboardFragment
import ru.mail.technotrack.ipfs.ui.fragment.DocumentsFragment
import ru.mail.technotrack.ipfs.viewmodel.IPFSViewModel
import javax.inject.Singleton


@Singleton
@Component(modules = [ AppModule::class, NetworkModule::class, IPFSModule::class, DatabaseModule::class, ViewModelModule::class ])
interface AppComponent {
    fun inject(application: Application)
    fun inject(mainActivity: MainActivity)
    fun inject(splashActivity: SplashActivity)
    fun inject(dashboardFragment: DashboardFragment)
    fun inject(documentsFragment: DocumentsFragment)
    fun inject(ipfsViewModel: IPFSViewModel)
}