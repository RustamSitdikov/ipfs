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
        call.enqueue(object : retrofit2.Callback<ByteArray> {

            override fun onResponse(call: Call<ByteArray>, response: Response<ByteArray>) {
                Log.d("HMMMMM", "HMMMMMM")
                if (fileName != null) {
                    saveFileIntoExternalStorage(fileName, response.body()!!)
                }
            }

            override fun onFailure(call: Call<ByteArray>, t: Throwable) {
                Log.d("ERROR", "Download file $fileName error")
            }
        })
    }

    private fun saveFileIntoExternalStorage(fileName: String, responseBody: ByteArray) {
        val fileBytes = responseBody
        val output = File(
            ipfsFolderLocation,
            fileName
        )
        if (output.exists()) {
            output.delete()
        }

        var inputStream: InputStream? = null
        var fileOutputStream: FileOutputStream? = null
        try {
//            inputStream = ByteArrayInputStream(fileBytes!!.toByteArray())
            inputStream = ByteArrayInputStream(fileBytes!!)
            val reader = InputStreamReader(inputStream)
            fileOutputStream = FileOutputStream(output.path)
            var next = -1
            while (next != -1) {
                next = reader.read()
                fileOutputStream!!.write(next)
            }
            // successfully finished
            result = Activity.RESULT_OK

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (inputStream != null) {
                try {
                    inputStream!!.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
            if (fileOutputStream != null) {
                try {
                    fileOutputStream!!.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
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
