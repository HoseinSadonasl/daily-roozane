package com.abc.daily.ui.add_note

import android.app.ActionBar.LayoutParams
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatEditText
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.abc.daily.R
import com.abc.daily.databinding.LayoutAddNoteBinding
import com.abc.daily.domain.model.note.Note
import com.google.android.material.checkbox.MaterialCheckBox
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
        onNoteSaveListener()
        onDeleteNoteListener()
        onSwitchSetReminderListener()
        onAddItemListListener()
    }

    private fun onAddItemListListener() {
        binding.buttonAddNoteAddItem.setOnClickListener {
            binding.itemParent.visibility = VISIBLE
            binding.itemParent.addView(itemListVew() as View)
        }
    }

    private fun itemListVew(): LinearLayout {
        val params = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        val layout = LinearLayout(requireContext()).apply {
            layoutParams = params
            orientation = LinearLayout.HORIZONTAL
            addView(itemCheckBoxView(this.context))
            addView(itemTextView(this.context))
        }
        return layout
    }

    private fun itemCheckBoxView(context: Context): View {
        return MaterialCheckBox(context).apply {
            setOneListener { compoundButton, isChecked ->

            }
        }

    }

    private fun itemTextView(context: Context): View {
        return AppCompatEditText(context).apply {
            hint = "List item"
        }
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

    private fun onDeleteNoteListener() {
        binding.imageViewAddNoteDelete.setOnClickListener { deleteNote(note) }
    }

    private fun onNoteSaveListener() {
        binding.fabAddNoteSave.setOnClickListener { saveNote() }
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