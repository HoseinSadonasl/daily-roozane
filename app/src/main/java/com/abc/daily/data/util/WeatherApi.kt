package com.abc.daily.data.util

import com.abc.daily.domain.model.weather.CurrentWeather
import com.abc.daily.util.Constants
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface WeatherApi {

    @Headers("Content-Type: application/json; charset=utf-8")
    @GET("weather?appid=${Constants.API_KEY}")
    suspend fun getWeatherUsingCityName(
        @Query("q") cityName: String?,
        @Query("lat") lat: String?,
        @Query("lon") lon: String?,
    ): Response<CurrentWeather>

}