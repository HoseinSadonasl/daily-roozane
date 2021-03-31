package com.abc.daily.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.abc.daily.Objects.NoteObjects;
import com.abc.daily.R;

import java.util.Date;

public class AlertReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationHelper notificationHelper = new NotificationHelper(context);
        int nId = intent.getIntExtra("ntId", 0);
        String nTitle = intent.getStringExtra("ntStr");
        int randomId = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
        notificationHelper.getManager().notify(randomId, notificationHelper.getChannelNotification(nId, nTitle).build());

    }
}

