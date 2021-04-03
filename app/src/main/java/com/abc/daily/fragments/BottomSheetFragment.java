package com.abc.daily.fragments;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.FragmentManager;
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
import androidx.core.content.ContextCompat;

import com.abc.daily.AddReadNote;
import com.abc.daily.Objects.NoteObjects;
import com.abc.daily.R;
import com.abc.daily.app.AlertReceiver;
import com.abc.daily.app.Application;
import com.abc.daily.app.DatabaseConnector;
import com.abc.daily.app.*;
import com.abc.daily.app.db;
import com.abc.daily.interfaces.ModifyObject;
import com.abc.daily.interfaces.NotificationObject;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;

import java.util.Calendar;
import java.util.Date;

public class BottomSheetFragment extends BottomSheetDialogFragment implements View.OnClickListener {

    MaterialButton datePicker, timePicker, closeSheetBtn, setReminderBtn;
    Calendar calendar;
    DatabaseConnector dbm;
    NoteObjects object;
    public NotificationObject notificationExtras;
    int hour, minute, year, month, dayOfMonth;
    String dateStr, timeStr;

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
        restoreData("");
    }

    private void restoreData(String themeColor) {
        restoreTheme();
        int id = notificationExtras.getObject().getId();
        Cursor cursor = dbm.getDb().rawQuery("SELECT * FROM " + db.Tables.DAILY_NOTE_TABLE + " WHERE "
                + db.Note.NOTE_ID + " = " + id, null);
        while (cursor.moveToNext()) {
            datePicker.setText(cursor.getString(cursor.getColumnIndex(db.Note.REMINDER_DATE)));
            timePicker.setText(cursor.getString(cursor.getColumnIndex(db.Note.REMINDER_TIME)));
        }
        cursor.close();
    }

    private void restoreTheme() {
        String color = spref.get(spref.THEME).getString(spref.Theme.THEME_COLOR, spref.Theme.DEFAULT_THEME_COLOR);
        switch (color) {
            case spref.Theme.PURPLE_COLOR : {
                setColor(R.color.deep_purple_400);
                break;
            }
            case spref.Theme.RED_COLOR : {
                setColor(R.color.red_500);
                break;
            }
            case spref.Theme.ORANGE_COLOR : {
                setColor(R.color.orange_400);
                break;
            }
            case spref.Theme.BLUE_COLOR : {
                setColor(R.color.blue_500);
                break;
            }
            case spref.Theme.GREEN_COLOR : {
                setColor(R.color.green_500);
                break;
            }
            case spref.Theme.TEAL_COLOR: {
                setColor(R.color.teal_500);
                break;
            }
            default:setColor(R.color.teal_500);
        }
    }

    private void setColor(int color) {
        setReminderBtn.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), color));
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
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
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
            intent.putExtra("ntId", object.getId());
            intent.putExtra("ntStr", object.getNoteTitle());
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
        return getString(R.string.At) + c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE);
    }

    private String getDateString(Calendar c) {
        String weekDay = "";
        switch (c.get(Calendar.DAY_OF_WEEK)) {
            case 1 : weekDay = getString(R.string.sun);
                break;
            case 2 : weekDay = getString(R.string.mon);
                break;
            case 3 : weekDay = getString(R.string.tue);
                break;
            case 4 : weekDay = getString(R.string.wed);
                break;
            case 5 : weekDay = getString(R.string.thu);
                break;
            case 6 : weekDay = getString(R.string.fri);
                break;
            case 7 : weekDay = getString(R.string.sat);
                break;
        }

        String monthOfYear = "";
        switch (c.get(Calendar.MONTH)) {
            case 0 : monthOfYear = getString(R.string.jan);
                break;
            case 1 : monthOfYear = getString(R.string.feb);
                break;
            case 2 : monthOfYear = getString(R.string.mar);
                break;
            case 3 : monthOfYear = getString(R.string.apr);
                break;
            case 4 : monthOfYear = getString(R.string.may);
                break;
            case 5 : monthOfYear = getString(R.string.jun);
                break;
            case 6 : monthOfYear = getString(R.string.jul);
                break;
            case 7 : monthOfYear = getString(R.string.aug);
                break;
            case 8 : monthOfYear = getString(R.string.sep);
                break;
            case 9 : monthOfYear = getString(R.string.oct);
                break;
            case 10 : monthOfYear = getString(R.string.nov);
                break;
            case 11 : monthOfYear = getString(R.string.dec);
                break;
        }

        return weekDay + ", " + monthOfYear + ", " + c.get(Calendar.DAY_OF_MONTH);
    }

}
