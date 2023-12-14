package com.abc.daily.domain.use_case

import android.location.Location
import com.abc.daily.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class GetWeather @Inject constructor(
    private val weatherRepository: WeatherRepository
) {

    suspend operator fun invoke(hashMap: HashMap<String, String>?) = flow {
        try {
            emit(weatherRepository.getWeather(hashMap))
        } catch (exeption: IOException) {
            exeption.printStackTrace()
        }
    }

}
