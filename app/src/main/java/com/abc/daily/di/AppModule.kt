package com.abc.daily.di

import android.app.AlarmManager
import android.app.UiModeManager
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.abc.daily.data.repository.AppPreferencesImpl
import com.abc.daily.data.repository.WeatherRepositoryImpl
import com.abc.daily.data.util.WeatherApi
import com.abc.daily.domain.repository.AppPreferencesRepository
import com.abc.daily.domain.repository.WeatherRepository
import com.abc.daily.domain.use_case.ThemePrefsDataStore
import com.abc.daily.domain.use_case.DefaultCityPrefsDataStore
import com.abc.daily.domain.use_case.FirstLunchPrefsDataStore
import com.abc.daily.domain.use_case.GetWeather
import com.abc.daily.domain.use_case.OrderNotesPrefsDataStore
import com.abc.daily.domain.use_case.PrefsDataStoreDomain
import com.abc.daily.domain.use_case.WeatherDomain
import com.abc.daily.ui.common.CommonViewModel
import com.abc.daily.util.Constants
import com.abc.daily.util.NotificationUtil
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
    fun provideDataStore(
        @ApplicationContext context: Context
    ) = PreferenceDataStoreFactory.create{ context.preferencesDataStoreFile(Constants.DATASTORE_DAILY) }


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
    fun provideAlarmManager(
        @ApplicationContext context: Context
    ): AlarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    @Provides
    @Singleton
    fun provideUiModeManager(
        @ApplicationContext context: Context
    ): UiModeManager =context.getSystemService(Context.UI_MODE_SERVICE) as UiModeManager


    @Provides
    @Singleton
    fun provideNotificationUtil(
        @ApplicationContext context: Context
    ): NotificationUtil = NotificationUtil(context)

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
    fun providesAppPreferencesRepository(appPref: DataStore<Preferences>): AppPreferencesRepository = AppPreferencesImpl(appPref)

    @Provides
    @Singleton
    fun providesAppPreferencesUseCases(appPreferencesRepository: AppPreferencesRepository): PrefsDataStoreDomain = PrefsDataStoreDomain(
        orderNotesPrefsDataStore = OrderNotesPrefsDataStore(appPreferencesRepository),
        themePrefsDataStore = ThemePrefsDataStore(appPreferencesRepository),
        defaultCityPrefsDataStore = DefaultCityPrefsDataStore(appPreferencesRepository),
        firstLunchPrefsDataStore = FirstLunchPrefsDataStore(appPreferencesRepository)
    )

    @Provides
    @Singleton
    fun provideFusedLocationProviderClient ( @ApplicationContext context: Context): FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

    @Provides
    @Singleton
    fun provideCommonViewModel (appPrefsDataStoreDomain: PrefsDataStoreDomain): CommonViewModel = CommonViewModel(appPrefsDataStoreDomain)


}