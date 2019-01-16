package ru.mail.technotrack.ipfs.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "document")
data class Document(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,
    val title: String,
    val path: String
)