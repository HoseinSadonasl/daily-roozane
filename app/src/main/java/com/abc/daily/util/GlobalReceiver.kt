package com.abc.daily.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import dagger.hilt.android.AndroidEntryPoint
import java.util.Date
import javax.inject.Inject

@AndroidEntryPoint
class GlobalReceiver: BroadcastReceiver() {

    @Inject
    lateinit var notificationUtil: NotificationUtil

    override fun onReceive(context: Context?, intent: Intent?) {
        Toast.makeText(context, "RECEIVED", Toast.LENGTH_SHORT).show()
        val notificationIntentId = intent!!.getIntExtra("ntId", 0)
        val notificationTitle = intent.getStringExtra("ntStr")
        val randomId = (Date().time / 1000L % Int.MAX_VALUE).toInt()
        notificationUtil.createNotification(randomId, notificationTitle)
    }
}