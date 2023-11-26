package com.abc.daily.di

import android.content.Context
import android.location.LocationManager
import com.abc.daily.data.repository.WeatherRepositoryImpl
import com.abc.daily.data.util.WeatherApi
import com.abc.daily.domain.repository.WeatherRepository
import com.abc.daily.domain.use_case.GetWeather
import com.abc.daily.domain.use_case.WeatherDomain
import com.abc.daily.util.Constants
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    
    @Provides
    @Singleton
    fun provideRetrofit(
    ): Retrofit = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun provideWeatherApi(
        retrofit: Retrofit
    ) = retrofit.create(WeatherApi::class.java)

    @Provides
    @Singleton
    fun provideGlide(
        @ApplicationContext context: Context
    ): RequestManager = Glide.with(context)

    @Provides
    @Singleton
    fun provideWeatherRepository(weatherApi: WeatherApi): WeatherRepository = WeatherRepositoryImpl(
        weatherApi = weatherApi
    )

    @Provides
    @Singleton
    fun providesWeatherUseCases(weatherRepository: WeatherRepository): WeatherDomain =
        WeatherDomain(
            getWeather = GetWeather(weatherRepository)
        )

    @Provides
    @Singleton
    fun provideFusedLocationProviderClient ( @ApplicationContext context: Context): FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)


}