package com.abc.daily.util

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by Saman Zaman(saman.zamani1@gmail.com) on 3/31/2017 AD.
 *
 * Last update on Monday, May 11, 2021
 */
class PersianDate {
    /**
     * return time in long value
     *
     * @return Value of time in mile
     */
    /*----- Define Variable ---*/
    var time: Long
        private set
    var shYear = 0
        private set
    var shMonth = 0
        private set
    var shDay = 0
        private set
    var grgYear = 0
        private set
    var grgMonth = 0
        private set
    var grgDay = 0
        private set
    var hour = 0
        private set
    var minute = 0
        private set
    var second = 0
        private set

    //endregion
    //region Setter & Getters
    /*---- Setter And getter ----*/
    var locale = Locale.getDefault()
        private set

    enum class Dialect {
        FINGLISH, AFGHAN, IRANIAN, KURDISH, PASHTO
    }

    /**
     * Contractor
     */
    constructor() {
        time = Date().time
        init()
    }

    /**
     * Contractor
     */
    constructor(timeInMilliSecond: Long) {
        time = timeInMilliSecond
        init()
    }

    /**
     * Contractor
     */
    constructor(date: Date) {
        time = date.time
        init()
    }



    //region CONST
    private val dayNames = arrayOf(
        "شنبه", "یک‌شنبه", "دوشنبه", "سه‌شنبه", "چهارشنبه", "پنج‌شنبه",
        "جمعه"
    )
    private val dayFinglishNames = arrayOf(
        "Shanbe", "Yekshanbe", "Doshanbe", "Seshanbe", "Chaharshanbe", "Panjshanbe",
        "Jom'e"
    )
    private val dayEnglishNames = arrayOf(
        "Saturday", "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday",
        "Friday"
    )
    private val monthNames = arrayOf(
        "فروردین", "اردیبهشت", "خرداد", "تیر", "مرداد", "شهریور",
        "مهر", "آبان", "آذر", "دی", "بهمن", "اسفند"
    )
    private val FinglishMonthNames = arrayOf(
        "Farvardin", "Ordibehesht", "Khordad", "Tir", "Mordad", "Shahrivar", "Mehr",
        "Aban", "Azar", "Day", "Bahman", "Esfand"
    )
    private val AfghanMonthNames = arrayOf(
        "حمل", "ثور", "جوزا", "سرطان", "اسد", "سنبله", "میزان",
        "عقرب", "قوس", "جدی", "دلو", "حوت"
    )
    private val KurdishMonthNames = arrayOf(
        "جیژنان", "گولان", "زه ردان", "په رپه ر", "گه لاویژ",
        "نوخشان", "به ران", "خه زان", "ساران", "بفران", "به ندان", "رمشان"
    )
    private val PashtoMonthNames = arrayOf(
        "وری", "غويی", "غبرګولی", "چنګاښ", "زمری", "وږی",
        "تله", "لړم", "ليندۍ", "مرغومی", "سلواغه", "كب"
    )

    fun setLocal(locale: Locale): PersianDate {
        this.locale = locale
        return this
    }

    fun setShYear(shYear: Int): PersianDate {
        this.shYear = shYear
        changeTime(true)
        return this
    }

    fun setShMonth(shMonth: Int): PersianDate {
        this.shMonth = shMonth
        changeTime(true)
        return this
    }

    fun setShDay(shDay: Int): PersianDate {
        this.shDay = shDay
        changeTime(true)
        return this
    }

    fun setGrgYear(grgYear: Int): PersianDate {
        this.grgYear = grgYear
        changeTime(false)
        return this
    }

    fun setGrgMonth(grgMonth: Int): PersianDate {
        this.grgMonth = grgMonth
        changeTime(false)
        return this
    }

    fun setGrgDay(grgDay: Int): PersianDate {
        this.grgDay = grgDay
        changeTime(false)
        return this
    }

    fun setHour(hour: Int): PersianDate {
        this.hour = hour
        changeTime(false)
        return this
    }

