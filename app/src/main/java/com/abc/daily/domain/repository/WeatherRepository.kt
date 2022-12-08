package com.abc.daily.domain.repository

import com.abc.daily.domain.model.weather.CurrentWeather
import retrofit2.Response

interface WeatherRepository {

    suspend fun getWeather(location: String): Response<CurrentWeather>

}