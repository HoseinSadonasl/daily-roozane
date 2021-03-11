package com.abc.daily;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;

import com.abc.daily.Objects.NoteObjects;
import com.abc.daily.fragments.BottomSheetFragment;
import com.abc.daily.interfaces.DialogInterface;
import com.abc.daily.interfaces.ModifyObject;
import com.abc.daily.interfaces.NotificationObject;
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
    long notificationId;

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

    public void showNote() {
        if (getIntent().hasExtra(db.Note.NOTE_ID)) {
            id = getIntent().getIntExtra(db.Note.NOTE_ID, 0);
            app.l("ADD READ NOTE ID: " + id);
            Cursor cursor = dbm.getDb().rawQuery("SELECT * FROM " + db.Tables.DAILY_NOTE_TABLE + " WHERE "
                    + db.Note.NOTE_ID + " = " + id, null);
            while (cursor.moveToNext()) {
                titleEdtTxt.setText(cursor.getString(cursor.getColumnIndex(db.Note.NOTE_TITLE)));
                contentEdtTxt.setText(cursor.getString(cursor.getColumnIndex(db.Note.NOTE_CONTENT)));
                createdDate.setText(cursor.getString(cursor.getColumnIndex(db.Note.NOTE_DATE)));
                modifiedDate.setText(cursor.getString(cursor.getColumnIndex(db.Note.NOTE_LAST_MODIFY)));
            }
            cursor.close();
        } else {
            delete.setVisibility(View.GONE);
        }

    }

    public ModifyObject modifyObject;
    String rDate = "";
    String rTime = "";
    private void saveNote() {
        if(modifyObject != null) {
            objects = modifyObject.get();
            rDate = objects.getReminderDate();
            rTime = objects.getReminderTime();
            app.l("AAAA" + rDate + "---" + rTime);
            app.l("AA"+objects.toString());
        }
        else {
            addToDb(false);
        }
        addToDb(true);
        app.l("6546"+objects.toString());
        finish();
    }

    private void addToDb(boolean applyAdding) {
        int id = getIntent().getIntExtra(db.Note.NOTE_ID, 0);
        String title = titleEdtTxt.getText().toString();
        String content = contentEdtTxt.getText().toString();
        if (title.equals("")) return;
        if (content.equals("")) return;
        if (getIntent().hasExtra(db.Note.NOTE_ID)) {
            objects.id = id;
            objects.noteTitle = title;
            objects.noteContent = content;
            if(modifyObject != null) {
                objects = modifyObject.get();
                objects.reminderDate = rDate;
                objects.reminderTime = rTime;
            } else {

                objects.reminderDate = ("Today");
                objects.reminderTime = ("At: ");
            }
            objects.noteDate = createdDate.getText().toString();
            objects.noteModifyDate = app.getDateTime(false);
            if (applyAdding)
                dbm.updateNote(objects);
            notificationId = (long) id;
        } else {
            objects.noteTitle = title;
            objects.noteContent = content;
            objects.reminderDate = ("Today");
            objects.reminderTime = ("At: ");
            objects.noteDate = app.getDateTime(false);
            objects.noteModifyDate = app.getDateTime(false);
            if (!applyAdding)
            notificationId = dbm.addNote(objects);
            app.l("notificationId" + notificationId);
        }
        app.l(objects.toString());
    }


    private void deleteNote() {
        dialog = new MyDailyDialog(this, "Delete", "Cancel",
                "Delete note?", "Do you  really  want to delete this note?", 1,
                0, this, 0, 0);
        dialog.setCancelable(true);
        dialog.show();
    }

    NotificationObject notificationObject;

    private void noteReminder() {
        if (titleEdtTxt.getText().toString().isEmpty()) {
            app.l("Empty note!");
            return;
        }
        addToDb(false);
        app.l("notificationId" + notificationId);
        BottomSheetFragment fragment = new BottomSheetFragment();
        fragment.notificationExtras = new NotificationObject() {
            @Override
            public NoteObjects getObject() {
                return objects;
            }
        };
        fragment.show(getSupportFragmentManager(), app.TAG);
    }


    @Override
    public void onPositiveClick() {
        dialog.dismiss();
        dbm.deleteNote(id);
        app.t("Deleted successfully");
        finish();
    }

    @Override
    public void onNegativeClick() {
        dialog.dismiss();
    }

}