package ru.mail.technotrack.ipfs.services

import android.app.IntentService
import android.content.Intent
import android.app.Activity
import android.os.Environment
import android.util.Log
import retrofit2.Call
import retrofit2.Response
import ru.mail.technotrack.ipfs.api.RetrofitClient
import ru.mail.technotrack.ipfs.utils.*
import java.io.*


class DownloadIntentService : IntentService("DownloadIntentService") {

    private var result = Activity.RESULT_CANCELED
    lateinit var ipfsFolderLocation: String

    override fun onHandleIntent(intent: Intent?) {
        createIPFSFolder()
        val fileName = intent?.getStringExtra(FILENAME)

        val retrofitClientApi = RetrofitClient.create()
        val call = retrofitClientApi.getFileContent("/$fileName")
        call.enqueue(object : retrofit2.Callback<String> {

            override fun onResponse(call: Call<String>, response: Response<String>) {
                Log.d("HMMMMM", "HMMMMMM")
                if (fileName != null) {
                    saveFileIntoExternalStorage(fileName, response.body()!!)
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.d("ERROR", "Download file $fileName error")
            }
        })
    }

    private fun saveFileIntoExternalStorage(fileName: String, responseBody: String) {
        val fileBytes = responseBody
        val output = File(
            ipfsFolderLocation,
            fileName
        )
        if (output.exists()) {
            output.delete()
        }

        try {
            var inputStream = ByteArrayInputStream(fileBytes.toByteArray())
            var fileOutputStream = FileOutputStream(output.path)
            StreamCopier.copyStream(inputStream, fileOutputStream)
        } catch (ex: Exception) {

        }
        result = Activity.RESULT_OK
        publishResults(output.absolutePath, result)
    }

    private fun publishResults(outputPath: String, result: Int) {
        val intent = Intent(NOTIFICATION)
        intent.putExtra(FILEPATH, outputPath)
        intent.putExtra(RESULT, result)
        sendBroadcast(intent)
    }

    private fun createIPFSFolder() {
        val folder = File(Environment.getExternalStorageDirectory().absolutePath, IPFSFOLDERNAME)
        ipfsFolderLocation = folder.absolutePath
        if (!folder.exists()) {
            folder.mkdirs()
        }
    }

}
