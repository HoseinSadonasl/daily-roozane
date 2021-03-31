package com.abc.daily.app;

public class db {

    public static class Tables{
        public static final String DAILY_NOTE_TABLE = "daily_note_db";
        public static final int DB_VERSION = 1;
    }


    public static class Note{

        public static final String NOTE_ID = "NOTE_ID";
        public static final String NOTE_TITLE = "NOTE_TITLE";
        public static final String NOTE_CONTENT = "NOTE_CONTENT";
        public static final String NOTE_DATE = "NOTE_DATE";
        public static final String NOTE_LAST_MODIFY = "note_last_modify";
        public static final String REMINDER_TIME = "REMINDER_TIME";
        public static final String REMINDER_DATE = "REMINDER_DATE";

    }



}
