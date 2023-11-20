package com.abc.daily.util

import android.content.Context
import com.abc.daily.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

object DateUtil {

    private val isToday: String = " امروز"
    private val datePattern = "Y F d"
    private val timePattern = "H:i"
    private val fullDateTimePattern = "Y/m/d H:i"

    fun toPersianDate(timestamp: String): String {
        val persianDate = PersianDate(timestamp.toLong())
        return formattedDateTime(persianDate, datePattern)
    }

    fun toPersianTime(timestamp: String): String {
        val persianDate = PersianDate(timestamp.toLong())
        return formattedDateTime(persianDate, timestamp)
    }

    fun toPersianDateAndTime(timestamp: String, context: Context): String {
        val persianDate = PersianDate(timestamp.toLong())
        if (persianDate.isToday) {
            return  context.getString(R.string.txt_today, formattedDateTime(persianDate, timePattern))
        } else return formattedDateTime(persianDate, fullDateTimePattern)
    }

    private fun formattedDateTime(persianDate: PersianDate, format: String): String = PersianDateFormat(format).format(persianDate)

}