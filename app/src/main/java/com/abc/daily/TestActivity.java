package com.abc.daily;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.abc.daily.app.ShamsiCalendar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;



public class TestActivity extends AppCompatActivity {

    FloatingActionButton fab;
    NotificationManager manager;
    String channelIdName = "Channel name";
    PendingIntent mpIntent;
    ShamsiCalendar shamsiCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        fab = findViewById(R.id.floatingActionButton);



        Intent pIntent = new Intent(this, MainActivity.class);
        mpIntent = PendingIntent.getActivity(
                this, 0, pIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //createNotifChannel();
                //simpleNotification();
                //pendingNotification();
                test0();
            }
        });
    }

    private void test0() {
        int year = ShamsiCalendar.getYear();
        int month = ShamsiCalendar.getMonth();
        int day = ShamsiCalendar.getDay();

    }






    private void pendingNotification() {

        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle()
                .bigText("BIG TEXT!!!  00000000000000000000000000000000000000000000000000000000")
                .setBigContentTitle("BIG TEXT TITLE")
                .setSummaryText("SummaryText");

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(TestActivity.this, channelIdName)
                        .setSmallIcon(R.drawable.ic_bell)
                        .setContentTitle("SimpleTitle")
                        .setContentText("This is simple notification")
                        //.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_bell))
                        //.setContentIntent(mpIntent)
                        //.addAction(R.drawable.ic_bell, "Action1", mpIntent)
                        //.addAction(R.drawable.ic_bell, "Action2", mpIntent)
                        //.setAutoCancel(true)
                        .setStyle(bigTextStyle);

        manager.notify(3, builder.build());
    }

    private void simpleNotification() {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(TestActivity.this, channelIdName)
                .setSmallIcon(R.drawable.ic_bell)
                .setContentTitle("SimpleTitle")
                .setContentText("This is simple notification")
                .setVibrate(new long[] {1000, 500, 500, 250, 250})
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_bell));
        manager.notify(1, builder.build());
    }

    private void createNotifChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            String offerChannelName = "offerChannelName";
            String offerChannelDescription = "offerChannelDescription";

            int channelImportance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel notificationChannel = new NotificationChannel(
                    channelIdName, offerChannelName, channelImportance);

            notificationChannel.setDescription(offerChannelDescription);
            notificationChannel.enableVibration(true);
            notificationChannel.enableLights(true);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.GREEN);
            manager.createNotificationChannel(notificationChannel);
        }
    }

}