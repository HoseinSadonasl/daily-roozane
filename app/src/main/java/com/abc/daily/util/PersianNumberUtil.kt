package com.abc.daily.util

import java.util.Locale

val Number.withPersianDigits: String
    get() = "$this".withPersianDigits

val String.withPersianDigits: String
    get() = StringBuilder().also { builder ->
        toCharArray().forEach {
            builder.append(
                when {
                    Character.isDigit(it) -> PERSIAN_DIGITS["$it".toInt()]
                    it == '.' -> "/"
                    else -> it
                }
            )
        }
    }.toString()

private val PERSIAN_DIGITS = charArrayOf(
    '0' + 1728,
    '1' + 1728,
    '2' + 1728,
    '3' + 1728,
    '4' + 1728,
    '5' + 1728,
    '6' + 1728,
    '7' + 1728,
    '8' + 1728,
    '9' + 1728
)

fun withPersianOrLatinDigits(number: Number): String {
    return if (Locale.getDefault().language == "fa") {
        number.withPersianDigits
    } else number.toString()
}