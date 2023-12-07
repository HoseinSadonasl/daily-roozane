package com.abc.daily.ui.main

import android.app.Activity
import android.app.ActivityOptions
import android.app.UiModeManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.abc.daily.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
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

    private fun setThemeDarkMode(isNight: Boolean) {
        val modeUiModeManager = if (isNight) UiModeManager.MODE_NIGHT_YES else UiModeManager.MODE_NIGHT_NO
        val modeAppCompatDelegate = if (isNight) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            uiModeManager.setApplicationNightMode(modeUiModeManager)
        } else AppCompatDelegate.setDefaultNightMode(modeAppCompatDelegate)
    }
}
