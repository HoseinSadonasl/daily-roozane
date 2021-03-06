package com.abc.daily.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import com.abc.daily.R;
import com.abc.daily.interfaces.NotificationTitle;

import java.util.Date;

public class AlertReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationHelper notificationHelper = new NotificationHelper(context);
        int randomId = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
        notificationHelper.getManager().notify(randomId, notificationHelper.getChannelNotification(intent.getStringExtra("ntStr")).build());
    }
}

