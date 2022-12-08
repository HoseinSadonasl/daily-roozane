package com.abc.daily.data.repository

import com.abc.daily.data.util.WeatherApi
import com.abc.daily.domain.repository.WeatherRepository
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val weatherApi: WeatherApi
) : WeatherRepository {

    override suspend fun getWeather(location: String) =
        weatherApi.getWeatherUsingCityName(location)

}