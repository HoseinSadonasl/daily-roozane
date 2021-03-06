package com.abc.daily.app;

import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.abc.daily.R;

public class NotificationHelper extends ContextWrapper {

    public static final String channelID = "reminder";
    public static final String channelName = "Note reminder";

    private NotificationManager mManager;

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
            //getChannelNotification();
        }
        return mManager;
    }


    public NotificationCompat.Builder getChannelNotification(String notificationTitle) {
        Uri soundUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://"+ getApplicationContext().getPackageName() + "/" + R.raw.daily_notification);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(Application.getContext(), channelID)
                .setSmallIcon(R.drawable.ic_baseline_timer_24)
                .setSound(soundUri)
                .setContentTitle(notificationTitle)
                .setContentText("AlarmManager is working.");
        return builder;
    }

}

