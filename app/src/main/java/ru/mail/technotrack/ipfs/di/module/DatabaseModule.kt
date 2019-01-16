package ru.mail.technotrack.ipfs.di.module

import android.app.Application
import android.content.Context
import dagger.Provides
import javax.inject.Singleton
import androidx.room.Room
import dagger.Module
import ru.mail.technotrack.ipfs.data.dao.IPFSDao
import ru.mail.technotrack.ipfs.data.database.IPFSDatabase
import ru.mail.technotrack.ipfs.data.network.IPFSApi
import ru.mail.technotrack.ipfs.data.repository.IPFSRepository


@Module
class DatabaseModule {

    @Singleton
    @Provides
    internal fun providesIPFSDatabase(context: Context): IPFSDatabase {
        return Room.databaseBuilder(context.applicationContext, IPFSDatabase::class.java, "ipfs.db").build()

    }

    @Singleton
    @Provides
    internal fun providesIPFSDao(database: IPFSDatabase): IPFSDao {
        return database.ipfsDao()
    }

    @Singleton
    @Provides
    internal fun providesIPFSRepository(api: IPFSApi, dao: IPFSDao): IPFSRepository {
        return IPFSRepository(api, dao)
    }

}