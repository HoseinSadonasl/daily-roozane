package com.abc.daily.domain.use_case

import com.abc.daily.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class GetWeather @Inject constructor(
    private val weatherRepository: WeatherRepository
) {

    suspend operator fun invoke(location: String) = flow {
        try {
            emit(weatherRepository.getWeather(location))
        } catch (exeption: IOException) {
            // TODO: handle exeptions
        }
    }

}
