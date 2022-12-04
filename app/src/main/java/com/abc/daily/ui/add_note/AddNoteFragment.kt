package com.abc.daily.ui.add_note

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.abc.daily.R
import com.abc.daily.databinding.LayoutAddNoteBinding
import com.abc.daily.domain.model.note.Note
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddNoteFragment: Fragment() {

    private lateinit var binding: LayoutAddNoteBinding
    private val addNoteViewModel: AddNoteViewModel by viewModels()
    private lateinit var note: Note

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.layout_add_note,
            container,
            false
        )

        initListeners()

        return binding.root
    }

    private fun initListeners() {
        binding.fabAddNoteSave.setOnClickListener {
            saveNote()
        }

        binding.imageViewAddNoteDelete.setOnClickListener {
            deleteNote(note)
        }

        binding.imageViewAddNoteReminder.setOnClickListener {
            setNoteReminder()
        }
    }

    private fun saveNote() {
        val currentTime = System.currentTimeMillis()
        val note = Note(
            title = binding.editTextAddNoteTitle.text.toString(),
            description = binding.editTextAddNoteDescription.text.toString(),
            createdAt = currentTime.toString(),
            modifiedAt = currentTime.toString()
        )
        addNoteViewModel.saveNote(note)
    }

    private fun deleteNote(note: Note) = addNoteViewModel.deleteNote(note)

    private fun setNoteReminder() {

    }

}