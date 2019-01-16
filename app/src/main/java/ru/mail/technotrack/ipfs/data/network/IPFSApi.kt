package ru.mail.technotrack.ipfs.data.network

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query
import ru.mail.technotrack.ipfs.model.Document
import ru.mail.technotrack.ipfs.model.Info


interface IPFSApi {

    @GET("id")
    fun getInfo(): Observable<Info>

    @GET("files/ls")
    fun getDocuments(@Query("arg") arg: String="/"): Observable<MutableList<Document>>

    @GET("files/read")
    fun getDocument(@Query("arg") arg: String="/"): Observable<Document>

    @GET("files/write")
    fun setDocument(@Query("arg") arg: String="/"): Observable<Document>
}