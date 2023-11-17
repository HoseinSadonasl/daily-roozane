package com.abc.daily.ui.notes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.abc.daily.Adapters.NotesFragmentAdapter
import com.abc.daily.R
import com.abc.daily.databinding.LayoutNotesFragmentBinding
import com.abc.daily.interfaces.DialogInterface
import com.abc.daily.util.Constants
import com.abc.daily.util.DateTimePickerDialog
import com.bumptech.glide.RequestManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class NotesFragment : Fragment(), DialogInterface {

    private lateinit var dialog: DateTimePickerDialog
    private lateinit var binding: LayoutNotesFragmentBinding
    private lateinit var notesAdapter: NotesFragmentAdapter
    private val viewModel: NotesViewModel by viewModels()

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
        observeDData()

        return binding.root
    }

    private fun initUiComponents() {
        binding.textViewScreenTitleNotesFragment.text = getString(R.string.app_name)
        notesAdapter = NotesFragmentAdapter(){
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

    private fun navigateToNoteFragment(id: Int = 0) = findNavController().navigate(NotesFragmentDirections.actionNotesFragmentToAddNoteFragment(id))


    override fun onPositiveClick() {
        dialog.getDate()
    }

    override fun onNegativeClick() {
        TODO("Not yet implemented")
    }

}