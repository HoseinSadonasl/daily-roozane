package com.abc.daily.util

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.View
import android.widget.NumberPicker
import com.abc.daily.R
import com.google.android.material.button.MaterialButton
import java.util.*

class CustomDatePickerDialog(
    context: Context,
    var dialogInterface: DialogInterface?,
) : Dialog(context), View.OnClickListener {

    private lateinit var now: PersianDate
    private lateinit var calendar: Calendar
    lateinit var positive: MaterialButton
    lateinit var negative: MaterialButton
    lateinit var yearNumberPicker: NumberPicker
    lateinit var monthNumberPicker: NumberPicker
    lateinit var dayNumberPicker: NumberPicker

    init {
        window!!.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setGravity(Gravity.BOTTOM)
        }
        setContentView(R.layout.layout_custom_datepicker_dialog)
        init()
    }

    private fun init() {
        positive = findViewById(R.id.positive)
        negative = findViewById(R.id.negative)
        yearNumberPicker = findViewById(R.id.numberPicker_year_addNotesFragment)
        monthNumberPicker = findViewById(R.id.numberPicker_month_addNotesFragment)
        dayNumberPicker = findViewById(R.id.numberPicker_day_addNotesFragment)

        positive.apply {
            text = context.getString(R.string.ok)
            setOnClickListener(this@CustomDatePickerDialog)
        }
        negative.apply {
            text = context.getString(R.string.cancel)
            setOnClickListener(this@CustomDatePickerDialog)
        }

        initCalendar()
        initPersianDate()
    }

    private fun initCalendar() {
        calendar = Calendar.getInstance()
    }

    private fun initPersianDate() {
        now = PersianDate(System.currentTimeMillis()).apply {
            initYearNumberPicker(shYear)
            initMonthNumberPicker(shMonth)
            initDayNumberPicker(shDay, monthLength)
        }
    }

    private fun initYearNumberPicker(shYear: Int) {
        val values = mutableListOf<String>()
        for (i in 0..9) {
            if (i == 0) {
                values.add(shYear.toString())
            } else {
                values.add((shYear.plus(i)).toString())
            }
        }
        yearNumberPicker.apply {
            maxValue = shYear + 9
            minValue = shYear
            value = shYear
            displayedValues = values.toTypedArray()
        }
    }

    private fun initMonthNumberPicker(shMonth: Int) {
        val values = mutableListOf<String>()
        for (i in 0..11) {
            values.add(i.toString())
        }
        monthNumberPicker.apply {
            maxValue = 11
            minValue = 0
            value = shMonth
            displayedValues = values.toTypedArray()
        }
    }

    private fun initDayNumberPicker(monthDay: Int, monthLength: Int) {
        val values = mutableListOf<String>()
        for (i in 1..monthLength) {
            values.add(i.toString())
        }
        dayNumberPicker.apply {
            maxValue = monthLength
            minValue = 1
            value = monthDay
            wrapSelectorWheel = true
            displayedValues = values.toTypedArray()
        }
    }

    fun getDate(): Calendar {
        val date = PersianDate().jalali_to_gregorian(
            yearNumberPicker.value,
            monthNumberPicker.value,
            dayNumberPicker.value
        )
        calendar.apply {
            set(Calendar.YEAR, date[0])
            set(Calendar.MONTH, date[1] - 1)
            set(Calendar.DAY_OF_MONTH, date[2])
        }
        return calendar
    }

    interface DialogInterface {
        fun onPositiveClick(datePickerDialog: CustomDatePickerDialog)
        fun onNegativeClick(datePickerDialog: CustomDatePickerDialog)
    }

    override fun onClick(v: View) {
        if (v === positive) {
            dialogInterface!!.onPositiveClick(this)
        } else {
            dialogInterface!!.onNegativeClick(this)
        }
    }
}