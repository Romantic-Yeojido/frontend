package com.example.romanticyeojido.utils

import android.content.Context
import android.net.Uri
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody

fun createImageMultipart(context: Context, uri: Uri, paramName: String): MultipartBody.Part? {
    val file = FileUtils.getFile(context, uri) ?: return null
    val requestBody = RequestBody.create("image/*".toMediaTypeOrNull(), file)
    return MultipartBody.Part.createFormData(paramName, file.name, requestBody)
}