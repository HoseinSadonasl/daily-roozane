package com.abc.daily.util

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat


object PermissionHelper {
    fun hasPermission(context: Context, permissions: List<String>): List<String> = permissions.filter {
        ContextCompat.checkSelfPermission(context, it) != PackageManager.PERMISSION_GRANTED
    }
}