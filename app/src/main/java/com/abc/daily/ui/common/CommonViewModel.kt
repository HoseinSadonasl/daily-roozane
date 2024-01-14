package com.abc.daily.ui.common

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
class CommonViewModel @Inject constructor(
    private val appPrefsDataStoreDomain: PrefsDataStoreDomain
): ViewModel()  {

    val isDarkMode = MutableLiveData<Boolean>()
    val themeColorLiveData = MutableLiveData<Int>()

    init {
        getDarkMode()
        getThemeColor()
    }


    fun getDarkMode() {
        viewModelScope.launch {
            appPrefsDataStoreDomain.themePrefsDataStore.invoke().onEach { isDark ->
                isDarkMode.postValue(isDark)
            }.launchIn(viewModelScope)
        }
    }

    fun setDarkMode(isDarkMode: Boolean) {
        viewModelScope.launch {
            appPrefsDataStoreDomain.themePrefsDataStore.invoke(isDarkMode)
        }
    }

    fun setThemeColor(themeColor: Int) {
        viewModelScope.launch {
            appPrefsDataStoreDomain.themePrefsDataStore.invoke(themeColor)
        }
    }

    fun getThemeColor() {
        viewModelScope.launch {
            appPrefsDataStoreDomain.themePrefsDataStore.invokeThemeColor().onEach { themeColor ->
                themeColorLiveData.postValue(themeColor)
            }.launchIn(viewModelScope)
        }
    }

}