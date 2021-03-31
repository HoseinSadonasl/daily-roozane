package com.abc.daily.app;

import android.content.Context;
import android.content.SharedPreferences;

import com.abc.daily.R;

public class spref {

    public static final String WEATHER = "WEATHER";
    public static final String THEME = "THEME";
    public static final String SORT = "SORT";
    public static final String SORT_TYPE = "SORT_TYPE";
    public static final String SORT_ICON_ID = "SORT_ICON_ID";

    private static SharedPreferences sharedPreferences;

    public static SharedPreferences get(String tag) {
        if (sharedPreferences != null)
            return sharedPreferences;

        sharedPreferences = Application.getContext().getSharedPreferences(tag, Context.MODE_PRIVATE);
        return sharedPreferences;
    }


    public static class Weather {
        public static final String CITY_NAME = "cityName";
        public static final String DEFAULT_CITY_NAME = "";
    }

    public static class SortState {
        public static final String SORT_BY_NAME = db.Note.NOTE_TITLE;
        public static final String SORT_BY_DATE = db.Note.NOTE_ID;
        public static final String SORT_DEFAULT = SORT_BY_DATE;
    }

    public static class SortType {
        public static final String ASC = "ASC";
        public static final String DESC = "DESC";
        public static final String DEFAULT_TYPE = ASC;
    }

    public static class SortButtonText {
        public static final String SORT_BTN_TXT_DATE = Application.getContext().getString(R.string.sort_date);
        public static final String SORT_BTN_TXT_NAME = Application.getContext().getString(R.string.sort_name);
    }

    public static class Theme {
        public static final String THEME_COLOR = "";
        public static final String DEFAULT_THEME_COLOR = "TEAL";

        public static final String PURPLE_COLOR = "PURPLE";
        public static final String RED_COLOR = "RED";
        public static final String ORANGE_COLOR = "ORANGE";
        public static final String BLUE_COLOR = "BLUE";
        public static final String TEAL_COLOR = "TEAL";
        public static final String GREEN_COLOR = "GREEN";
    }
}
