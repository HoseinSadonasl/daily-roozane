package com.abc.daily.util

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey

object Constants {

    const val DATASTORE_DAILY = "dailyappdata"
    val FIRST_LUNCH = booleanPreferencesKey("firstLunch")
    val DEFAULT_CITY = stringPreferencesKey("")
    val DARK_MODE = booleanPreferencesKey("nightMode")
    val NOTES_ORDER = stringSetPreferencesKey("order")
    val NOTES_ORDER_DSC = "dsc"
    val NOTES_ORDER_ASC = "asc"
    val NOTES_ORDER_DATE = "date"
    val NOTES_ORDER_NAME = "name"

    const val BASE_URL = "https://api.openweathermap.org/data/2.5/"
    const val ICON_URL = "https://openweathermap.org/img/wn/"
    const val API_KEY = "6c7b0789e344c8bdd8f0935ff4568e72"

}