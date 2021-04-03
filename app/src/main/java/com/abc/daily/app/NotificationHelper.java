package com.abc.daily.app;

import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.abc.daily.AddReadNote;
import com.abc.daily.Objects.NoteObjects;
import com.abc.daily.R;
import com.abc.daily.interfaces.NotificationObject;

public class NotificationHelper extends ContextWrapper {

    public static final String channelID = "reminder";
    public static final String channelName = "Note reminder";

    private NotificationManager mManager;
    PendingIntent mIntent;
    int id = 0;

    public NotificationHelper(Context base) {
        super(base);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel();
        }
    }
    @TargetApi(Build.VERSION_CODES.O)
    private void createChannel() {
        NotificationChannel channel = new NotificationChannel(
                channelID, channelName, NotificationManager.IMPORTANCE_HIGH);
        getManager().createNotificationChannel(channel);
    }


    public NotificationManager getManager() {
        if (mManager == null) {
            mManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return mManager;
    }

    private PendingIntent pIntent(int id) {
        Intent pIntent = new Intent(this, AddReadNote.class);
        pIntent.putExtra(db.Note.NOTE_ID, id);
        mIntent = PendingIntent.getActivity(
                this, 0, pIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        return mIntent;
    }


    public NotificationCompat.Builder getChannelNotification(int notificationId , String notificationTitle) {
        id = notificationId;
        Uri soundUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://"+ getApplicationContext().getPackageName() + "/" + R.raw.daily_notification);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(Application.getContext(), channelID)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentIntent(pIntent(id))
                .setSound(soundUri)
                .setContentTitle(notificationTitle)
                .setContentText(getString(R.string.tap_for_more))
                .setAutoCancel(true);
        return builder;
    }



}