    fun setMinute(minute: Int): PersianDate {
        this.minute = minute
        changeTime(false)
        return this
    }

    fun setSecond(second: Int): PersianDate {
        this.second = second
        changeTime(false)
        return this
    }
    /**
     * init with Grg data
     *
     * @param year Year in Grg
     * @param month Month in Grg
     * @param day day in Grg
     * @param hour hour
     * @param minute min
     * @param second second
     * @return PersianDate
     */
    /**
     * init without time
     *
     * @param year Year in Grg
     * @param month Month in Grg
     * @param day Day in Grg
     * @return persianDate
     */
    @JvmOverloads
    fun initGrgDate(
        year: Int,
        month: Int,
        day: Int,
        hour: Int = 0,
        minute: Int = 0,
        second: Int = 0
    ): PersianDate {
        grgYear = year
        grgMonth = month
        grgDay = day
        this.hour = hour
        this.minute = minute
        this.second = second
        changeTime(false)
        return this
    }
    /**
     * initialize date from Jallali date
     *
     * @param year Year in jallali date
     * @param month Month in Jallali date
     * @param day day in Jallali date
     * @param hour Hour
     * @param minute Minute
     * @param second Second
     * @return PersianDate
     */
    /**
     * initialize date from Jallali date
     *
     * @param year Year in Jallali date
     * @param month Month in Jallali date
     * @param day day in Jallali date
     * @return PersianDate
     */
    @JvmOverloads
    fun initJalaliDate(
        year: Int, month: Int, day: Int, hour: Int = 0, minute: Int = 0,
        second: Int = 0
    ): PersianDate {
        shYear = year
        shMonth = month
        shDay = day
        this.hour = hour
        this.minute = minute
        this.second = second
        changeTime(true)
        return this
    }

    /**
     * Check Grg year is leap
     *
     * @param Year Year
     * @return boolean
     */
    @JvmOverloads
    fun grgIsLeap(Year: Int = grgYear): Boolean {
        if (Year % 4 == 0) {
            return if (Year % 100 == 0) {
                Year % 400 == 0
            } else true
        }
        return false
    }

    /**
     * Check year in Leap
     *
     * @return true or false
     */
    val isLeap: Boolean
        get() = isLeap(shYear)

    /**
     * Check custom year is leap
     *
     * @param year int year
     * @return true or false
     */
    fun isLeap(year: Int): Boolean {
        val referenceYear = 1375.0
        var startYear = 1375.0
        val yearRes = year - referenceYear
        //first of all make sure year is not multiplier of 1375
        if (yearRes == 0.0 || yearRes % 33 == 0.0) {
            return true //year is 1375 or 1375+-(i)*33
        }
        if (yearRes > 0) {
            if (yearRes > 33) {
                val numb = yearRes / 33
                startYear = referenceYear + Math.floor(numb) * 33
            }
        } else {
            if (yearRes > -33) {
                startYear = referenceYear - 33
            } else {
                val numb = Math.abs(yearRes / 33)
                startYear = referenceYear - Math.ceil(numb) * 33
            }
        }
        val leapYears = doubleArrayOf(
            startYear, startYear + 4, startYear + 8, startYear + 12, startYear + 16, startYear + 20,
            startYear + 24, startYear + 28, startYear + 33
        )
        return Arrays.binarySearch(leapYears, year.toDouble()) >= 0
    }

