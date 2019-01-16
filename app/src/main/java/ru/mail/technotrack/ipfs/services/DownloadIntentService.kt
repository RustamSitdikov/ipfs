package ru.mail.technotrack.ipfs.services

import android.app.IntentService
import android.content.Intent
import android.app.Activity
import android.os.Environment
import ru.mail.technotrack.ipfs.api.downloadFile
import ru.mail.technotrack.ipfs.database.FileInfo
import ru.mail.technotrack.ipfs.utils.*
import java.io.*


class DownloadIntentService : IntentService("DownloadIntentService") {

    private var result = Activity.RESULT_CANCELED
    lateinit var ipfsFolderLocation: String

    override fun onHandleIntent(intent: Intent?) {
        createIPFSFolder()
        val file = intent?.getSerializableExtra(ITEM_FILE) as FileInfo
        downloadFile({ it, name ->
            run {
                saveFileIntoExternalStorage(it, name)
            }
        }, file)
    }

    private fun saveFileIntoExternalStorage(responseBody: String, fileName: String) {
        val fileBytes = responseBody
        val output = File(
            ipfsFolderLocation,
            fileName
        )
        if (output.exists()) {
            output.delete()
        }

        try {
            val inputStream = ByteArrayInputStream(fileBytes.toByteArray())
            val fileOutputStream = FileOutputStream(output.path)
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
