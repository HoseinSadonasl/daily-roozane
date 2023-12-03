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

    companion object {
        const val NOTIFICATION_NOTE_ID = "notification_note_id"
        const val NOTIFICATION_NOTE_TITLE = "notification_note_title"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.let {
            val notificationTitle = it.getStringExtra("ntStr")
            val randomId = (Date().time / 1000L % Int.MAX_VALUE).toInt()
            notificationUtil.createNotification(randomId, notificationTitle)
        }
    }
}