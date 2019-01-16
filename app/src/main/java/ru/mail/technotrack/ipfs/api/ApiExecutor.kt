package ru.mail.technotrack.ipfs.api

import retrofit2.Call
import retrofit2.Response
import ru.mail.technotrack.ipfs.api.DTO.FileInfoList
import ru.mail.technotrack.ipfs.database.FileInfo

fun getFilesInfo(result: (FileInfoList, String) -> Unit, path: String = "/") {
    val service = RetrofitClient.create()
    service.getFilesInfo(path).enqueue(object: retrofit2.Callback<FileInfoList> {
        override fun onResponse(call: Call<FileInfoList>, response: Response<FileInfoList>) {
            response.body()?.let { result(it, path) }
        }

        override fun onFailure(call: Call<FileInfoList>, t: Throwable) {
        }

    })
}

fun downloadFile(result: (String, String) -> Unit, file: FileInfo) {
    val service = RetrofitClient.create()
    service.getFileContent(file.path + file.name).enqueue(object: retrofit2.Callback<String> {
        override fun onResponse(call: Call<String>, response: Response<String>) {
            response.body()?.let { result(it, file.name) }
        }

        override fun onFailure(call: Call<String>, t: Throwable) {
        }

    })
}

fun uploadFile(result: (String) -> Unit, file: ByteArray) {
    val service = RetrofitClient.create()

    service.uploadFileContent(file).enqueue(object: retrofit2.Callback<String> {
        override fun onResponse(call: Call<String>, response: Response<String>) {
            //response.body()?.let { result(it, file.name) }
        }

        override fun onFailure(call: Call<String>, t: Throwable) {
        }

    })
}