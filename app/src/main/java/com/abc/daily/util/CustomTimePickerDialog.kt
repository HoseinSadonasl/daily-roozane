package com.abc.daily.util

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.View
import android.widget.NumberPicker
import com.abc.daily.R
import com.abc.daily.interfaces.DialogInterface
import com.google.android.material.button.MaterialButton
import java.util.*

class CustomTimePickerDialog(
    context: Context,
    var dialogInterface: DialogInterface?,
) : Dialog(context), View.OnClickListener {

    private lateinit var calendar: Calendar
    lateinit var positive: MaterialButton
    lateinit var negative: MaterialButton
    lateinit var hourNumberPicker: NumberPicker
    lateinit var minuteNumberPicker: NumberPicker
    lateinit var amPmNumberPicker: NumberPicker

    init {
        window!!.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setGravity(Gravity.BOTTOM)
        }
        setContentView(R.layout.layout_custom_timepicker_dialog)
        init()
    }

    private fun init() {
        positive = findViewById(R.id.positive)
        negative = findViewById(R.id.negative)
        hourNumberPicker = findViewById(R.id.numberPicker_hour_addNotesFragment)
        minuteNumberPicker = findViewById(R.id.numberPicker_minute_addNotesFragment)
        amPmNumberPicker = findViewById(R.id.numberPicker_amPm_addNotesFragment)

        positive.apply {
            text = context.getString(R.string.timePickerDialog_submitBtn)
            setOnClickListener(this@CustomTimePickerDialog)
        }
        negative.apply {
            text = context.getString(R.string.timePickerDialog_cancelBtn)
            setOnClickListener(this@CustomTimePickerDialog)
        }

        initTime()
    }

    private fun initTime() {
        initCalendar()
        initHours()
        initMinutes()
        initAmPm()
    }

    private fun initCalendar() {
        calendar = Calendar.getInstance()
    }

    private fun initHours() {
        val values = mutableListOf<String>()
        for (i in 1..12) {
            values.add(i.toString())
        }
        hourNumberPicker.apply {
            maxValue = 12
            minValue = 1
            value = calendar.get(Calendar.HOUR)
            displayedValues = values.toTypedArray()
        }
    }

    private fun initMinutes() {
        val values = mutableListOf<String>()
        for (i in 0..60) {
            values.add(i.toString())
        }
        minuteNumberPicker.apply {
            maxValue = 59
            minValue = 0
            value = calendar.get(Calendar.MINUTE)
            displayedValues = values.toTypedArray()
        }
    }

    private fun initAmPm() {
        val values = mutableListOf(context.getString(R.string.timepicker_am), context.getString(R.string.timepicker_pm))
        amPmNumberPicker.apply {
            maxValue = 1
            minValue = 0
            value = calendar.get(Calendar.AM_PM)
            displayedValues = values.toTypedArray()
        }
    }

    fun getTime(): Calendar {
        calendar.apply {
            set(Calendar.HOUR_OF_DAY, hourNumberPicker.value)
            set(Calendar.MINUTE, minuteNumberPicker.value)
            set(Calendar.SECOND, 0)
            set(Calendar.AM_PM, amPmNumberPicker.value)
        }
        return calendar
    }

    interface DialogInterface {
        fun onPositiveClick(timePickerDialog: CustomTimePickerDialog)
        fun onNegativeClick(timePickerDialog: CustomTimePickerDialog)
    }

    override fun onClick(v: View) {
        if (v === positive) {
            dialogInterface!!.onPositiveClick(this)
        } else {
            dialogInterface!!.onNegativeClick(this)
        }
    }
}