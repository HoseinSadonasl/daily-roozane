package com.abc.daily.domain.use_case

import android.location.Location
import com.abc.daily.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class GetWeather @Inject constructor(
    private val weatherRepository: WeatherRepository
) {

    suspend operator fun invoke(city: String?, location: Pair<String, String>?) = flow {
        try {
            emit(weatherRepository.getWeather(city, location))
        } catch (exeption: IOException) {
            // TODO: handle exeptions
        }
    }

}
