package ru.mail.technotrack.ipfs.fragments

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_upload_menu.*

import ru.mail.technotrack.ipfs.R
import android.graphics.Bitmap
import android.widget.ImageView


class UploadMenuFragment : BottomSheetDialogFragment() {

    private val REQUEST_CAMERA = 1
    private val REQUEST_IMAGE_CAPTURE = 2
    private val REQUEST_READ_EXTERNAL_FOR_GALLERY = 3
    private val REQUEST_PICK_IMAGE = 4
    private val REQUEST_READ_EXTERNAL_FOR_STORAGE = 5
    private val REQUEST_PICK_FILE = 6

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_upload_menu, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        navigation_view.setNavigationItemSelectedListener { menuItem ->
            when {
                menuItem.itemId == R.id.item_camera -> {
                    if (ContextCompat.checkSelfPermission(
                            context!!,
                            Manifest.permission.CAMERA
                        )
                        != PackageManager.PERMISSION_GRANTED
                    ) {
                        ActivityCompat.requestPermissions(
                            activity!!,
                            arrayOf(Manifest.permission.CAMERA),
                            REQUEST_CAMERA
                        )
                        dismiss()
                        true
                    } else {
                        dispatchTakePictureIntent()
                        dismiss()
                        true
                    }
                }
                menuItem.itemId == R.id.item_gallery -> {
                    if (ContextCompat.checkSelfPermission(
                            context!!,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        ActivityCompat.requestPermissions(
                            activity!!,
                            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                            REQUEST_READ_EXTERNAL_FOR_GALLERY)
                        dismiss()
                        true
                    } else {
                        dispatchSelectPictureFromGalleryIntent()
                        dismiss()
                        true
                    }
                }
                menuItem.itemId == R.id.item_storage -> {
                    if (ContextCompat.checkSelfPermission(
                            context!!,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        ActivityCompat.requestPermissions(
                            activity!!,
                            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                            REQUEST_READ_EXTERNAL_FOR_STORAGE)
                        dismiss()
                        true
                    } else {
                        dispatchSelectFileFromStorageIntent()
                        dismiss()
                        true
                    }
                }
                else -> {
                    dismiss()
                    false
                }
            }
        }
    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(context!!.packageManager)?.also {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    private fun dispatchSelectPictureFromGalleryIntent() {
        Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            .also {galleryIntent ->
                galleryIntent.resolveActivity(context!!.packageManager)?.also {
                    startActivityForResult(galleryIntent, REQUEST_PICK_IMAGE)
                }
            }
    }

    private fun dispatchSelectFileFromStorageIntent() {
        Intent(Intent.ACTION_GET_CONTENT)
            .also {storageIntent ->
                storageIntent.type = "*/*"
                storageIntent.resolveActivity(context!!.packageManager)?.also {
                    startActivityForResult(storageIntent, REQUEST_PICK_FILE)
                }
            }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CAMERA && grantResults[0] == PermissionChecker.PERMISSION_GRANTED) {
            dispatchTakePictureIntent()
        }

        if (requestCode == REQUEST_READ_EXTERNAL_FOR_GALLERY && grantResults[0] == PermissionChecker.PERMISSION_GRANTED) {
            dispatchSelectPictureFromGalleryIntent()
        }

        if (requestCode == REQUEST_READ_EXTERNAL_FOR_STORAGE && grantResults[0] == PermissionChecker.PERMISSION_GRANTED) {
            dispatchSelectFileFromStorageIntent()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val photo = data!!.extras.get("data") as Bitmap
            val imageView = this.activity!!.findViewById<ImageView>(R.id.imageView)
            imageView.setImageBitmap(photo)
        }

        if (requestCode == REQUEST_PICK_IMAGE && resultCode == RESULT_OK) {
//            do something
        }

        if (requestCode == REQUEST_PICK_FILE && resultCode == RESULT_OK) {
//            do something
        }

    }

}
