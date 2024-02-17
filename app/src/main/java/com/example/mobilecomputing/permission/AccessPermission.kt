package com.example.mobilecomputing.permission

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

var permissionGranted = false

val permissionList = arrayOf(
    android.Manifest.permission.CAMERA,
    android.Manifest.permission.RECORD_AUDIO
)

fun isPermissionGranted(context: Context): Boolean = permissionList.all {
    ContextCompat.checkSelfPermission(
        context,
        it
    ) == PackageManager.PERMISSION_GRANTED
}