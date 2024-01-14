package com.abc.daily.ui.settings

import android.app.UiModeManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.abc.daily.R
import com.abc.daily.databinding.LayoutSettingsFragmentBinding
import com.abc.daily.ui.main.MainActivity
import com.abc.daily.ui.main.MainViewModel
import com.abc.daily.util.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SettingsFragment : Fragment() {

    @Inject
    lateinit var uiModeManager: UiModeManager

    private lateinit var mainActivity: MainActivity

    lateinit var binding: LayoutSettingsFragmentBinding
    private val settingsViewModel: SettingsViewModell by viewModels()
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.layout_settings_fragment, container, false)

        mainActivity = requireActivity() as MainActivity

        val ui = requireContext().getSystemService(Context.UI_MODE_SERVICE) as UiModeManager
        binding.switchSettingsDarkmode.isChecked = ui.nightMode == UiModeManager.MODE_NIGHT_YES
        initUiComponents()
        observeData()


        return binding.root
    }

    private fun initUiComponents() {
        binding.buttonAddNoteBackward.setOnClickListener { findNavController().popBackStack() }

        binding.switchSettingsDarkmode.setOnCheckedChangeListener { compoundButton, isChecked ->
            setPrefsTheme(isChecked)
        }

        binding.tvSettingsReportbug.setOnClickListener { openEmailApp() }

        binding.tvSettingsConnect.setOnClickListener { openEmailApp() }

        initThemeRadioButtons(mainActivity.applicationInfo.theme)
        initChangeThemeRadioButtons()

    }

    private fun initThemeRadioButtons(currentTheme: Any) {
        when(currentTheme) {
            R.style.PrimaryTheme -> binding.rbSettingsfragmentTealrb.isChecked = true
            R.style.BlueTheme -> binding.rbSettingsfragmentBluerb.isChecked = true
            R.style.RedTheme -> binding.rbSettingsfragmentRedrb.isChecked = true
            R.style.YellowTheme -> binding.rbSettingsfragmentYellowrb.isChecked = true
            R.style.GreenTheme -> binding.rbSettingsfragmentGreenrb.isChecked = true
            R.style.PurpleTheme -> binding.rbSettingsfragmentPurplerb.isChecked = true
            else -> {}
        }
    }

    private fun initChangeThemeRadioButtons() {

        binding.rbSettingsfragmentBluerb.setOnClickListener { rb ->
            if (binding.rbSettingsfragmentBluerb.isChecked)  mainActivity.setThemeColor(Constants.THEME_BLUE)
        }

        binding.rbSettingsfragmentPurplerb.setOnClickListener { rb  ->
            if (binding.rbSettingsfragmentPurplerb.isChecked) mainActivity.setThemeColor(Constants.THEME_PURPLE)
        }

        binding.rbSettingsfragmentGreenrb.setOnClickListener { rb  ->
            if (binding.rbSettingsfragmentGreenrb.isChecked) mainActivity.setThemeColor(Constants.THEME_GREEN)
        }

        binding.rbSettingsfragmentRedrb.setOnClickListener { rb  ->
            if (binding.rbSettingsfragmentRedrb.isChecked) mainActivity.setThemeColor(Constants.THEME_RED)
        }

        binding.rbSettingsfragmentTealrb.setOnClickListener { rb  ->
            if ( binding.rbSettingsfragmentTealrb.isChecked) mainActivity.setThemeColor(Constants.THEME_PRIMARY)
        }

        binding.rbSettingsfragmentYellowrb.setOnClickListener { rb  ->
            if (binding.rbSettingsfragmentYellowrb.isChecked) mainActivity.setThemeColor(Constants.THEME_YELLOW)
        }
        
    }

    private fun openEmailApp() {
        Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:roozaneapp@gmail.com")
            startActivity(Intent.createChooser(this, getString(R.string.send_mail)))
        }
    }

    private fun setPrefsTheme(isDark: Boolean)  {
        settingsViewModel.setDarkMode(isDark)
    }

    private fun observeData() {
        observeDarkMode()
        observeThemeColor()
    }

    private fun observeDarkMode() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                settingsViewModel.isDarkMode.collect { isDark ->
                    binding.switchSettingsDarkmode.isChecked = isDark
                }
            }
        }
    }

    private fun observeThemeColor() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                settingsViewModel.currentTheme.collect { themeColor ->
//                    initThemeRadioButtons(themeColor)
                }
            }
        }
    }

}