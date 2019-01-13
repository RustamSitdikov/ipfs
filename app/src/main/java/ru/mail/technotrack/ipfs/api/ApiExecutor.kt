package ru.mail.technotrack.ipfs.api

import android.util.Log
import retrofit2.Call
import retrofit2.Response
import ru.mail.technotrack.ipfs.api.DTO.FileInfoList

fun getFilesInfo(result: (FileInfoList, String) -> Unit, path: String = "/") {
    val service = RetrofitClient.create()
    service.getFilesInfo(path).enqueue(object: retrofit2.Callback<FileInfoList> {
        override fun onResponse(call: Call<FileInfoList>, response: Response<FileInfoList>) {
            Log.d("HMMM", "KEKKEKE")
            response.body()?.let { result(it, path) }
        }

        override fun onFailure(call: Call<FileInfoList>, t: Throwable) {
            Log.d("HMMM", "KEKKEKE")
        }

    })
}