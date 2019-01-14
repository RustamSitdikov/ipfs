package ru.mail.technotrack.ipfs.di.module

import dagger.Module
import dagger.Provides
import io.ipfs.kotlin.IPFS
import io.ipfs.kotlin.IPFSConfiguration
import javax.inject.Singleton


@Module
class IPFSModule {

    @Provides
    @Singleton
    internal fun provideIPFS(): IPFS {
        return IPFS(IPFSConfiguration())
    }
}