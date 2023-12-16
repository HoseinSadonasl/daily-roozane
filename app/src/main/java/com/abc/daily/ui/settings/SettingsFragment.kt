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
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SettingsFragment : Fragment() {

    @Inject
    lateinit var uiModeManager: UiModeManager

    lateinit var binding: LayoutSettingsFragmentBinding
    private val settingsViewModel: SettingsViewModell by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.layout_settings_fragment, container, false)

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
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                settingsViewModel.isDarkMode.collect { isDark ->
                    binding.switchSettingsDarkmode.isChecked = isDark
                }
            }
        }
    }

}