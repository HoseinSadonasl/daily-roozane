package com.abc.daily.ui.common

import android.app.UiModeManager
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.databinding.DataBindingUtil
import com.abc.daily.R
import com.abc.daily.databinding.LayoutMainBinding
import com.abc.daily.util.Constants
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var uiModeManager: UiModeManager

    private lateinit var binding: LayoutMainBinding
    private val commonViewModel: CommonViewModel by viewModels()
    private var keepSplashOnScreen = true
    private var isLunched: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeData()
        installSplashScreen().setKeepOnScreenCondition { keepSplashOnScreen }
    }


    private fun observeData() {
        observeColorTheme()
        observeDarkMode()
    }

    private fun observeDarkMode() {
        commonViewModel.isDarkMode.observe(this) { isDark ->
            setThemeDarkMode(isDark)
        }
    }

    private fun observeColorTheme() {
        commonViewModel.themeColorLiveData.observe(this) { themeColor ->
            when (themeColor) {
                Constants.THEME_PRIMARY -> setTheme(R.style.PrimaryTheme)
                Constants.THEME_BLUE -> setTheme(R.style.BlueTheme)
                Constants.THEME_GREEN -> setTheme(R.style.GreenTheme)
                Constants.THEME_RED -> setTheme(R.style.RedTheme)
                Constants.THEME_PURPLE -> setTheme(R.style.PurpleTheme)
                Constants.THEME_YELLOW -> setTheme(R.style.YellowTheme)
                else -> {}
            }
            if (isLunched) recreate()
            else initMainActivityView()
        }
    }

    private fun initMainActivityView() {
        binding = DataBindingUtil.setContentView(this, R.layout.layout_main)
        isLunched = true
        keepSplashOnScreen = false
    }

    fun setThemeColor(themeColor: Int) {
        commonViewModel.setThemeColor(themeColor)
    }

    fun requestMultiplePermissions(callBack: (Pair<String, Boolean>) -> Unit) =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            permissions.entries.forEach { callBack(it.key to it.value) }
        }

    private fun setThemeDarkMode(isNight: Boolean) {
        val modeUiModeManager =
            if (isNight) UiModeManager.MODE_NIGHT_YES else UiModeManager.MODE_NIGHT_NO
        val modeAppCompatDelegate =
            if (isNight) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            uiModeManager.setApplicationNightMode(modeUiModeManager)
        } else AppCompatDelegate.setDefaultNightMode(modeAppCompatDelegate)
    }
}
