package com.abc.daily.ui.notes

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.location.LocationListenerCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.abc.daily.Adapters.NotesFragmentAdapter
import com.abc.daily.R
import com.abc.daily.databinding.LayoutNotesFragmentBinding
import com.abc.daily.util.Constants
import com.abc.daily.util.NoteSortDialog
import com.abc.daily.util.PermissionHelper
import com.bumptech.glide.RequestManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class NotesFragment : Fragment(), LocationListenerCompat {

    private lateinit var binding: LayoutNotesFragmentBinding
    private lateinit var notesAdapter: NotesFragmentAdapter
    private val viewModel: NotesViewModel by viewModels()

    @Inject
    lateinit var locationManager: LocationManager

    @Inject
    lateinit var glide: RequestManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.layout_notes_fragment,
            container,
            false
        )

        initUiComponents()
        initListeners()
        checkPermissions()
        observeDData()

        return binding.root
    }

    private fun initUiComponents() {
        binding.textViewScreenTitleNotesFragment.text = getString(R.string.app_name)
        notesAdapter = NotesFragmentAdapter() {
            navigateToNoteFragment(it)
        }
        binding.recyclerViewNotesListNotesFragment.apply {
            setNestedScrollingEnabled(true)
            adapter = notesAdapter
        }
    }

    private fun initListeners() {
        binding.fabAddNoteNotesFragment.setOnClickListener {
            navigateToNoteFragment()
        }

        binding.buttonSortNotesFragment.setOnClickListener {
            val dialog = NoteSortDialog(requireActivity()) { sort ->
                viewModel.getNotes(sort)
            }
            dialog.show()
        }
    }

    private fun checkPermissions() {
        val permissions = listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
        )
        if (!PermissionHelper.hasLocationPermission(requireContext())) {
            PermissionHelper.requestPermission(
                requireActivity(),
                permissions,
                PermissionHelper.ACCESS_COARSE_LOCATION_REQUEST_CODE
            )
        } else
            getCurrentLocation()
    }

    private fun observeDData() {
        observeNotes()
        observeWeather()
    }

    private fun observeWeather() {
        viewModel.weather.observe(viewLifecycleOwner) { currentWeather ->
            currentWeather.let {
                val temp = it.main.temp.toInt() - 273
                binding.textViewTempNotesFragment.text =
                    (getString(R.string.dgree_notesFragment, temp.toString()))
                glide.load(
                    Constants.ICON_URL + it.weather.get(0).icon + "@2x.png"
                ).into(binding.imageViewWeatherImageNotesFragment)
                binding.textViewLocationNotesFragment.text = it.name
            }
        }
    }

    private fun observeNotes() {
        viewModel.notesList.observe(viewLifecycleOwner) { notes ->
            notesAdapter.submitList(notes)
        }
    }

    private fun navigateToNoteFragment(id: Int = 0) =
        findNavController().navigate(NotesFragmentDirections.actionNotesFragmentToAddNoteFragment(id))

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PermissionHelper.ACCESS_COARSE_LOCATION_REQUEST_CODE -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getCurrentLocation()
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun getCurrentLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            locationManager.getCurrentLocation(LocationManager.NETWORK_PROVIDER, null, requireActivity().mainExecutor) { location ->
                val loc = location.latitude.toString() to location.longitude.toString()
                viewModel.getWeather("", location = loc)
            }
        } else {
            locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER,
                this, requireActivity().mainLooper
            )
        }
    }

    override fun onLocationChanged(location: Location) {
        val loc = location.latitude.toString() to location.longitude.toString()
        viewModel.getWeather("", location = loc)
    }

}