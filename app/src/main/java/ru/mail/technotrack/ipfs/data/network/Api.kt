package ru.mail.technotrack.ipfs.data.network

import io.reactivex.Observable
import retrofit2.http.GET
import ru.mail.technotrack.ipfs.data.Version


interface Api {

    @GET("repo/version")
    fun getVersion(): Observable<Version>
}