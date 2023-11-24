package com.abc.daily.data.repository

import com.abc.daily.data.util.WeatherApi
import com.abc.daily.domain.repository.WeatherRepository
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val weatherApi: WeatherApi
) : WeatherRepository {

    override suspend fun getWeather(city: String?, location: Pair<String, String>?) =
        weatherApi.getWeatherUsingCityName(city, location!!.first, location!!.second)

}