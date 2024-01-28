package com.abc.daily.util

import android.content.Context
import com.abc.daily.Dialog
import com.abc.daily.R

fun showMessageDialog(context: Context, message: String) = Dialog(context, onPositiveCallback = { it.dismiss() }).apply {
    setTitle(context.getString(R.string.app_name))
    setDescription(message)
    setPositiveButtonText(context.getString(R.string.gotit_all))
}.show()