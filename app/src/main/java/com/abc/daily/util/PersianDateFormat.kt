package com.abc.daily.util

import java.text.ParseException
import java.text.SimpleDateFormat


/**
 * Created by SamanZaman(saman.zamani1@gmail.com) on 3/31/2017 AD.
 *
 * Last update on Sunday, November 17, 2021
 */
class PersianDateFormat {
    //number Format
    enum class PersianDateNumberCharacter {
        ENGLISH, FARSI
    }
    //variable
    /**
     * Key for convert Date to String
     */
    private val key = arrayOf(
        "a", "l", "j", "F", "Y", "H", "i", "s", "d", "g", "n", "m", "t", "w", "y",
        "z", "A",
        "L", "X", "C", "E", "P", "Q", "R"
    )

    //from version 1.3.3 pattern is public and has de
    private var pattern = "l j F Y H:i:s"
    private var numberCharacter = PersianDateNumberCharacter.ENGLISH

    /**
     * key_parse for convert String to PersianDate
     *
     * yyyy = Year (1396) MM = month (02-12-...) dd = day (13-02-15-...) HH = Hour (13-02-15-...) mm =
     * minutes (13-02-15-...) ss = second (13-02-15-...)
     */
    private val key_parse = arrayOf("yyyy", "MM", "dd", "HH", "mm", "ss")

    /**
     * Constructor for create formatter with just pattern
     */
    constructor(pattern: String) {
        this.pattern = pattern
    }

    /**
     * Constructor for create pattern with number character format
     *
     * @param pattern pattern use for format
     * @param numberCharacter character type can be PersianDateNumberCharacter.FARSI | PersianDateNumberCharacter.English
     */
    constructor(pattern: String, numberCharacter: PersianDateNumberCharacter) {
        this.pattern = pattern
        this.numberCharacter = numberCharacter
    }

    /**
     * Change pattern
     *
     * @param pattern change format pattern
     */
    fun setPattern(pattern: String) {
        this.pattern = pattern
    }

    /**
     * Change number character
     *
     * @param numberCharacter number character
     */
    fun setNumberCharacter(
        numberCharacter: PersianDateNumberCharacter
    ) {
        this.numberCharacter = numberCharacter
    }

    /**
     * Constructor without pattern
     */
    constructor() {}

    fun format(date: PersianDate): String {
        val year2: String
        year2 = if (("" + date.shYear).length == 2) {
            "" + date.shYear
        } else if (("" + date.shYear).length == 3) {
            ("" + date.shYear).substring(2, 3)
        } else {
            ("" + date.shYear).substring(2, 4)
        }
        val values = arrayOf(
            if (date.isMidNight) "ق.ظ" else "ب.ظ", date.dayName(), "" + date.shDay,
            date.monthName(),
            "" + date.shYear,
            textNumberFilter("" + date.hour), textNumberFilter("" + date.minute),
            textNumberFilter("" + date.second),
            textNumberFilter("" + date.shDay), "" + date.hour, "" + date.shMonth,
            textNumberFilter("" + date.shMonth),
            "" + date.monthDays, "" + date.dayOfWeek(), year2, "" + date.dayInYear,
            date.timeOfTheDay,
            if (date.isLeap) "1" else "0",
            date.AfghanMonthName(),
            date.KurdishMonthName(),
            date.PashtoMonthName(),
            date.FinglishMonthName(),
            date.dayFinglishName(),
            date.dayEnglishName()
        )
        if (numberCharacter == PersianDateNumberCharacter.FARSI) {
            farsiCharacter(values)
        }
        return stringUtils(pattern, key, values)
    }
    /**
     * Parse Jallali date from String
     *
     * @param date date in string
     * @param pattern pattern
     */
    /**
     * Parse Jallali date from String
     *
     * @param date date in string
     */
    @JvmOverloads
    @Throws(ParseException::class)
    fun parse(date: String, pattern: String = this.pattern): PersianDate {
        val JalaliDate = object : ArrayList<Int>() {
            init {
                add(0)
                add(0)
                add(0)
                add(0)
                add(0)
                add(0)
            }
        }
        for (i in key_parse.indices) {
            if (pattern.contains(key_parse[i])) {
                val start_temp = pattern.indexOf(key_parse[i])
                val end_temp = start_temp + key_parse[i].length
                val dateReplace = date.substring(start_temp, end_temp)
                if (dateReplace.matches("[-+]?\\d*\\.?\\d+".toRegex())) {
                    JalaliDate[i] = dateReplace.toInt()
                } else {
                    throw ParseException("Parse Exception", 10)
                }
            }
        }
        return PersianDate()
            .initJalaliDate(
                JalaliDate[0], JalaliDate[1], JalaliDate[2], JalaliDate[3],
                JalaliDate[4], JalaliDate[5]
            )
    }
    /**
     * Convert String Grg date to persian date object
     *
     * @param date date String
     * @param pattern pattern
     * @return PersianDate object
     */
    /**
     * Convert String Grg date to persian date object
     *
     * @param date date in String
     * @return PersianDate object
     */
    @JvmOverloads
    @Throws(ParseException::class)
    fun parseGrg(date: String?, pattern: String? = this.pattern): PersianDate {
        val dateInGrg = SimpleDateFormat(pattern).parse(date)
        return PersianDate(dateInGrg.time)
    }

