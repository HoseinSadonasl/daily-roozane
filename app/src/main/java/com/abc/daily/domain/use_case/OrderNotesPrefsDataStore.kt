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

class OrderNotesPrefsDataStore @Inject constructor(
    private val appPreferencesRepository: AppPreferencesRepository
) {
    suspend fun invoke(orderType: String, noteOrder: String) {
        try {
            appPreferencesRepository.getDataStorePreferences().edit { preferences ->
                preferences[Constants.NOTES_ORDER] = setOf(orderType, noteOrder)
            }
        } catch (exception: IOException) {
            exception.printStackTrace()
        }
    }

    suspend fun invoke(): Flow<Set<String>> =
        appPreferencesRepository.getDataStorePreferences().data.catch {
            if (it is IOException) {
                it.printStackTrace()
                emit(emptyPreferences())
            } else {
                throw it
            }
        }.map { preferences ->
            preferences[Constants.NOTES_ORDER] ?: setOf(Constants.NOTES_ORDER_DSC, Constants.NOTES_ORDER_DATE)
        }

}