    /**
     * Author: JDF.SCR.IR =>> Download Full Version :  http://jdf.scr.ir/jdf License: GNU/LGPL _ Open
     * Source & Free :: Version: 2.80 : [2020=1399]
     */
    fun gregorian_to_jalali(gy: Int, gm: Int, gd: Int): IntArray {
        val out = intArrayOf(
            if (gm > 2) gy + 1 else gy,
            0,
            0
        )
        run {
            val g_d_m: IntArray = intArrayOf(0, 31, 59, 90, 120, 151, 181, 212, 243, 273, 304, 334)
            out[2] =
                (355666 + (365 * gy) + ((out.get(0) + 3) / 4) - ((out.get(0) + 99) / 100)
                        ) + ((out.get(0) + 399) / 400) + gd + g_d_m.get(gm - 1)
        }
        out[0] = -1595 + 33 * (out[2] / 12053)
        out[2] %= 12053
        out[0] += 4 * (out[2] / 1461)
        out[2] %= 1461
        if (out[2] > 365) {
            out[0] += ((out[2] - 1) / 365)
            out[2] = (out[2] - 1) % 365
        }
        if (out[2] < 186) {
            out[1] = 1 + (out[2] / 31)
            out[2] = 1 + out[2] % 31
        } else {
            out[1] = 7 + ((out[2] - 186) / 30)
            out[2] = 1 + (out[2] - 186) % 30
        }
        return out
    }

    override fun toString(): String {
        return PersianDateFormat.format(this, null)?:"jjjj"
    }
    /**
     * Author: JDF.SCR.IR =>> Download Full Version :  http://jdf.scr.ir/jdf License: GNU/LGPL _ Open
     * Source & Free :: Version: 2.80 : [2020=1399]
     */
    fun jalali_to_gregorian(jy: Int, jm: Int, jd: Int): IntArray {
        var jy = jy
        jy += 1595
        val out = intArrayOf(
            0,
            0,
            -355668 + 365 * jy + (jy / 33) * 8 + ((jy % 33 + 3) / 4) + jd + if (jm < 7) (jm - 1) * 31 else (jm - 7) * 30 + 186
        )
        out[0] = 400 * (out[2] / 146097)
        out[2] %= 146097
        if (out[2] > 36524) {
            out[0] += 100 * (--out[2] / 36524)
            out[2] %= 36524
            if (out[2] >= 365) {
                out[2]++
            }
        }
        out[0] += 4 * (out[2] / 1461)
        out[2] %= 1461
        if (out[2] > 365) {
            out[0] += ((out[2] - 1) / 365)
            out[2] = (out[2] - 1) % 365
        }
        val sal_a = intArrayOf(
            0, 31, if (out[0] % 4 == 0 && out[0] % 100 != 0 || out[0] % 400 == 0) 29 else 28,
            31, 30, 31, 30, 31, 31, 30, 31, 30, 31
        )
        out[2]++
        while (out[1] < 13 && out[2] > sal_a[out[1]]) {
            out[2] -= sal_a[out[1]]
            out[1]++
        }
        return out
    }
    /**
     * Get day of week from PersianDate object
     *
     * @param date persianDate
     * @return int
     */
    /**
     * calc day of week
     *
     * @return int
     */
    @JvmOverloads
    fun dayOfWeek(date: PersianDate = this): Int {
        return this.dayOfWeek(date.toDate())
    }

