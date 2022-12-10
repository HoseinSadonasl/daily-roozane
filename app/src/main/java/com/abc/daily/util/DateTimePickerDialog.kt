package com.abc.daily.util

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.widget.NumberPicker
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import com.abc.daily.R
import com.abc.daily.interfaces.DialogInterface
import com.google.android.material.button.MaterialButton
import java.util.*

class DateTimePickerDialog(
    context: Context,
    private var positiveText: String,
    private var negativeText: String,
    private var titleText: String,
    private var subtitleText: String,
    private var alertSubtitleVisibility: Int,
    private var textInputVisibility: Int,
    var dialogInterface: DialogInterface?,
    var imageVisibility: Int
) : Dialog(context), View.OnClickListener {

    private lateinit var now: PersianDate
    private lateinit var calendar: Calendar
    lateinit var positive: MaterialButton
    lateinit var negative: MaterialButton
    lateinit var alertTitle: AppCompatTextView
    lateinit var alertSubtitle: AppCompatTextView
    lateinit var textInput: AppCompatEditText
    lateinit var image_view: AppCompatImageView

    lateinit var yearNumberPicker: NumberPicker
    lateinit var monthNumberPicker: NumberPicker
    lateinit var dayNumberPicker: NumberPicker

    lateinit var hourNumberPicker: NumberPicker
    lateinit var minuteNumberPicker: NumberPicker
    lateinit var amPmNumberPicker: NumberPicker

    init {
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setContentView(R.layout.layout_custom_dialog_datetimepicker)
        init()
    }

    private fun init() {
        positive = findViewById(R.id.positive)
        negative = findViewById(R.id.negative)
        alertTitle = findViewById(R.id.alertTitle)
        alertSubtitle = findViewById(R.id.alertSubtitle)
        textInput = findViewById(R.id.textInput)
        image_view = findViewById(R.id.image_view)
        yearNumberPicker = findViewById(R.id.numberPicker_year_addNotesFragment)
        monthNumberPicker = findViewById(R.id.numberPicker_month_addNotesFragment)
        dayNumberPicker = findViewById(R.id.numberPicker_day_addNotesFragment)
        hourNumberPicker = findViewById(R.id.numberPicker_hour_addNotesFragment)
        minuteNumberPicker = findViewById(R.id.numberPicker_minute_addNotesFragment)
        amPmNumberPicker = findViewById(R.id.numberPicker_amPm_addNotesFragment)

        textInput.visibility = if (textInputVisibility == 0) View.GONE else View.VISIBLE
        alertSubtitle.visibility = if (alertSubtitleVisibility == 0) View.GONE else View.VISIBLE
        image_view.visibility = if (imageVisibility == 0) View.GONE else View.VISIBLE
        positive.setOnClickListener(this)
        negative.setOnClickListener(this)
        positive.text = positiveText
        negative.text = negativeText
        alertTitle.text = titleText
        alertSubtitle.text = subtitleText

        initTime()
        initPersianDate()
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
            maxValue = 60
            minValue = 0
            value = calendar.get(Calendar.MINUTE)
            displayedValues = values.toTypedArray()
        }
    }

    private fun initAmPm() {
        val values = mutableListOf("AM", "PM")
        amPmNumberPicker.apply {
            maxValue = 1
            minValue = 0
            value = calendar.get(Calendar.AM_PM)
            displayedValues = values.toTypedArray()
        }
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

    fun setLogoImg(image: Int) {
        imageVisibility = 1
        image_view.visibility = View.VISIBLE
        image_view.setImageResource(image)
    }

    fun getDate(): Long {
        val date = PersianDate().jalali_to_gregorian(
            yearNumberPicker.value,
            monthNumberPicker.value,
            dayNumberPicker.value
        )
        calendar.apply {
            set(Calendar.HOUR_OF_DAY, hourNumberPicker.value)
            set(Calendar.MINUTE, minuteNumberPicker.value)
            set(Calendar.AM_PM, amPmNumberPicker.value)
            set(Calendar.YEAR, date[0])
            set(Calendar.MONTH, date[1] - 1)
            set(Calendar.DAY_OF_MONTH, date[2])
        }
        return calendar.timeInMillis
    }

    override fun onClick(v: View) {
        if (v === positive) {
            dialogInterface!!.onPositiveClick()
        } else {
            dialogInterface!!.onNegativeClick()
        }
    }
}