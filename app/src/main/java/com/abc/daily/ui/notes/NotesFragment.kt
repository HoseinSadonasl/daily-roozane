package com.abc.daily.ui.notes

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.databinding.adapters.TextViewBindingAdapter.OnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.abc.daily.Adapters.NotesFragmentAdapter
import com.abc.daily.R
import com.abc.daily.databinding.LayoutNotesFragmentBinding
import com.abc.daily.ui.add_note.AddNoteFragment
import com.abc.daily.util.Constants
import com.abc.daily.util.DateUtil
import com.abc.daily.util.OrderDialog
import com.abc.daily.util.PermissionHelper
import com.bumptech.glide.RequestManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import dagger.hilt.android.AndroidEntryPoint
import java.util.Date
import javax.inject.Inject


@AndroidEntryPoint
class NotesFragment : Fragment() {

    private lateinit var binding: LayoutNotesFragmentBinding
    private lateinit var notesAdapter: NotesFragmentAdapter
    private val viewModel: NotesViewModel by viewModels()

    private lateinit var locationRequest: LocationRequest

    @Inject
    lateinit var glide: RequestManager

    @Inject
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

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
        initIntentData()
        observeDData()

        return binding.root
    }

    private fun initIntentData() {
        if (requireActivity().intent.hasExtra(AddNoteFragment.NOTE_ARGUMENT))
           requireActivity().intent.extras?.run {
               val id = getInt(AddNoteFragment.NOTE_ARGUMENT)
               navigateToNoteFragment(id)
           }
    }

    private fun initUiComponents() {
        initDate()
        binding.textViewScreenTitleNotesFragment.text = getString(R.string.app_name)
        notesAdapter = NotesFragmentAdapter() {
            navigateToNoteFragment(it)
        }
        binding.recyclerViewNotesListNotesFragment.apply {
            setNestedScrollingEnabled(true)
            adapter = notesAdapter
        }
        initTextInput()
    }

    private fun initTextInput() {
        binding.textInputNotesFragment.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.orderNotes(text.toString())
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {}
        })



    }

    private fun initDate() {
        val date = DateUtil.toFullPersianDate(Date().time.toString())
        binding.layoutNotesfragmentCard.textViewDateNotesFragment.text = date

    }

    private fun initListeners() {
        binding.fabAddNoteNotesFragment.setOnClickListener {
            navigateToNoteFragment()
        }

        binding.buttonSortNotesFragment.setOnClickListener {
            showOrderDialog()
        }

        binding.layoutNoNotesNotesFragment.setOnClickListener { navigateToNoteFragment() }

        binding.buttonSettingsNotesFragment.setOnClickListener { navigateToSettingsFragment() }
    }

    private fun showOrderDialog() {
        val dialog = OrderDialog(requireActivity(), viewModel.orderNotes) { order ->
            viewModel.setOrderPrefs(order)
        }
        dialog.show()
    }

    private fun checkPermissions() {
        val permissions = listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
        )
        if (!PermissionHelper.hasLocationPermission(requireContext())) {
            PermissionHelper.requestPermission(permissions, registerForActivityResult(ActivityResultContracts.RequestPermission()) {isGranted->
                if (isGranted) {
                    getCurrentLocation()
                }
            })
        } else getCurrentLocation()
    }

    private fun observeDData() {
        observeNotes()
        observeWeather()
    }

    private fun observeWeather() {
        viewModel.weather.observe(viewLifecycleOwner) { currentWeather ->
            currentWeather.let {
                val temp = it.main.temp.toInt() - 273
                binding.layoutNotesfragmentCard.textViewTempNotesFragment.text =
                    (getString(R.string.dgree_notesFragment, temp.toString()))
                glide.load(
                    Constants.ICON_URL + it.weather.get(0).icon + "@2x.png"
                ).into(binding.layoutNotesfragmentCard.imageViewWeatherImageNotesFragment)
                binding.layoutNotesfragmentCard.textViewLocationNotesFragment.text = it.name
            }
        }
    }

    private fun observeNotes() {
        viewModel.notesList.observe(viewLifecycleOwner) { notes ->
            if (notes.isNotEmpty()) {
                binding.recyclerViewNotesListNotesFragment.visibility = View.VISIBLE
                binding.fabAddNoteNotesFragment.visibility = View.VISIBLE
                notesAdapter.submitList(notes)
                binding.layoutNoNotesNotesFragment.visibility = View.GONE
            } else {
                binding.layoutNoNotesNotesFragment.apply {
                    visibility = View.VISIBLE
                }
            }
        }
    }

    private fun navigateToNoteFragment(id: Int = 0) =
        findNavController().navigate(NotesFragmentDirections.actionNotesFragmentToAddNoteFragment(id))

    private fun navigateToSettingsFragment() =
        findNavController().navigate(NotesFragmentDirections.actionNotesFragmentToSettingsFragment())

    @SuppressLint("MissingPermission")
    private fun getCurrentLocation() {
        locationRequest = LocationRequest
            .Builder(Priority.PRIORITY_BALANCED_POWER_ACCURACY,10000L)
            .setMaxUpdates(10)
            .build()
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.locations.map { location ->
                    Log.w(::getCurrentLocation.name, "getCurrentLocation: $location")
                    val loc = location.latitude.toString() to location.longitude.toString()
                    viewModel.getWeather("", location = loc)
                }
            }
        }, requireActivity().mainLooper)
    }
}