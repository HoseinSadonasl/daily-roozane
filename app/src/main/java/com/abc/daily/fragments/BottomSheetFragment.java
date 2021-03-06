package com.abc.daily.fragments;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.abc.daily.AddReadNote;
import com.abc.daily.MainActivity;
import com.abc.daily.Objects.NoteObjects;
import com.abc.daily.R;
import com.abc.daily.app.AlertReceiver;
import com.abc.daily.app.Application;
import com.abc.daily.app.app;
import com.abc.daily.app.db;
import com.abc.daily.interfaces.NotificationTitle;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.mohamadamin.persianmaterialdatetimepicker.time.RadialPickerLayout;
import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class BottomSheetFragment extends BottomSheetDialogFragment implements View.OnClickListener {

    AppCompatTextView dateTxt, timeTxt;
    AppCompatImageView closeSheetBtn, datePicker, timePicker;
    MaterialButton setReminderBtn;
    Calendar calendar;




    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(@NonNull Dialog dialog, int style) {
        super.setupDialog(dialog, style);

        View view = LayoutInflater.from(getContext()).inflate(R.layout.bottom_sheet_layout, null);
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

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.closeSheetBtn : {
                closeSheet();
                break;
            }
            case R.id.setReminderBtn : {
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
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            }
        }, year, month, dayOfMonth);
        datePickerDialog.show();

        dateTxt.setText(year + "-" + month + ":" + dayOfMonth);

    }

    private void getTime() {

        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                calendar.set(Calendar.SECOND, 0);
            }
        }, hour, minute, true);
        timePickerDialog.show();

        timeTxt.setText(hour + ":" + minute);
    }


    private void setReminder() {
        setAlarm(calendar);
        dismiss();
    }

    public NotificationTitle notificationTitle;

    private void setAlarm(Calendar c) {

        AlarmManager alarmManager = (AlarmManager) Application.getContext().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getActivity(), AlertReceiver.class);

        if (notificationTitle != null)
            intent.putExtra("ntStr", notificationTitle.onNotificationTitle());

        int randomId = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), randomId, intent, 0);

        if (c.before(Calendar.getInstance())) {
            c.add(Calendar.DATE, 1);
        }

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
        app.l("Time milis: " + c.getTimeInMillis());

    }

}
