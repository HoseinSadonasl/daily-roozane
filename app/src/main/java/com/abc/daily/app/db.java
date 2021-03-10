package com.abc.daily.app;

public class db {

    public static class Tables{
        public static final String DAILY_NOTE_TABLE = "daily_note_db";
        public static final int DB_VERSION = 1;
    }


    public static class Note{

        public static final String NOTE_ID = "note_id";
        public static final String NOTE_TITLE = "note_title";
        public static final String NOTE_CONTENT = "note_content";
        public static final String NOTE_DATE = "note_date";
        public static final String NOTE_LAST_MODIFY = "note_last_modify";
        public static final String REMINDER_TIME = "reminder_time";
        public static final String REMINDER_DATE = "reminder_date";

    }



}
