package ru.mail.technotrack.ipfs.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.mail.technotrack.ipfs.data.dao.IPFSDao
import ru.mail.technotrack.ipfs.model.Document

@Database(entities = arrayOf(Document::class), version = 1)
abstract class IPFSDatabase : RoomDatabase() {
    abstract fun ipfsDao(): IPFSDao
}