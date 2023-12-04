package com.abc.daily.ui.settings

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.abc.daily.R
import com.abc.daily.databinding.LayoutSettingsFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : Fragment() {

    lateinit var binding: LayoutSettingsFragmentBinding
    private val settingsViewModel: SettingsViewModell by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.layout_settings_fragment, container, false)

        initUiComponents()
        observeData()


        return binding.root
    }

    private fun initUiComponents() {
        binding.buttonAddNoteBackward.setOnClickListener { findNavController().popBackStack() }

        binding.switchSettingsDarkmode.setOnCheckedChangeListener { compoundButton, isChecked ->
            setPrefsTheme(isChecked)
            setThemeDarkMode(isChecked)
        }

    }

    private fun setPrefsTheme(isDark: Boolean) = settingsViewModel.setDarkMode(isDark)

    private fun setThemeDarkMode(isNight: Boolean) {

        if (isNight) AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }

    private fun observeData() {
        settingsViewModel.isDarkMode?.let {
            binding.switchSettingsDarkmode.isChecked = it
        }

    }

}