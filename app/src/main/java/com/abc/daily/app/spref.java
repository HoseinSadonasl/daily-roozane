package com.abc.daily.app;

import android.content.Context;
import android.content.SharedPreferences;

public class spref {

    private static SharedPreferences sharedPreferences;

    public static SharedPreferences get(String tag) {
        if (sharedPreferences != null)
            return sharedPreferences;

        sharedPreferences = Application.getContext().getSharedPreferences(tag, Context.MODE_PRIVATE);
        return sharedPreferences;
    }

    public static class tags {
        public static final String WEATHER = "WEATHER";
        public static final String SETTINGS = "SETTINGS";
        public static final String NOTIFICATIONS = "NOTIFICATIONS";
    }

    public static class Weather {
        public static final String cityName = "cityName";
        public static final String defaultCityName = "";
    }


    public static class notifications {
        public static final String NOTIFICATION_TITLE = "NOTIFICATIONTITLE" ;
        public static final String DEFAULT_NOTIFICATION_TITLE = "" ;
    }

}
