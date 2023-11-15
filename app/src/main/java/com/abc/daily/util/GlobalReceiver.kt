package com.abc.daily.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.abc.daily.app.NotificationUtil
import dagger.hilt.android.AndroidEntryPoint
import java.util.Date

@AndroidEntryPoint
class GlobalReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        Toast.makeText(context, "RECEIVED", Toast.LENGTH_SHORT).show()
        val notificationIntentId = intent!!.getIntExtra("ntId", 0)
        val notificationTitle = intent.getStringExtra("ntStr")
        val randomId = (Date().time / 1000L % Int.MAX_VALUE).toInt()
        NotificationUtil().createNotification(randomId, notificationTitle)
    }
}