package ru.mail.technotrack.ipfs.di.module

import dagger.Module
import dagger.Provides
import javax.inject.Singleton
import android.app.Application
import android.content.Context


@Module
class AppModule(val application: Application) {

    @Provides
    @Singleton
    internal fun provideApplication(): Application {
        return application
    }

    @Provides
    internal fun provideContext(): Context {
        return application.applicationContext
    }
}