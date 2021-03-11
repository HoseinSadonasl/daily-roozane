package com.abc.daily.fragments;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.abc.daily.AddReadNote;
import com.abc.daily.Objects.NoteObjects;
import com.abc.daily.R;
import com.abc.daily.app.AlertReceiver;
import com.abc.daily.app.Application;
import com.abc.daily.app.DatabaseConnector;
import com.abc.daily.app.app;
import com.abc.daily.app.db;
import com.abc.daily.interfaces.ModifyObject;
import com.abc.daily.interfaces.NotificationObject;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;

import java.util.Calendar;
import java.util.Date;

public class BottomSheetFragment extends BottomSheetDialogFragment implements View.OnClickListener {

    AppCompatTextView dateTxt, timeTxt;
    MaterialButton datePicker, timePicker;
    MaterialButton closeSheetBtn, setReminderBtn;
    Calendar calendar;
    DatabaseConnector dbm;
    NoteObjects object;
    public NotificationObject notificationExtras;
    int hour;
    int minute;
    int year;
    int month;
    int dayOfMonth;
    String dateStr;
    String timeStr;

    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(@NonNull Dialog dialog, int style) {
        super.setupDialog(dialog, style);

        View view = LayoutInflater.from(getContext()).inflate(R.layout.reminder_sheet_layout, null);
        dialog.setContentView(view);

        init(view);

        CoordinatorLayout.LayoutParams params =
                (CoordinatorLayout.LayoutParams) ((View) view.getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = params.getBehavior();

        if (behavior != null && behavior instanceof BottomSheetBehavior) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                @Override
                public void onStateChanged(@NonNull View bottomSheet, int newState) {


                    switch (newState) {
                        case BottomSheetBehavior.STATE_DRAGGING: {
                            break;
                        }
                        case BottomSheetBehavior.STATE_SETTLING: {
                            break;
                        }
                        case BottomSheetBehavior.STATE_EXPANDED: {

                            break;
                        }
                        case BottomSheetBehavior.STATE_COLLAPSED: {
                            break;
                        }
                        case BottomSheetBehavior.STATE_HIDDEN: {
                            dismiss();
                            break;
                        }
                    }

                }

                @Override
                public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                }
            });
        }
    }

    private void init(View view) {
        dateTxt = view.findViewById(R.id.dateTxt);
        timeTxt = view.findViewById(R.id.timeTxt);
        closeSheetBtn = view.findViewById(R.id.closeSheetBtn);
        datePicker = view.findViewById(R.id.datepicker);
        timePicker = view.findViewById(R.id.timePicker);
        setReminderBtn = view.findViewById(R.id.setReminderBtn);

        closeSheetBtn.setOnClickListener(this);
        setReminderBtn.setOnClickListener(this);
        datePicker.setOnClickListener(this);
        timePicker.setOnClickListener(this);

        calendar = Calendar.getInstance();

        object = new NoteObjects();
        dbm = new DatabaseConnector(getActivity());
        restoreData();
    }

    private void restoreData() {
            int id = notificationExtras.getObject().getId();
            Cursor cursor = dbm.getDb().rawQuery("SELECT * FROM " + db.Tables.DAILY_NOTE_TABLE + " WHERE "
                    + db.Note.NOTE_ID + " = " + id, null);
            while (cursor.moveToNext()) {
                datePicker.setText(cursor.getString(cursor.getColumnIndex(db.Note.REMINDER_DATE)));
                timePicker.setText(cursor.getString(cursor.getColumnIndex(db.Note.REMINDER_TIME)));
            }
            cursor.close();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.closeSheetBtn: {
                closeSheet();
                break;
            }
            case R.id.setReminderBtn: {
                setReminder();
                break;
            }
            case R.id.datepicker: {
                getDate();
                break;
            }
            case R.id.timePicker: {
                getTime();
                break;
            }
        }

    }


    private void closeSheet() {
        dismiss();
    }

    private void getDate() {
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                datePicker.setText(getDateString(calendar));
            }
        }, year, month, dayOfMonth);
        datePickerDialog.show();
    }

    private void getTime() {
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                calendar.set(Calendar.SECOND, 0);
                timePicker.setText(getTimeString(calendar));
            }
        }, hour, minute, false);
        timePickerDialog.show();

    }


    private void setReminder() {
        setAlarm(calendar);
        dismiss();
    }


    private void setAlarm(Calendar c) {
        AlarmManager alarmManager = (AlarmManager) Application.getContext().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getActivity(), AlertReceiver.class);

        dateStr = getDateString(c);
        timeStr = getTimeString(c);

        if (notificationExtras != null) {
            object = notificationExtras.getObject();
            int id = object.getId();
            intent.putExtra("ntId", object.getId());
            intent.putExtra("ntStr", object.getNoteTitle());
            object.id = id;
            object.reminderDate = dateStr;
            object.reminderTime = timeStr;
            dbm.updateNote(object);

            app.l(object.toString());
            ((AddReadNote) getActivity()).modifyObject = new ModifyObject() {
                @Override
                public NoteObjects get() {
                    return object;
                }
            };
        }

        int randomId = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), randomId, intent, 0);

        if (c.before(Calendar.getInstance())) {
            c.add(Calendar.DATE, 1);
        }
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);

    }

    private String getTimeString(Calendar c) {
        return "At: " + c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE);
    }

    private String getDateString(Calendar c) {
        String weekDay = "";
        switch (c.get(Calendar.DAY_OF_WEEK)) {
            case 1 : weekDay = "Sun";
                break;
            case 2 : weekDay = "Mon";
                break;
            case 3 : weekDay = "Tue";
                break;
            case 4 : weekDay = "Wed";
                break;
            case 5 : weekDay = "Thu";
                break;
            case 6 : weekDay = "Fri";
                break;
            case 7 : weekDay = "Sat";
                break;
        }
        String monthOfYear = "";
        switch (c.get(Calendar.MONTH)) {
            case 1 : monthOfYear = "Jan";
                break;
            case 2 : monthOfYear = "Feb";
                break;
            case 3 : monthOfYear = "Mar";
                break;
            case 4 : monthOfYear = "Apr";
                break;
            case 5 : monthOfYear = "May";
                break;
            case 6 : monthOfYear = "Jun";
                break;
            case 7 : monthOfYear = "Jul";
                break;
            case 8 : monthOfYear = "Aug";
                break;
            case 9 : monthOfYear = "Sep";
                break;
            case 10 : monthOfYear = "Oct";
                break;
            case 11 : monthOfYear = "Nov";
                break;
            case 12 : monthOfYear = "Dec";
                break;
        }
        return weekDay + ", " + monthOfYear + ", " + c.get(Calendar.DAY_OF_MONTH);
    }

}
