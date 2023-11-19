package com.abc.daily.util

object DateUtil {

    private val datePattern = "Y F d"
    private val timePattern = "H:i"
    private val fullDateTimePattern = "Y/m/d H:i"

    fun toPersianDate(timestamp: String): String {
        val persianDate = PersianDate(timestamp.toLong())
        val formattedDate = PersianDateFormat(datePattern).format(persianDate)
        return formattedDate
    }

    fun toPersianTime(timestamp: String): String {
        val persianDate = PersianDate(timestamp.toLong())
        val formattedDate = PersianDateFormat(timePattern).format(persianDate)
        return formattedDate
    }

    fun toPersianDateAndTime(timestamp: String): String {
        val persianDate = PersianDate(timestamp.toLong())
        val formattedDate = PersianDateFormat(fullDateTimePattern).format(persianDate)
        return formattedDate
    }

}