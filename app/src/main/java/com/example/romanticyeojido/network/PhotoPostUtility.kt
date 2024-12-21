package com.example.romanticyeojido.network

import android.content.ContentResolver
import android.net.Uri
import android.provider.MediaStore
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

fun convertUriToMultipartBody(uri: Uri, contentResolver: ContentResolver): MultipartBody.Part? {
    val file = File(getRealPathFromURI(uri, contentResolver))
    val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
    return MultipartBody.Part.createFormData("image", file.name, requestBody)
}

fun getRealPathFromURI(uri: Uri, contentResolver: ContentResolver): String {
    val cursor = contentResolver.query(uri, null, null, null, null)
    cursor?.moveToFirst()
    val columnIndex = cursor?.getColumnIndex(MediaStore.Images.Media.DATA)
    val path = cursor?.getString(columnIndex ?: 0)
    cursor?.close()
    return path ?: ""
}
