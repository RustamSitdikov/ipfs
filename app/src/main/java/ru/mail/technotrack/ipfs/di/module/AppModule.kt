package ru.mail.technotrack.ipfs.di.module

import dagger.Module
import dagger.Provides
import android.app.Application
import android.content.Context


@Module
class AppModule(val application: Application) {

    @Provides
    internal fun provideContext(): Context {
        return application.applicationContext
    }
}