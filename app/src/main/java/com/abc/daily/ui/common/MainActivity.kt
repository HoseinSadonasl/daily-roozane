package com.abc.daily.ui.common

import android.Manifest
import android.app.UiModeManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowInsetsControllerCompat
import androidx.databinding.DataBindingUtil
import com.abc.daily.R
import com.abc.daily.databinding.LayoutMainBinding
import com.abc.daily.util.Constants
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    var resultLauncher: ActivityResultLauncher<Array<String>> =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            permissions.entries.forEach { permission ->
                when (permission.key) {
                    Manifest.permission.ACCESS_COARSE_LOCATION -> {
                        if (permission.value) commonViewModel.fillLocationPermissionLiveData(permission.value)
                    }
                    Manifest.permission.POST_NOTIFICATIONS -> {
                        if (!permission.value) {
                            Toast.makeText(this, getString(R.string.toast_notificationpermission), Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }

    @Inject
    lateinit var uiModeManager: UiModeManager

    @Inject
    lateinit var commonViewModel: CommonViewModel

    private lateinit var binding: LayoutMainBinding
    private var keepSplashOnScreen = true
    private var isLunched: Boolean = false
    private var isDark: Boolean = false

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
            this.isDark = isDark
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
            WindowInsetsControllerCompat(window, binding.root).isAppearanceLightStatusBars = !isDark
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

    private fun setThemeDarkMode(isNight: Boolean) {
        val modeUiModeManager = if (isNight) UiModeManager.MODE_NIGHT_YES else UiModeManager.MODE_NIGHT_NO
        val modeAppCompatDelegate = if (isNight) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            uiModeManager.setApplicationNightMode(modeUiModeManager)
        } else AppCompatDelegate.setDefaultNightMode(modeAppCompatDelegate)
    }
}
