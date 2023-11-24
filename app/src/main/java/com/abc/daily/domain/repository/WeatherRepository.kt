package com.abc.daily.domain.repository

import com.abc.daily.domain.model.weather.CurrentWeather
import retrofit2.Response

interface WeatherRepository {

    suspend fun getWeather(city: String?, location: Pair<String, String>?): Response<CurrentWeather>

}