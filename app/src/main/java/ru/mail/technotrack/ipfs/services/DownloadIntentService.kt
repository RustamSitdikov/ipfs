package ru.mail.technotrack.ipfs.services

import android.app.IntentService
import android.content.Intent
import android.app.Activity
import android.os.Environment
import java.io.*
import java.net.URL


class DownloadIntentService : IntentService("DownloadIntentService") {

    private var result = Activity.RESULT_CANCELED
    val FILENAME = "filename"
    val FILEPATH = "filepath"
    val FILEBYTES = "filebytes"
    val RESULT = "result"
    val NOTIFICATION = "notification"

    private val IPFSFOLDERNAMAE = "ipfs"
    lateinit var ipfsFolderLocation: String

    override fun onHandleIntent(intent: Intent?) {
        createIPFSFolder()

        val fileName = intent?.getStringExtra(FILENAME)
        val fileBytes = intent?.getStringExtra(FILEBYTES)
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
            inputStream = ByteArrayInputStream(fileBytes!!.toByteArray())
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
        val folder = File(Environment.getExternalStorageDirectory().absolutePath, IPFSFOLDERNAMAE)
        ipfsFolderLocation = folder.absolutePath
        if (!folder.exists()) {
            folder.mkdirs()
        }
    }

}
