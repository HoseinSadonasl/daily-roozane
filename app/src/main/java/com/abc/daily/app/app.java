package com.abc.daily.app;

import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class app {

    public static String TAG = "Daily";


    public static void l(String message) {
        Log.e(app.TAG, message);
    }

    public static void t(String message) {
        Toast.makeText(Application.getContext(), message, Toast.LENGTH_SHORT).show();
    }

    public static String getDateTime(Boolean clock){
        Calendar cal = Calendar.getInstance();
        if (clock) {
            SimpleDateFormat currentTime = new SimpleDateFormat("H:mm");
            return currentTime.format(cal.getTime());
        } else {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyy hh:mm a");
            return dateFormat.format(cal.getTime());
        }

    }



}
