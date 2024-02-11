package com.abc.daily.util

import android.content.Context
import com.abc.daily.R
import com.abc.daily.util.PersianDateFormat.PersianDateNumberCharacter
import java.util.Locale

object DateUtil {

    private val isToday: String = " امروز"
    private val datePattern = "Y F d"
    private val fullDatePattern = "l j F Y"
    private val fullLatinDatePattern = "Y-m-d"
    private val timePattern = "H:i"
    private val fullDateTimePattern = "Y/m/d H:i"

    fun toPersianDate(timestamp: String): String {
        val persianDate = PersianDate(timestamp.toLong())
        return formattedDateTime(persianDate, datePattern)
    }

    private fun fullPersianDatePattern(): String = if (Locale.getDefault().language == "fa") fullDatePattern else fullLatinDatePattern

    fun toFullPersianDate(timestamp: String): String {
        val persianDate = PersianDate(timestamp.toLong())
        val pattern = fullPersianDatePattern()
        return formattedDateTime(persianDate, pattern)
    }

    fun toPersianTime(timestamp: String): String {
        val persianDate = PersianDate(timestamp.toLong())
        return formattedDateTime(persianDate, timestamp)
    }

    fun toPersianDateAndTime(milliseconds: String, context: Context): String {
        val persianDate = PersianDate(milliseconds.toLong())
        return if (persianDate.isToday) {
            return if (persianDate.untilToday()[1].toInt() <= 59) {
                context.getString(R.string.txt_minutes_ago, withPersianOrLatinDigits(persianDate.untilToday()[2]))
            } else {
                context.getString(R.string.txt_hours_ago, withPersianOrLatinDigits(persianDate.untilToday()[1]))
            }
        } else formattedDateTime(persianDate, fullDateTimePattern)
    }
    fun alarmToPersianDateAndTime(milliseconds: String): String {
        val persianDate = PersianDate(milliseconds.toLong())
        return  formattedDateTime(persianDate, fullDateTimePattern)
    }

    private fun formattedDateTime(persianDate: PersianDate, format: String): String =
        PersianDateFormat(format, persianDateNumberCharacterFormat()).format(persianDate)

    private fun persianDateNumberCharacterFormat(): PersianDateNumberCharacter {
        return if (Locale.getDefault().language == "fa") {
            PersianDateNumberCharacter.FARSI
        } else PersianDateNumberCharacter.ENGLISH
    }

}