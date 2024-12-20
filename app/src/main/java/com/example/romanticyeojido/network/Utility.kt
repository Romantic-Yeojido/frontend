package com.example.romanticyeojido.network

import android.content.Context
import android.util.Base64
import android.util.Log
import java.security.MessageDigest

object Utility {
    fun getKeyHash(context: Context): String? {
        return try {
            val packageInfo = context.packageManager.getPackageInfo(
                context.packageName,
                android.content.pm.PackageManager.GET_SIGNATURES
            )
            val signatures = packageInfo.signatures
            val md = MessageDigest.getInstance("SHA")
            signatures?.forEach { signature ->
                md.update(signature.toByteArray())
            }
            Base64.encodeToString(md.digest(), Base64.DEFAULT).trim()
        } catch (e: Exception) {
            Log.e("KeyHash", "Error getting key hash", e)
            null
        }
    }
}