    /**
     * Get day of week from Date object
     *
     * @param date Date
     * @return int
     */
    fun dayOfWeek(date: Date?): Int {
        val cal = Calendar.getInstance()
        cal.time = date
        return if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
            0
        } else (cal.get(Calendar.DAY_OF_WEEK))
    }

    fun getWeek(date: PersianDate): ArrayList<PersianDate> {
        val currentWeek = ArrayList<PersianDate>()
        for (i in 0 until date.dayOfWeek()) {
            val dateTmp = PersianDate(date.time)
            currentWeek.add(dateTmp.subDays(date.dayOfWeek() - i))
        }
        currentWeek.add(date)
        val threshold = 7 - currentWeek.size
        for (j in 1..threshold) {
            val dateTmp = PersianDate(date.time)
            currentWeek.add(dateTmp.addDay(j.toLong()))
        }
        return currentWeek
    }

    val week: Array<PersianDate>
        get() = getWeek(this).toTypedArray()
    /**
     * Return list of month
     *
     * @param dialect dialect
     * @return month names
     */
    /**
     * Return list of month
     *
     * @return month names
     */
    @JvmOverloads
    fun monthList(dialect: Dialect? = Dialect.IRANIAN): Array<String> {
        when (dialect) {
            Dialect.FINGLISH -> return FinglishMonthNames
            Dialect.AFGHAN -> return AfghanMonthNames
            Dialect.KURDISH -> return KurdishMonthNames
            Dialect.PASHTO -> return PashtoMonthNames
            else -> return monthNames
        }
    }
    /**
     * return month name
     *
     * @return string
     */
    /**
     * Get current month name in Persian
     */
    @JvmOverloads
    fun monthName(dialect: Dialect? = Dialect.IRANIAN): String {
        return monthName(shMonth, dialect)
    }

    /**
     * Return month name
     *
     * @param month Month
     */
    fun monthName(month: Int, dialect: Dialect?): String {
        when (dialect) {
            Dialect.FINGLISH -> return FinglishMonthNames[month - 1]
            Dialect.AFGHAN -> return AfghanMonthNames[month - 1]
            Dialect.KURDISH -> return KurdishMonthNames[month - 1]
            Dialect.PASHTO -> return PashtoMonthNames[month - 1]
            else -> return monthNames[month - 1]
        }
    }
    /**
     * Get month name in Finglish
     */
    /**
     * Get current date Finglish month name
     */
    @JvmOverloads
    fun FinglishMonthName(month: Int = shMonth): String {
        return FinglishMonthNames[month - 1]
    }
    /**
     * Get month name in Afghan
     */
    /**
     * Get current date Afghan month name
     */
    @JvmOverloads
    fun AfghanMonthName(month: Int = shMonth): String {
        return AfghanMonthNames[month - 1]
    }
    /**
     * Get month name in Kurdish
     */
    /**
     * Get current date Kurdish month name
     */
    @JvmOverloads
    fun KurdishMonthName(month: Int = shMonth): String {
        return KurdishMonthNames[month - 1]
    }
    /**
     * Get month name in Pashto
     */
    /**
     * Get current date Pashto month name
     */
    @JvmOverloads
    fun PashtoMonthName(month: Int = shMonth): String {
        return PashtoMonthNames[month - 1]
    }
    /**
     * Get Day Name
     */
    /**
     * get day name
     */
    @JvmOverloads
    fun dayName(date: PersianDate = this): String {
        return dayNames[this.dayOfWeek(date)]
    }
    /**
     * Get Finglish Day Name
     */
    /**
     * Get Finglish Day Name
     */
    @JvmOverloads
    fun dayFinglishName(date: PersianDate = this): String {
        return dayFinglishNames[this.dayOfWeek(date)]
    }
    /**
     * Get English Day Name
     */
    /**
     * Get English Day Name
     */
    @JvmOverloads
    fun dayEnglishName(date: PersianDate = this): String {
        return dayEnglishNames[this.dayOfWeek(date)]
    }

    /**
     * Number days of month
     *
     * @return return days
     */
    val monthDays: Int
        get() = getMonthDays(shYear, shMonth)

    /**
     * calc count of day in month
     */
    fun getMonthDays(Year: Int, month: Int): Int {
        if (month == 12 && !isLeap(Year)) {
            return 29
        }
        return if (month <= 6) {
            31
        } else {
            30
        }
    }

    /**
     * calculate day in year
     */
    val dayInYear: Int
        get() = getDayInYear(shMonth, shDay)

    /**
     * Calc day of the year
     *
     * @param month Month
     * @param day Day
     */
    fun getDayInYear(month: Int, day: Int): Int {
        var day = day
        for (i in 1 until month) {
            if (i <= 6) {
                day += 31
            } else {
                day += 30
            }
        }
        return day
    }
    /**
     * Subtract date
     *
     * @param SubYear Number of subtraction years
     * @param SubMonth Number of subtraction months
     * @param SubDay Number of subtraction year days
     * @param SubHour Number of subtraction year Hours
     * @param SubMinute Number of subtraction year minutes
     * @param SubSecond Number of subtraction year seconds
     *
     * @return this
     */
    /**
     * Subtract date
     *
     * @param SubYear Number of subtraction years
     * @param SubMonth Number of subtraction months
     * @param SubDay Number of subtraction year days
     *
     * @return this
     */
    @JvmOverloads
    fun subDate(
        SubYear: Long, SubMonth: Long, SubDay: Long, SubHour: Long = 0, SubMinute: Long = 0,
        SubSecond: Long = 0
    ): PersianDate {
        //sub Days
        var SubYear = SubYear
        var SubMonth = SubMonth
        var dayMustDecrease = SubDay
        //check if month bigger than a year
        if (SubMonth >= 12) {
            SubYear += Math.floor(SubMonth / 12.0).toLong()
            SubMonth = SubMonth % 12
        }
        //sub year
        for (i in 1..SubYear) {
            if (isLeap((shYear - i).toInt())) {
                dayMustDecrease += 366
            } else {
                dayMustDecrease += 365
            }
        }

        //sub month
        var tmpYear = shYear - SubYear.toInt()
        var tmpMonth = shMonth
        for (i in 0 until SubMonth) {
            tmpMonth -= 1
            if (tmpMonth <= 0) {
                tmpYear -= 1
                tmpMonth = 12
            }
            dayMustDecrease += this.getMonthLength(tmpYear, tmpMonth).toLong()
        }
        time -= dayMustDecrease * 86400000L + SubHour * 3600000 + SubMinute * 60000 + (SubSecond
                * 1000)
        init()
        return this
    }

    /**
     * Subtract more than one year
     *
     * @param years: Number of subtraction years
     *
     * @return this
     */
    fun subYears(years: Int): PersianDate {
        return subDate(years.toLong(), 0, 0)
    }

    /**
     * Subtract a year
     *
     * @return this
     */
    fun subYear(): PersianDate {
        return subYears(1)
    }

    /**
     * Subtract months
     *
     * @param months: Number of subtraction months
     * @return this
     */
    fun subMonths(months: Int): PersianDate {
        return subDate(0, months.toLong(), 0)
    }

    /**
     * Subtract a month
     *
     * @return this
     */
    fun subMonth(): PersianDate {
        return subMonths(1)
    }

    /**
     * Subtract days
     *
     * @param days: Number of subtraction days
     *
     * @return this
     */
    fun subDays(days: Int): PersianDate {
        return subDate(0, 0, days.toLong())
    }

    /**
     * Subtract a day
     *
     * @return this
     */
    fun subDay(): PersianDate {
        return subDays(1)
    }

    /**
     * Subtract hours
     *
     * @param hours: Number of subtraction hours
     * @return this
     */
    fun subHours(hours: Int): PersianDate {
        return subDate(0, 0, 0, hours.toLong(), 0, 0)
    }

    /**
     * Subtract an hour
     *
     * @return this
     */
    fun subHour(): PersianDate {
        return subHours(1)
    }

    /**
     * Subtract minutes
     *
     * @param minutes: Number of subtraction minutes
     * @return this
     */
    fun subMinutes(minutes: Int): PersianDate {
        return subDate(0, 0, 0, 0, minutes.toLong(), 0)
    }

    /**
     * Subtract a minute
     *
     * @return this
     */
    fun subMinute(): PersianDate {
        return subMinutes(1)
    }

    /**
     * Subtract Seconds
     *
     * @param seconds: Number of subtraction seconds
     * @return this
     */
    fun subSeconds(seconds: Int): PersianDate {
        return subDate(0, 0, 0, 0, 0, seconds.toLong())
    }

    /**
     * Subtract a Second
     *
     * @return this
     */
    fun subSecond(): PersianDate {
        return subSeconds(1)
    }

    /**
     * add date
     *
     * @param AddYear Number of Year you want add
     * @param AddMonth Number of month you want add
     * @param AddDay Number of day you want add
     * @param AddHour Number of hour you want add
     * @param AddMinute Number of minute you want add
     * @param AddSecond Number of second you want add
     * @return new date
     */
    fun addDate(
        AddYear: Long, AddMonth: Long, AddDay: Long, AddHour: Long, AddMinute: Long,
        AddSecond: Long
    ): PersianDate {
        //add Days
        var AddYear = AddYear
        var AddMonth = AddMonth
        var dayMustIncrease = AddDay
        //check if month bigger than a year
        if (AddMonth >= 12) {
            AddYear += Math.floor(AddMonth / 12.0).toLong()
            AddMonth = AddMonth % 12
        }
        //add year
        for (i in 0 until AddYear) {
            if (isLeap((shYear + i).toInt())) {
                dayMustIncrease += 366
            } else {
                dayMustIncrease += 365
            }
        }
        //add month
        var tmpYear = shYear - AddYear.toInt()
        var tmpMonth = shMonth
        for (i in 0 until AddMonth) {
            dayMustIncrease += this.getMonthLength(tmpYear, tmpMonth).toLong()
            tmpMonth += 1
            if (tmpMonth >= 13) {
                tmpYear += 1
                tmpMonth = 1
            }
        }
        time += dayMustIncrease * 86400000L + AddHour * 3600000 + AddMinute * 60000 + (AddSecond
                * 1000)
        init()
        return this
    }

    /**
     * add to date
     *
     * @param year Number of Year you want add
     * @param month Number of month you want add
     * @param day Number of day you want add
     */
    fun addDate(year: Long, month: Long, day: Long): PersianDate {
        return this.addDate(year, month, day, 0, 0, 0)
    }

    fun addYear(year: Long): PersianDate {
        return this.addDate(year, 0, 0)
    }

    fun addMonth(month: Long): PersianDate {
        return this.addDate(0, month, 0)
    }

    fun addWeek(week: Long): PersianDate {
        return this.addDate(0, 0, week * 7)
    }

    fun addDay(day: Long): PersianDate {
        return this.addDate(0, 0, day)
    }

    /**
     * Compare 2 date
     *
     * @param dateInput PersianDate type
     */
    fun after(dateInput: PersianDate): Boolean {
        return time < dateInput.time
    }

    /**
     * compare to data
     *
     * @param dateInput Input
     */
    fun before(dateInput: PersianDate): Boolean {
        return !after(dateInput)
    }

    /**
     * Check date equals
     */
    fun equals(dateInput: PersianDate): Boolean {
        return time == dateInput.time
    }

    /**
     * compare two data
     *
     * @return 0 = equal,1=data1 > anotherDate,-1=data1 > anotherDate
     */
    operator fun compareTo(anotherDate: PersianDate): Int {
        return time.compareTo(anotherDate.time)
    }

    /**
     * Return Day in different date
     */
    val dayUntilToday: Long
        get() = getDayUntilToday(PersianDate())

    /**
     * Return different just day in compare 2 date
     *
     * @param date date for compare
     */
    fun getDayUntilToday(date: PersianDate): Long {
        val ret = untilToday(date)
        return ret[0]
    }
    /**
     * calculate different between 2 date
     *
     * @param date Date 1
     */
    /**
     * Calc different date until now
     */
    @JvmOverloads
    fun untilToday(date: PersianDate = PersianDate()): LongArray {
        val secondsInMilli: Long = 1000
        val minutesInMilli = secondsInMilli * 60
        val hoursInMilli = minutesInMilli * 60
        val daysInMilli = hoursInMilli * 24
        var different = Math.abs(time - date.time)
        val elapsedDays = different / daysInMilli
        different = different % daysInMilli
        val elapsedHours = different / hoursInMilli
        different = different % hoursInMilli
        val elapsedMinutes = different / minutesInMilli
        different = different % minutesInMilli
        val elapsedSeconds = different / secondsInMilli
        return longArrayOf(elapsedDays, elapsedHours, elapsedMinutes, elapsedSeconds)
    }

    /**
     * convert PersianDate class to date
     */
    fun toDate(): Date {
        return Date(time)
    }
    /**
     * Get start of day
     */
    /**
     * Get Start of day
     */
    @JvmOverloads
    fun startOfDay(persianDate: PersianDate = this): PersianDate {
        persianDate.setHour(0).setMinute(0).setSecond(0)
        return persianDate
    }
    /**
     * Get end of day
     */
    /**
     * Get end of day
     */
    @JvmOverloads
    fun endOfDay(persianDate: PersianDate = this): PersianDate {
        persianDate.setHour(23).setMinute(59).setSecond(59)
        return persianDate
    }

    /**
     * Check midnight
     */
    fun isMidNight(persianDate: PersianDate): Boolean {
        return persianDate.isMidNight
    }

    /**
     * Check is midNight
     */
    val isMidNight: Boolean
        get() = (hour < 12)

    /**
     * Get short name time of the day
     */
    val shortTimeOfTheDay: String
        get() = if ((isMidNight)) AM_SHORT_NAME else PM_SHORT_NAME

    /**
     * Get short name time of the day
     */
    fun getShortTimeOfTheDay(persianDate: PersianDate): String {
        return if (persianDate.isMidNight) AM_SHORT_NAME else PM_SHORT_NAME
    }

    /**
     * Get time of the day
     */
    val timeOfTheDay: String
        get() = if ((isMidNight)) AM_NAME else PM_NAME

    /**
     * Get time of the day
     */
    fun getTimeOfTheDay(persianDate: PersianDate): String {
        return if (persianDate.isMidNight) AM_NAME else PM_NAME
    }

    /**
     * Get number of days in month
     *
     * @param year Jalali year
     * @param month Jalali month
     * @return number of days in month
     */
    fun getMonthLength(year: Int, month: Int): Int {
        if (month <= 6) {
            return 31
        } else if (month <= 11) {
            return 30
        } else {
            return if (isLeap(year)) {
                30
            } else {
                29
            }
        }
    }

    /**
     * Get number of days in month
     *
     * @param persianDate persianDate object
     * @return number of days in month
     */
    fun getMonthLength(persianDate: PersianDate): Int {
        return this.getMonthLength(persianDate.shYear, persianDate.shMonth)
    }

    /**
     * Get number of days in month
     *
     * @return number of days in month
     */
    val monthLength: Int
        get() = this.getMonthLength(this)
    //endregion
    /*----- Helper Function-----*/ //region Private functions
    /**
     * Helper function
     */
    private fun textNumberFilter(date: String): String {
        return if (date.length < 2) {
            "0$date"
        } else date
    }

    private fun init() {
        grgYear = SimpleDateFormat("yyyy", locale).format(time).toInt()
        grgMonth = SimpleDateFormat("MM", locale).format(time).toInt()
        grgDay = SimpleDateFormat("dd", locale).format(time).toInt()
        hour = SimpleDateFormat("HH", locale).format(time).toInt()
        minute = SimpleDateFormat("mm", locale).format(time).toInt()
        second = SimpleDateFormat("ss", locale).format(time).toInt()
        changeTime(false)
    }

    private fun changeTime(isJalaliChanged: Boolean) {
        if (isJalaliChanged) {
            TimeCalcFromJalali(
                shYear, shMonth, shDay, hour, minute,
                second
            )
        } else {
            TimeCalcFromGrg(
                grgYear, grgMonth, grgDay, hour, minute,
                second
            )
        }
    }

    private fun TimeCalcFromJalali(year: Int, month: Int, day: Int, hr: Int, min: Int, sec: Int) {
        val grgTimes =
            intArrayOf(0 /*YEAR*/, 0 /*MONTH*/, 0 /*DAY*/, 0 /*HOUR*/, 0 /*MINUTE*/, 0 /*SECOND*/)
        val jalaliTimes = intArrayOf(
            year /*YEAR*/, month /*MONTH*/, day /*DAY*/, hr /*HOUR*/, min /*MINUTE*/, sec /*SECOND*/
        )
        //convert timestamp to grg date
        val convertedTime = jalali_to_gregorian(year, month, day)
        grgTimes[0] = convertedTime[0]
        grgTimes[1] = convertedTime[1]
        grgTimes[2] = convertedTime[2]
        grgTimes[3] = hr
        grgTimes[4] = min
        grgTimes[5] = sec
        notify(grgTimes, jalaliTimes)
    }

    private fun TimeCalcFromGrg(year: Int, month: Int, day: Int, hr: Int, min: Int, sec: Int) {
        val grgTimes = intArrayOf(
            year /*YEAR*/, month /*MONTH*/, day /*DAY*/, hr /*HOUR*/, min /*MINUTE*/, sec /*SECOND*/
        )
        val jalaliTimes =
            intArrayOf(0 /*YEAR*/, 0 /*MONTH*/, 0 /*DAY*/, 0 /*HOUR*/, 0 /*MINUTE*/, 0 /*SECOND*/)
        val convertedTime = gregorian_to_jalali(year, month, day)
        jalaliTimes[0] = convertedTime[0]
        jalaliTimes[1] = convertedTime[1]
        jalaliTimes[2] = convertedTime[2]
        jalaliTimes[3] = hr
        jalaliTimes[4] = min
        jalaliTimes[5] = sec
        notify(grgTimes, jalaliTimes)
    }

    private fun updateTimeStamp() {
        try {
            time = Objects
                .requireNonNull(
                    SimpleDateFormat("dd/MM/yyyy HH:mm:ss", locale)
                        .parse(
                            "" + grgDay + "/" + grgMonth + "/" + grgYear + " " + hour
                                    + ":" + minute + ":" + second
                        )
                )
                .time //reported in #https://github.com/samanzamani/PersianDate/issues/54
        } catch (e: ParseException) {
            time = Date().time
        }
    }

    private fun notify(grg: IntArray, jalali: IntArray) {
        grgYear = grg[0]
        grgMonth = grg[1]
        grgDay = grg[2]
        shYear = jalali[0]
        shMonth = jalali[1]
        shDay = jalali[2]
        hour = jalali[3]
        minute = jalali[4]
        second = jalali[5]
        updateTimeStamp()
    } //endregion

    companion object {
        val FARVARDIN = 1
        val ORDIBEHEST = 2
        val KHORDAD = 3
        val TIR = 4
        val MORDAD = 5
        val SHAHRIVAR = 6
        val MEHR = 7
        val ABAN = 8
        val AZAR = 9
        val DAY = 10
        val BAHMAN = 11
        val ESFAND = 12
        val AM = 1
        val PM = 2
        val AM_SHORT_NAME = "ق.ظ"
        val PM_SHORT_NAME = "ب.ظ"
        val AM_NAME = "قبل از ظهر"
        val PM_NAME = "بعد از ظهر"

        /**
         * Check static is leap year for Jalali Date
         *
         * @param year Jalali year
         * @return true if year is leap
         */
        fun isJalaliLeap(year: Int): Boolean {
            return (PersianDate().isLeap(year))
        }

        /**
         * Check static is leap year for Grg Date
         *
         * @param year Year
         * @return boolean
         */
        fun isGrgLeap(year: Int): Boolean {
            return (PersianDate().grgIsLeap(year))
        }

        /**
         * Return today
         */
        fun today(): PersianDate {
            val persianDate = PersianDate()
            persianDate.setHour(0).setMinute(0).setSecond(0)
            return persianDate
        }

        /**
         * Get tomorrow
         */
        fun tomorrow(): PersianDate {
            val persianDate = PersianDate()
            persianDate.addDay(1)
            persianDate.setHour(0).setMinute(0).setSecond(0)
            return persianDate
        }
    }
}