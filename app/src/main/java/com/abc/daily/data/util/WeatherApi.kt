package com.abc.daily.data.util

import com.abc.daily.domain.model.weather.CurrentWeather
import com.abc.daily.util.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.QueryMap

interface WeatherApi {

    @Headers("Content-Type: application/json; charset=utf-8")
    @GET("weather")
    suspend fun getWeather(@QueryMap hashMap: HashMap<String, String>?): Response<CurrentWeather>

}