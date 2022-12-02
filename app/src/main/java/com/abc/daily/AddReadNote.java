package com.abc.daily;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;

import com.abc.daily.Objects.NoteObjects;
import com.abc.daily.fragments.BottomSheetFragment;
import com.abc.daily.interfaces.DialogInterface;
import com.abc.daily.interfaces.ModifyObject;
import com.abc.daily.interfaces.NotificationObject;

import com.abc.daily.app.DatabaseConnector;
import com.abc.daily.app.*;
import com.abc.daily.app.db;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class AddReadNote extends MainActivity implements
        View.OnClickListener, DialogInterface {

    AppCompatImageView back, share, delete;
    AppCompatEditText titleEdtTxt, contentEdtTxt;
    AppCompatTextView appbarTitle, createdDate, modifiedDate;
    FloatingActionButton fab;
    DatabaseConnector dbm;
    NoteObjects objects;
    int id;
    MyDailyDialog dialog;
    long notificationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        restoreTheme(false);
        getWindow().setStatusBarColor(getResources().getColor(R.color.Second_white));
        setContentView(R.layout.layout_add_note);

        init();
        showNote();
    }

    private void init() {
        back = findViewById(R.id.button_addNote_backward);
        share = findViewById(R.id.imageView_addNote_reminder);
        delete = findViewById(R.id.imageView_addNote_delete);
        titleEdtTxt = findViewById(R.id.editText_addNote_title);
        contentEdtTxt = findViewById(R.id.editText_addNote_description);
        appbarTitle = findViewById(R.id.textView_addNote_appbarTitle);
        createdDate = findViewById(R.id.textView_addNote_createdDate);
        modifiedDate = findViewById(R.id.textView_addNote_modifiedDate);
        fab = findViewById(R.id.fab_addNote_save);

        dbm = new DatabaseConnector(this);
        objects = new NoteObjects();

        back.setOnClickListener(this);
        share.setOnClickListener(this);
        delete.setOnClickListener(this);
        fab.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.button_addNote_backward: {
                finish();
                break;
            }
            case R.id.imageView_addNote_reminder: {
                noteReminder();
                break;
            }
            case R.id.imageView_addNote_delete: {
                deleteNote();
                break;
            }
            case R.id.fab_addNote_save: {
                saveNote();
                break;
            }
        }

    }

    @SuppressLint("Range")
    public void showNote() {
        if (getIntent().hasExtra(db.Note.NOTE_ID)) {
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
        }
        else {
            addToDb(false);
        }
        addToDb(true);
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

                objects.reminderDate = (getString(R.string.today));
                objects.reminderTime = (getString(R.string.At));
            }
            objects.noteDate = createdDate.getText().toString();
            objects.noteModifyDate = app.getDateTime(false);
            if (applyAdding)
                dbm.updateNote(objects);
            notificationId = (long) id;
        } else {
            objects.noteTitle = title;
            objects.noteContent = content;
            objects.reminderDate = (getString(R.string.today));
            objects.reminderTime = (getString(R.string.At));
            objects.noteDate = app.getDateTime(false);
            objects.noteModifyDate = app.getDateTime(false);
            if (!applyAdding)
                notificationId = dbm.addNote(objects);
        }
        objects.id = (int) notificationId;
    }


    private void deleteNote() {
        dialog = new MyDailyDialog(
                this,
                getString(R.string.dialog_delete),
                getString(R.string.Dialog_cancel),
                getString(R.string.dialog_delete_note),
                getString(R.string.dialog_delete_note_desc),
                1,
                0,
                this,
                0);
        dialog.setCancelable(true);
        dialog.show();
    }

    NotificationObject notificationObject;
    private void noteReminder() {
        if (titleEdtTxt.getText().toString().isEmpty()) {
            return;
        }
        addToDb(false);
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
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onPositiveClick() {
        dialog.dismiss();
        dbm.deleteNote(id);
        app.t(getString(R.string.toast_delete_succes));
        finish();
    }

    @Override
    public void onNegativeClick() {
        dialog.dismiss();
    }

}