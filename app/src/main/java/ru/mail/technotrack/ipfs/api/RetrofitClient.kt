package ru.mail.technotrack.ipfs.api

import retrofit2.Call;
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.mail.technotrack.ipfs.api.DTO.FileInfo
import ru.mail.technotrack.ipfs.api.DTO.FileInfoList
import ru.mail.technotrack.ipfs.utils.BASE_API_URL

interface RetrofitClient {
    companion object Factory {
        fun create(): RetrofitClient{
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_API_URL)
                .build()

            return retrofit.create(RetrofitClient::class.java)
        }
    }

    @GET("/api/v0/files/ls")
    fun getFilesInfo(@Query("arg") filesPath: String="/", @Query("l") longListing: Boolean=true): Call<FileInfoList>

    @GET("")
    fun getFileInfo(@Query("arg") filesPath: String="/"): Call<FileInfo>
}