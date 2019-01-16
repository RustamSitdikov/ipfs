package ru.mail.technotrack.ipfs.di.module

import dagger.Module
import dagger.Provides
import io.ipfs.api.IPFS
import javax.inject.Singleton


@Module
class IPFSModule {

    companion object {
        const val NODE_URL: String = "/ip4/127.0.0.1/tcp/5001"
    }

    @Provides
    @Singleton
    internal fun provideIPFS(): IPFS {
        return IPFS(NODE_URL)
    }
}