    /**
     * Replace String
     *
     * @param text String
     * @param key Looking for
     * @param values Replace with
     */
    private fun stringUtils(text: String, key: Array<String>, values: Array<String>): String {
        var text = text
        for (i in key.indices) {
            text = text.replace(key[i], values[i])
        }
        return text
    }

    /**
     * add zero to start
     *
     * @param date data
     * @return return string with 0 in start
     */
    private fun textNumberFilter(date: String): String {
        return if (date.length < 2) {
            "0$date"
        } else date
    }

    companion object {
        /**
         * Convert with charter type
         *
         * @param date date in PersianDate object
         * @param pattern pattern
         * @param numberFormatCharacter number charter
         * @return return date
         */
        /**
         * Format date
         *
         *
         * @param date PersianDate object of date
         * @param pattern Pattern you want to show
         * @return date in pattern
         */
        @JvmOverloads
        fun format(
            date: PersianDate,
            pattern: String?,
            numberFormatCharacter: PersianDateNumberCharacter = PersianDateNumberCharacter.ENGLISH
        ): String? {
            var pattern = pattern
            if (pattern == null) pattern = "l j F Y H:i:s"
            val key = arrayOf(
                "a", "l", "j", "F", "Y", "H", "i", "s", "d", "g", "n", "m", "t", "w", "y", "z",
                "A", "L", "X", "C", "E", "P", "Q", "R"
            )
            val year2: String
            year2 = if (("" + date.shYear).length == 2) {
                "" + date.shYear
            } else if (("" + date.shYear).length == 3) {
                ("" + date.shYear).substring(2, 3)
            } else {
                ("" + date.shYear).substring(2, 4)
            }
            val values = arrayOf(
                date.shortTimeOfTheDay, date.dayName(), "" + date.shDay,
                date.monthName(),
                "" + date.shYear,
                textNumberFilterStatic("" + date.hour), textNumberFilterStatic("" + date.minute),
                textNumberFilterStatic("" + date.second),
                textNumberFilterStatic("" + date.shDay), "" + date.hour, "" + date.shMonth,
                textNumberFilterStatic("" + date.shMonth),
                "" + date.monthDays, "" + date.dayOfWeek(), year2, "" + date.dayInYear,
                date.timeOfTheDay,
                if (date.isLeap) "1" else "0",
                date.AfghanMonthName(),
                date.KurdishMonthName(),
                date.PashtoMonthName(),
                date.FinglishMonthName(),
                date.dayFinglishName(),
                date.dayEnglishName()
            )
            if (numberFormatCharacter == PersianDateNumberCharacter.FARSI) {
                farsiCharacter(values)
            }
            for (i in key.indices) {
                pattern = pattern!!.replace(key[i], values[i])
            }
            return pattern
        }

        fun textNumberFilterStatic(date: String): String {
            return if (date.length < 2) {
                "0$date"
            } else date
        }

        /**
         * Convert English characters to Farsi characters
         *
         * @param values a string array of values
         * @return a converted string array
         */
        fun farsiCharacter(values: Array<String>): Array<String> {
            val persianChars = arrayOf("۰", "۱", "۲", "٣", "۴", "۵", "۶", "۷", "۸", "٩")
            val englishChars = arrayOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9")
            for (i in values.indices) {
                var tmpValue = values[i]
                for (j in persianChars.indices) {
                    tmpValue = tmpValue.replace(englishChars[j].toRegex(), persianChars[j])
                }
                values[i] = tmpValue
            }
            return values
        }
    }
}