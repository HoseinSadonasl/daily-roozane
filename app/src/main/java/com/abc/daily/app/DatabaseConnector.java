package com.abc.daily.app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import com.abc.daily.Objects.NoteObjects;

public class DatabaseConnector extends SQLiteOpenHelper {

    SQLiteDatabase sqLiteDatabase;

    public DatabaseConnector(@Nullable Context context) {
        super(context, db.Tables.DAILY_NOTE_TABLE, null, db.Tables.DB_VERSION);
        //this.onCreate(sqLiteDatabase);
    }


    @Override
    public void onCreate(SQLiteDatabase database) {

        String query = "CREATE TABLE IF NOT EXISTS "
                + db.Tables.DAILY_NOTE_TABLE + "("
                + db.Note.NOTE_ID          + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + db.Note.NOTE_TITLE       + " VARCHAR(100), "
                + db.Note.NOTE_CONTENT     + " VARCHAR,      "
                + db.Note.NOTE_DATE        + " VARCHAR,      "
                + db.Note.NOTE_LAST_MODIFY + " VARCHAR       )";
        database.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
//        database.execSQL("DROP TABLE IF EXISTS " + db.Tables.DAILY_NOTE_TABLE );
//        onCreate(database);
    }


    public void addNote(NoteObjects noteObjects) {

        SQLiteDatabase addDb = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(db.Note.NOTE_TITLE, noteObjects.noteTitle);
        values.put(db.Note.NOTE_CONTENT, noteObjects.noteContent);
        values.put(db.Note.NOTE_DATE, noteObjects.noteDate);
        addDb.insert(db.Tables.DAILY_NOTE_TABLE, null, values);
        addDb.close();

    }

    public void updateNote(NoteObjects noteObjects) {

        SQLiteDatabase addDb = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(db.Note.NOTE_TITLE, noteObjects.noteTitle);
        values.put(db.Note.NOTE_CONTENT, noteObjects.noteContent);
        values.put(db.Note.NOTE_DATE, noteObjects.noteDate);
        values.put(db.Note.NOTE_LAST_MODIFY, noteObjects.noteModifyDate);
        addDb.update(db.Tables.DAILY_NOTE_TABLE, values, db.Note.NOTE_ID + " = ? ", new String[] {String.valueOf(noteObjects.id)});

    }

    public List<NoteObjects> getAllNotes() {

        SQLiteDatabase getDb = getReadableDatabase();
        List<NoteObjects>  list = new ArrayList<>();
        Cursor cursor = getDb.rawQuery("SELECT * FROM " + db.Tables.DAILY_NOTE_TABLE, null);
        if (cursor.moveToFirst()) {
            for (int i = 0 ; i < cursor.getCount() ; i++) {
                NoteObjects noteObjects = new NoteObjects();
                noteObjects.setId(cursor.getInt(0));
                noteObjects.setNoteTitle(cursor.getString(1));
                noteObjects.setNoteContent(cursor.getString(2));
                noteObjects.setNoteDate(cursor.getString(3));
                list.add(noteObjects);
                cursor.moveToNext();
            }
        }
        cursor.close();

        return list;
    }

    public SQLiteDatabase getDb() {
        SQLiteDatabase db = getWritableDatabase();
        return db;
    }

    public void deleteNote(int id) {
        SQLiteDatabase ddb = getWritableDatabase();
        ddb.delete(db.Tables.DAILY_NOTE_TABLE, db.Note.NOTE_ID + "=?" , new String[] {String.valueOf(id)});
        ddb.close();
    }


}
