package ru.mail.technotrack.ipfs.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.webkit.MimeTypeMap
import android.widget.EditText
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import kotlinx.android.synthetic.main.activity_scrolling.*
import ru.mail.technotrack.ipfs.R
import ru.mail.technotrack.ipfs.api.DTO.FileInfo
import ru.mail.technotrack.ipfs.utils.getTypeFile
import java.io.File
import java.io.FileOutputStream

class ScrollingActivity : AppCompatActivity() {

    private val REQUEST_WRITE_EXTERNAL_FOR_STORAGE  = 1

    private lateinit var name: EditText
    private lateinit var type: EditText

    lateinit var ipfsFolderLocation: String
    private val ipfsFolderName: String = "ipfs"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val item = intent.extras.getSerializable("item") as FileInfo
        setContentView(R.layout.activity_scrolling)

        name = findViewById(R.id.name)
        name.setText(item.name)

        type = findViewById(R.id.type)
        type.setText(item.type?.let { getTypeFile(it) })

        title = item.name

        setSupportActionBar(toolbar)
        fab_share.setOnClickListener { view ->
            Snackbar.make(view, "Action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        fab_open.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
                != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    REQUEST_WRITE_EXTERNAL_FOR_STORAGE
                )
            } else {
                dispatchWriteExternalStorageIntent()
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_WRITE_EXTERNAL_FOR_STORAGE && grantResults[0] == PermissionChecker.PERMISSION_GRANTED) {
            dispatchWriteExternalStorageIntent()
        }
    }

    private fun dispatchWriteExternalStorageIntent() {
        val fileLocation = downloadFile(name.toString())

        val intent = Intent()
        intent.action = android.content.Intent.ACTION_VIEW
        val file = File(fileLocation)

        val mime = MimeTypeMap.getSingleton()
        val extension = file.name.substring(file.name.indexOf(".") + 1)
        val type = mime.getMimeTypeFromExtension(extension)

        intent.setDataAndType(Uri.fromFile(file), type)

        val chooserIntent = Intent.createChooser(intent, "Choose an application to open with:")

        this.startActivity(chooserIntent)
    }

    private fun createIPFSFolder() {
        val folder = File(Environment.getExternalStorageDirectory().absolutePath, ipfsFolderName)
        ipfsFolderLocation = folder.absolutePath
        if (!folder.exists()) {
            folder.mkdirs()
        }
    }

    private fun downloadFile(fileName: String): String {
//        TODO: get byteArray from ipfs
        var fileByteArray = ByteArray(1)
        return saveDownloadedFile(fileName, fileByteArray)
    }

    private fun saveDownloadedFile(fileName: String, fileByteArray: ByteArray): String {
        createIPFSFolder()
        val fileLocation = "$ipfsFolderLocation/$fileName"
        var file = File(fileLocation)
        if (file.exists())
            file.delete()
        try {
            val out = FileOutputStream(file)
            out.write(fileByteArray)
            out.flush()
            out.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return fileLocation
    }
}
