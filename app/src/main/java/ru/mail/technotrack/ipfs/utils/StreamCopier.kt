package ru.mail.technotrack.ipfs.utils

import android.util.Log
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream


object StreamCopier {
    fun copyStream(inputStream: InputStream, outputStream: OutputStream) {
        val bufferSize = 1024
        try {
            val bytes = ByteArray(bufferSize)
            while (true) {
                val count = inputStream.read(bytes, 0, bufferSize)
                if (count == -1)
                    break
                outputStream.write(bytes, 0, count)
            }
        } catch (ex: Exception) {
            Log.d("ERROR", "Can't copy inputStream into outputStream")
        } finally {
            try {
                inputStream.close()
                outputStream.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

    }
}