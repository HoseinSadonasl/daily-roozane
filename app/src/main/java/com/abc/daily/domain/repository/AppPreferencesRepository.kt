package com.abc.daily.domain.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.flow.Flow

interface AppPreferencesRepository {

    suspend fun getDataStorePreferences(): DataStore<Preferences>

}