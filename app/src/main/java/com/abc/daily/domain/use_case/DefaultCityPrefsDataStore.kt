package com.abc.daily.domain.use_case

import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import com.abc.daily.domain.repository.AppPreferencesRepository
import com.abc.daily.util.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

class DefaultCityPrefsDataStore @Inject constructor(
    private val appPreferencesRepository: AppPreferencesRepository
) {
    suspend fun invoke(city: String) {
        try {
            appPreferencesRepository.getDataStorePreferences().edit { preferences ->
                preferences[Constants.DEFAULT_CITY] = city
            }
        } catch (exception: IOException) {
            exception.printStackTrace()
        }
    }

    suspend fun invoke(): Flow<String> =
        appPreferencesRepository.getDataStorePreferences().data.catch {
            if (it is IOException) {
                it.printStackTrace()
                emit(emptyPreferences())
            } else {
                throw it
            }
        }.map { preferences ->
            preferences[Constants.DEFAULT_CITY] ?: ""
        }

}
