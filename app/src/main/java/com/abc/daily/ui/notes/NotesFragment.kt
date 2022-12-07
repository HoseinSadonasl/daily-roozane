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
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotesFragment : Fragment() {

    private lateinit var binding: LayoutNotesFragmentBinding
    private lateinit var notesAdapter: NotesFragmentAdapter
    private val viewModel: NotesViewModel by viewModels()

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
        binding.textViewScreenTitleNotesFragment.text = "Notes"
        notesAdapter = NotesFragmentAdapter()
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
        viewModel.notesList.observe(viewLifecycleOwner) { notes ->
            notesAdapter.submitList(notes)
        }
    }

    private fun navigateToNoteFragment() =
        findNavController().navigate(R.id.action_notesFragment_to_addNoteFragment)

}