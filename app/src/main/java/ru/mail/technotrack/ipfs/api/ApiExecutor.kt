package ru.mail.technotrack.ipfs.api

import retrofit2.Call
import retrofit2.Response
import ru.mail.technotrack.ipfs.api.DTO.FileInfoList

fun getFilesInfo(result: (FileInfoList, String) -> Unit, path: String = "/") {
    val service = RetrofitClient.create();
    service.getFilesInfo(path).enqueue(object: retrofit2.Callback<FileInfoList> {
        override fun onResponse(call: Call<FileInfoList>, response: Response<FileInfoList>) {
            response.body()?.let { result(it, path) }
        }

        override fun onFailure(call: Call<FileInfoList>, t: Throwable) {

        }

    })
}