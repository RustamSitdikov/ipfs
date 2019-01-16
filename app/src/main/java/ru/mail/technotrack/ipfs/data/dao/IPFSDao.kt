package ru.mail.technotrack.ipfs.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import io.reactivex.Observable
import ru.mail.technotrack.ipfs.model.Document


@Dao
interface IPFSDao {
    @Insert(onConflict = REPLACE)
    fun insertDocument(document: Document)

    @Insert(onConflict = REPLACE)
    fun insertDocuments(documents: MutableList<Document>)

    @Query("SELECT * FROM document WHERE id = :documentId")
    fun selectDocument(documentId: String): Observable<Document>

    @Query("SELECT * FROM document")
    fun selectDocuments(): Observable<MutableList<Document>>
}