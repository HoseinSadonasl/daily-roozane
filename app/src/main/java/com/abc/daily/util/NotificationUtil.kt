package com.abc.daily.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.abc.daily.R
import com.abc.daily.ui.main.MainActivity
import com.abc.daily.ui.add_note.AddNoteFragment
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class NotificationUtil @Inject constructor(@ApplicationContext private val context: Context) {

    companion object {
        const val CHANNEL_ID = "daily"
        const val CHANNEL_NAME = "dailyReminder"
    }

    fun createNotification(
        notificationId: Int,
        notificationTitle: String?
    ) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationCnhannel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(notificationCnhannel)
        }
        val pendingIntent = createPendingIntent(notificationId)
        val notificationBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setContentIntent(pendingIntent)
            .setContentTitle(notificationTitle)
            .setContentText(context.getString(R.string.tap_for_more))
            .setAutoCancel(true)
            .setChannelId(CHANNEL_ID)

        notificationManager.notify(0, notificationBuilder.build())
    }

    private fun createPendingIntent(id: Int): PendingIntent? {
        val pIntent = Intent(context, MainActivity::class.java).apply {
            putExtra(AddNoteFragment.NOTE_ARGUMENT, id)
        }
        return PendingIntent.getActivity(context, 0, pIntent,  PendingIntent.FLAG_IMMUTABLE)
    }


}
