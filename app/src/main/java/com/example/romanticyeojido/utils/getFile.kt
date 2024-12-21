package com.example.romanticyeojido.utils

import android.content.Context
import android.net.Uri
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

object FileUtils {
    fun getFile(context: Context, uri: Uri): File? {
        return try {
            val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
            val file = File(context.cacheDir, "temp_image_${System.currentTimeMillis()}.jpg")
            val outputStream = FileOutputStream(file)
            inputStream?.copyTo(outputStream)
            outputStream.close()
            inputStream?.close()
            file
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
