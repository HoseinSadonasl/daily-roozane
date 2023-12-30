package com.abc.daily.util

import android.content.Context
import com.abc.daily.R

object DateUtil {

    private val isToday: String = " امروز"
    private val datePattern = "Y F d"
    private val fullDatePattern = "l j F Y"
    private val timePattern = "H:i"
    private val fullDateTimePattern = "Y/m/d H:i"

    fun toPersianDate(timestamp: String): String {
        val persianDate = PersianDate(timestamp.toLong())
        return formattedDateTime(persianDate, datePattern)
    }
    fun toFullPersianDate(timestamp: String): String {
        val persianDate = PersianDate(timestamp.toLong())
        return formattedDateTime(persianDate, fullDatePattern)
    }

    fun toPersianTime(timestamp: String): String {
        val persianDate = PersianDate(timestamp.toLong())
        return formattedDateTime(persianDate, timestamp)
    }

    fun toPersianDateAndTime(milliseconds: String, context: Context): String {
        val persianDate = PersianDate(milliseconds.toLong())
        return if (persianDate.isToday) {
            return if (persianDate.untilToday()[1].toInt() <= 59) {
                context.getString(R.string.txt_minutes_ago, (persianDate.untilToday()[2]).toString())
            } else {
                context.getString(R.string.txt_hours_ago, (persianDate.untilToday()[1]).toString())
            }
        } else formattedDateTime(persianDate, fullDateTimePattern)
    }
    fun alarmToPersianDateAndTime(milliseconds: String): String {
        val persianDate = PersianDate(milliseconds.toLong())
        return  formattedDateTime(persianDate, fullDateTimePattern)
    }

    private fun formattedDateTime(persianDate: PersianDate, format: String): String =
        PersianDateFormat(format).format(persianDate)

}