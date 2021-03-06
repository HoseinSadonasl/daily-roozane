package com.abc.daily;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;

import com.abc.daily.Objects.NoteObjects;
import com.abc.daily.app.AlertReceiver;
import com.abc.daily.fragments.BottomSheetFragment;
import com.abc.daily.interfaces.DialogInterface;
import com.abc.daily.interfaces.NotificationTitle;
import com.abc.daily.ui.MyDailyDialog;

import com.abc.daily.app.DatabaseConnector;
import com.abc.daily.app.*;
import com.abc.daily.app.db;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class AddReadNote extends AppCompatActivity implements
        View.OnClickListener, DialogInterface {

    AppCompatImageView back, share, delete;
    AppCompatEditText titleEdtTxt, contentEdtTxt;
    AppCompatTextView appbarTitle, createdDate, modifiedDate;
    FloatingActionButton fab;
    DatabaseConnector dbm;
    NoteObjects objects = new NoteObjects();
    int id;
    MyDailyDialog dialog;
    String notificationTitle = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_read_note);

        init();
        showNote();
    }

    private void init() {
        back = findViewById(R.id.back);
        share = findViewById(R.id.reminderBtn);
        delete = findViewById(R.id.delete);
        titleEdtTxt = findViewById(R.id.titleEdtTxt);
        contentEdtTxt = findViewById(R.id.contentEdtTxt);
        appbarTitle = findViewById(R.id.appbarTitle);
        createdDate = findViewById(R.id.createdDate);
        modifiedDate = findViewById(R.id.modifiedDate);
        fab = findViewById(R.id.fab);

        dbm = new DatabaseConnector(this);


        back.setOnClickListener(this);
        share.setOnClickListener(this);
        delete.setOnClickListener(this);
        fab.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.back: {
                finish();
                break;
            }
            case R.id.reminderBtn: {
                noteReminder();
                break;
            }
            case R.id.delete: {
                deleteNote();
                break;
            }
            case R.id.fab: {
                saveNote();
                break;
            }
        }

    }

    private void showNote() {
        if (getIntent().hasExtra(db.Note.NOTE_ID)) {
            delete.setVisibility(View.VISIBLE);
            share.setVisibility(View.VISIBLE);
            id = getIntent().getIntExtra(db.Note.NOTE_ID, 0);
            Cursor cursor = dbm.getDb().rawQuery("SELECT * FROM " + db.Tables.DAILY_NOTE_TABLE + " WHERE "
                    + db.Note.NOTE_ID + " = " + id, null);
            while (cursor.moveToNext()) {
                titleEdtTxt.setText(cursor.getString(cursor.getColumnIndex(db.Note.NOTE_TITLE)));
                contentEdtTxt.setText(cursor.getString(cursor.getColumnIndex(db.Note.NOTE_CONTENT)));
                createdDate.setText(cursor.getString(cursor.getColumnIndex(db.Note.NOTE_DATE)));
                modifiedDate.setText(cursor.getString(cursor.getColumnIndex(db.Note.NOTE_LAST_MODIFY)));
            }
            cursor.close();


        }

    }

    private void saveNote() {
        int id = getIntent().getIntExtra(db.Note.NOTE_ID, 0);
        String title = titleEdtTxt.getText().toString();
        String content = contentEdtTxt.getText().toString();
        if (title.equals("")) return;
        if (content.equals("")) return;
        if (getIntent().hasExtra(db.Note.NOTE_ID)) {
            objects.id = id;
            objects.noteTitle = title;
            objects.noteContent = content;
            objects.noteDate = app.getDateTime(false);
            objects.noteModifyDate = app.getDateTime(false);
            dbm.updateNote(objects);
            finish();
        } else {
            objects.noteTitle = title;
            objects.noteContent = content;
            objects.noteDate = app.getDateTime(false);
            dbm.addNote(objects);
            finish();
        }

    }


    private void deleteNote() {
        dialog = new MyDailyDialog(this, "Delete", "Cancel",
                "Delete note?", "Do you  really  want to delete this note?", 1,
                0, this, 0, 0);
        dialog.setCancelable(true);
        dialog.show();
    }

    private void noteReminder() {

        notificationTitle = titleEdtTxt.getText().toString();

        BottomSheetFragment fragment = new BottomSheetFragment();
        fragment.notificationTitle = new NotificationTitle() {
            @Override
            public String onNotificationTitle() {
                return notificationTitle;
            }
        };
        fragment.show(getSupportFragmentManager(), "TAG");
    }


    @Override
    public void onPositiveClick() {

        dialog.dismiss();
//        dbm.deleteNote(id);
//        app.t("Deleted successfully");هم
//        finish();
    }

    @Override
    public void onNegativeClick() {
        dialog.dismiss();
    }

}