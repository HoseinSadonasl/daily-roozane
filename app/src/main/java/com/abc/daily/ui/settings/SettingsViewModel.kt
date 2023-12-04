package com.abc.daily.ui.settings

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abc.daily.domain.use_case.PrefsDataStoreDomain
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModell @Inject constructor(
    private val appPrefsDataStoreDomain: PrefsDataStoreDomain
): ViewModel() {

    // TODO: stateFlow
    var isDarkMode:Boolean? = null

    init {
        getDarkMode()
    }

    fun getDarkMode() {
        viewModelScope.launch {
            appPrefsDataStoreDomain.darkModePrefsDataStore.invoke().onEach { isDark ->
                isDarkMode = isDark
            }.launchIn(viewModelScope)
        }
    }
    fun setDarkMode(isDarkMode: Boolean) {
        viewModelScope.launch {
            appPrefsDataStoreDomain.darkModePrefsDataStore.invoke(isDarkMode)
        }
    }

}