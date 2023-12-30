package com.abc.daily.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.abc.daily.domain.repository.AppPreferencesRepository
import javax.inject.Inject

class AppPreferencesImpl @Inject constructor(
    private val appPrefs: DataStore<Preferences>
) : AppPreferencesRepository {

    override suspend fun getDataStorePreferences(): DataStore<Preferences> = appPrefs

}