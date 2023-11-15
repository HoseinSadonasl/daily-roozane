package com.abc.daily.ui.add_note

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.abc.daily.R
import com.abc.daily.app.AlertReceiver
import com.abc.daily.databinding.LayoutAddNoteBinding
import com.abc.daily.domain.model.note.Note
import com.abc.daily.util.CustomDatePickerDialog
import com.abc.daily.util.CustomTimePickerDialog
import com.abc.daily.util.GlobalReceiver
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class AddNoteFragment: Fragment() {

    private lateinit var binding: LayoutAddNoteBinding
    private val addNoteViewModel: AddNoteViewModel by viewModels()
    private lateinit var note: Note
    private lateinit var calendar: Calendar

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

        initComponents()
        initListeners()

        return binding.root
    }

    private fun initComponents() {
        calendar = Calendar.getInstance()
    }

    private fun initListeners() {
        binding.fabAddNoteSave.setOnClickListener {
            saveNote()
        }

        binding.imageViewAddNoteDelete.setOnClickListener {
            deleteNote(note)
        }

        binding.textViewReminderTimeAddNoteFragment.setOnClickListener {
            getTimeForReminder()
        }

        binding.textViewReminderDateAddNoteFragment.setOnClickListener {
            getDateForReminder()
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
        setReminderForNote()
    }

    private fun setReminderForNote() {
        val alarm = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(requireContext(), GlobalReceiver::class.java)
        val requestCode = System.currentTimeMillis().toInt()
        val pendingIntent: PendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getBroadcast(
                requireContext(),
                requestCode,
                intent,
                PendingIntent.FLAG_IMMUTABLE
            )
        } else {
            PendingIntent.getBroadcast(requireContext(), requestCode, intent,
                PendingIntent.FLAG_IMMUTABLE)
        }
        alarm.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }

    private fun deleteNote(note: Note) = addNoteViewModel.deleteNote(note)

    private fun getTimeForReminder() {
        CustomTimePickerDialog(
            requireContext(),
            dialogInterface = object : CustomTimePickerDialog.DialogInterface {
                override fun onPositiveClick(timePickerDialog: CustomTimePickerDialog) {
                    timePickerDialog.let {
                        calendar = it.getTime()
                        it.dismiss()
                    }
                }

                override fun onNegativeClick(timePickerDialog: CustomTimePickerDialog) {
                    timePickerDialog.dismiss()
                }

            }
        ).show()
    }

    private fun getDateForReminder() {
        CustomDatePickerDialog(
            requireContext(),
            dialogInterface = object : CustomDatePickerDialog.DialogInterface {
                override fun onPositiveClick(datePickerDialog: CustomDatePickerDialog) {
                    datePickerDialog.apply {
                        calendar = getDate()
                        dismiss()
                    }
                }

                override fun onNegativeClick(datePickerDialog: CustomDatePickerDialog) {
                    datePickerDialog.dismiss()
                }

            }
        ).show()
    }

}