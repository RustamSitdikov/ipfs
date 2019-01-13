package ru.mail.technotrack.ipfs.activities

import android.Manifest
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.webkit.MimeTypeMap
import android.widget.EditText
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import kotlinx.android.synthetic.main.activity_scrolling.*
import ru.mail.technotrack.ipfs.R
import ru.mail.technotrack.ipfs.utils.getTypeFile
import java.io.File
import retrofit2.Call
import retrofit2.Response
import ru.mail.technotrack.ipfs.api.RetrofitClient
import ru.mail.technotrack.ipfs.database.FileInfo
import android.content.IntentFilter
import android.util.Log
import ru.mail.technotrack.ipfs.api.DTO.FileInfoList
import ru.mail.technotrack.ipfs.services.DownloadIntentService


class ScrollingActivity : AppCompatActivity() {

    private val REQUEST_WRITE_EXTERNAL_FOR_STORAGE = 1

    private lateinit var name: EditText
    private lateinit var type: EditText

    lateinit var ipfsFolderLocation: String
    private val ipfsFolderName: String = "ipfs"

    val FILENAME = "filename"
    val FILEPATH = "filepath"
    val FILEBYTES = "filebytes"
    val RESULT = "result"
    val NOTIFICATION = "notification"

    private val receiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            val bundle = intent.extras
            if (bundle != null) {
                val filePath = bundle.getString(FILEPATH)
                val resultCode = bundle.getInt(RESULT)
                if (resultCode == Activity.RESULT_OK) {
                    openFile(filePath)
                } else {
                    showError()
                }
            }
        }
    }

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
                downloadFile(name.toString())
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_WRITE_EXTERNAL_FOR_STORAGE && grantResults[0] == PermissionChecker.PERMISSION_GRANTED) {
            downloadFile(name.toString())
        }
    }

    private fun openFile(filePath: String) {
        val intent = Intent()
        intent.action = android.content.Intent.ACTION_VIEW
        val file = File(filePath)

        val mime = MimeTypeMap.getSingleton()
        val extension = file.name.substring(file.name.indexOf(".") + 1)
        val type = mime.getMimeTypeFromExtension(extension)

        intent.setDataAndType(Uri.fromFile(file), type)

        val chooserIntent = Intent.createChooser(intent, "Choose an application to open with:")

        this.startActivity(chooserIntent)
    }

    private fun showError() {
        Log.d("ERROR", "Can't open file")
    }

    private fun downloadFile(fileName: String) {

        val retrofitClientApi = RetrofitClient.create()
        val call = retrofitClientApi.getFileContent(fileName)
        call.enqueue(object : retrofit2.Callback<String> {

            override fun onResponse(call: Call<String>, response: Response<String>) {
                val intent = Intent(this@ScrollingActivity, DownloadIntentService::class.java)
                // add infos for the service which file to download and where to store
                intent.putExtra(FILENAME, name.toString())
                intent.putExtra(FILEBYTES, response.body())
                startService(intent)
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.d("ERROR", "Dowload file $fileName error")
            }
        })
    }

    override fun onResume() {
        super.onResume()
        registerReceiver(
            receiver, IntentFilter(
                NOTIFICATION
            )
        )
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(receiver)
    }
}
