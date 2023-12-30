package com.abc.daily.ui.main

import android.app.UiModeManager
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.abc.daily.R
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var uiModeManager: UiModeManager

    private val mainViewModel: MainViewModel by viewModels()
    private var keepSplashOnScreen = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().setKeepOnScreenCondition { keepSplashOnScreen }
        setContentView(R.layout.layout_note_fragment)
        observeData()
    }

    private fun observeData() {
        mainViewModel.isDarkMode.observe(this) {isDark ->
           setThemeDarkMode(isDark)
        }
        keepSplashOnScreen = false
    }


    fun requestMultiplePermissions(callBack: (Pair<String, Boolean>) -> Unit) = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
        permissions.entries.forEach { callBack(it.key to it.value) }
    }

    private fun setThemeDarkMode(isNight: Boolean) {
        val modeUiModeManager = if (isNight) UiModeManager.MODE_NIGHT_YES else UiModeManager.MODE_NIGHT_NO
        val modeAppCompatDelegate = if (isNight) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            uiModeManager.setApplicationNightMode(modeUiModeManager)
        } else AppCompatDelegate.setDefaultNightMode(modeAppCompatDelegate)
    }
}
