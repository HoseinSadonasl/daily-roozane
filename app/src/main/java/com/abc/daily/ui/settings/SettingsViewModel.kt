package com.abc.daily.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abc.daily.domain.use_case.PrefsDataStoreDomain
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModell @Inject constructor(
    private val appPrefsDataStoreDomain: PrefsDataStoreDomain
): ViewModel() {

    var isDarkMode = MutableSharedFlow<Boolean>()
    var currentTheme = MutableSharedFlow<Int>()

    init {
        getDarkMode()
        getThemeMode()
    }

    fun getDarkMode() {
        viewModelScope.launch {
            appPrefsDataStoreDomain.themePrefsDataStore.invoke().onEach { isDark ->
                isDarkMode.emit(isDark)
            }.launchIn(viewModelScope)
        }
    }

    fun getThemeMode() {
        viewModelScope.launch {
            appPrefsDataStoreDomain.themePrefsDataStore.invokeThemeColor().onEach { themeColor ->
                currentTheme.emit(themeColor)
            }.launchIn(viewModelScope)
        }
    }

    fun setDarkMode(isDarkMode: Boolean) {
        viewModelScope.launch {
            appPrefsDataStoreDomain.themePrefsDataStore.invoke(isDarkMode)
        }
    }

}