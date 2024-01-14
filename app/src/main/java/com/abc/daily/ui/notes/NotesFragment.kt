package com.abc.daily.ui.notes

import android.Manifest
import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.abc.daily.Adapters.NotesFragmentAdapter
import com.abc.daily.Dialog
import com.abc.daily.R
import com.abc.daily.databinding.LayoutNotesFragmentBinding
import com.abc.daily.ui.add_note.AddNoteFragment
import com.abc.daily.ui.common.MainActivity
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

    companion object {
        const val REQUEST_INTERVAL_MILLIS: Long = 60000L
    }

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

    private fun showWelcomeDialog() {
        Dialog(
            requireContext(),
            onPositiveCallback = {
                viewModel.setFirstLunch(false)
                it.dismiss()
            }
        ).apply {
            setTitle(getString(R.string.welcomedialog_title))
            setDescription(getString(R.string.welcomedialog_decription) + "\n\n" + getString(R.string.welcomedialog_whatsnew))
            setDescriptionTextDirection(View.TEXT_ALIGNMENT_TEXT_START)
            setPositiveButtonText(getString(R.string.welcomedialog_btncontinue))
            setLogoImg(R.drawable.all_dailylogo_colored)
            show()
        }
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

        binding.layoutNotesfragmentCard.textViewLocationNotesFragment.setOnClickListener { showSearchCityDialog() }
    }

    @SuppressLint("MissingPermission")
    private fun showSearchCityDialog() {
        Dialog(requireContext(),
            onPositiveCallback = { dialog ->
                viewModel.saveDefaultCity("")
                getCurrentLocation()
                dialog.dismiss()
            },
            onNegativeCallback = { dialog ->
                updateWeather(dialog.textInput.text.toString())
                dialog.dismiss()
            }
        ).apply {
            setTitle(getString(R.string.dialog_citytextinputtitle))
            setLogoImg(R.drawable.all_location)
            setPositiveButtonText(getString(R.string.dialog_currentlocationbtn))
            setNegativeButtonText(getString(R.string.all_submit))
            textInput(getString(R.string.notesfragment_entercityname))
            setButtonsOrientation(LinearLayout.VERTICAL)
        }.show()
    }

    private fun updateWeather(cityName: String) {
        viewModel.fillWeatherParams(cityName, null)
    }

    private fun showOrderDialog() {
        val dialog = OrderDialog(requireActivity(), viewModel.orderNotes) { order ->
            viewModel.setOrderPrefs(order)
        }
        dialog.show()
    }

    private fun checkPermissions() {
        val permissions = mutableListOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            permissions.add(Manifest.permission.POST_NOTIFICATIONS)

        if (PermissionHelper.hasPermission(requireContext(), permissions).isEmpty().not()) {
            val activity = requireActivity() as MainActivity
            activity.requestMultiplePermissions { permission ->
                when (permission.first) {
                    Manifest.permission.ACCESS_COARSE_LOCATION -> {
                        if (permission.second) getCurrentLocation()
                    }

                    Manifest.permission.POST_NOTIFICATIONS -> {
                        if (!permission.second) {
                            Toast.makeText(requireContext(), getString(R.string.toast_notificationpermission), Toast.LENGTH_LONG).show()
                        }
                        viewModel.setFirstLunch(true)
                    }
                }
            }.launch(permissions.toTypedArray())
        } else getCurrentLocation()
    }

    private fun observeDData() {
        observefirtLunchLiveData()
        observeNotes()
        observeWeather()
    }

    private fun observefirtLunchLiveData() {
        viewModel.isFirstLunchLiveData.observe(viewLifecycleOwner) { isFirstLunch ->
            if (isFirstLunch) showWelcomeDialog()
        }
    }

    private fun observeWeather() {
        viewModel.weatherLiveData.observe(viewLifecycleOwner) { currentWeather ->
            currentWeather?.let {
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
        viewModel.notesListLiveData.observe(viewLifecycleOwner) { notes ->
            if (notes.isNotEmpty()) {
                binding.layoutNoNotesNotesFragment.visibility = View.GONE
                binding.recyclerViewNotesListNotesFragment.visibility = View.VISIBLE
                binding.editTextSearchNoteNotesFragment.visibility = View.VISIBLE
                notesAdapter.submitList(notes)
            } else {
                if (binding.textInputNotesFragment.text.isNullOrBlank()) {
                    binding.editTextSearchNoteNotesFragment.visibility = View.GONE
                }
                binding.recyclerViewNotesListNotesFragment.visibility = View.GONE
                binding.layoutNoNotesNotesFragment.visibility = View.VISIBLE
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
            .Builder(Priority.PRIORITY_BALANCED_POWER_ACCURACY, REQUEST_INTERVAL_MILLIS)
            .setMaxUpdates(10)
            .build()
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    locationResult.locations.map { location ->
                        Log.w(::getCurrentLocation.name, "getCurrentLocation: $location")
                        val loc = location.latitude.toString() to location.longitude.toString()
                        viewModel.fillWeatherParams(null, location = loc)
                    }
                }
            },
            requireActivity().mainLooper
        )
    }

}