package com.abc.daily.util

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat


object PermissionHelper {
    fun hasPermission(context: Context, permissions: List<String>): List<String> = permissions.filter {
        checkIfPermissionNotGranted(context, it)
    }

    fun checkIfPermissionNotGranted(context: Context, permission: String) = ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED
}