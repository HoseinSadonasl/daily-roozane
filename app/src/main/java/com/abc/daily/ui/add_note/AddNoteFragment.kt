package com.abc.daily.ui.add_note

import android.app.TimePickerDialog
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
import com.abc.daily.interfaces.DialogInterface
import com.abc.daily.util.CustomTimePickerDialog
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

        binding.textViewReminderTimeAddNoteFragment.setOnClickListener {
            setNoteReminder()
        }

        onSwitchSetReminderListener()

    }

    private fun onSwitchSetReminderListener() {
        binding.switchSetReminderAddNoteFragment.setOnCheckedChangeListener { compoundButton, isChecked ->
            binding.reminderParent.apply {
                if (isChecked) {
                    this.visibility = View.VISIBLE
                } else {
                    this.visibility = View.GONE
                }
            }

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
        CustomTimePickerDialog(
            requireContext(),
            dialogInterface = object : CustomTimePickerDialog.DialogInterface {
                override fun onPositiveClick(timePickerDialog: CustomTimePickerDialog) {
                    timePickerDialog.dismiss()
                }

                override fun onNegativeClick(timePickerDialog: CustomTimePickerDialog) {
                    timePickerDialog.dismiss()
                }

            }
        ).show()
    }

}