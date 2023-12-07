package com.abc.daily.ui.add_note

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.abc.daily.Dialog
import com.abc.daily.R
import com.abc.daily.databinding.LayoutAddNoteBinding
import com.abc.daily.domain.model.note.Note
import com.abc.daily.util.CustomDatePickerDialog
import com.abc.daily.util.CustomTimePickerDialog
import com.abc.daily.util.DateUtil
import com.abc.daily.util.GlobalReceiver
import com.abc.daily.util.PersianDate
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar
import javax.inject.Inject

@AndroidEntryPoint
class AddNoteFragment : Fragment() {

    @Inject
    lateinit var alarmManager: AlarmManager

    private lateinit var binding: LayoutAddNoteBinding
    private val addNoteViewModel: AddNoteViewModel by viewModels()
    private var note: Note? = null
    private lateinit var calendar: Calendar
    private var noteIdArg: Int? = null
    private var hasReminder: String? = null

    companion object {
        const val NOTE_ARGUMENT = "noteId"
    }

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

        getArgs()
        initComponents()
        initNote()
        observeNoteData()
        observeNoteReminderData()
        initListeners()

        return binding.root
    }

    private fun observeNoteReminderData() {
        addNoteViewModel.noteReminderLiveData.observe(viewLifecycleOwner) {
            hasReminder = it.second.toString()
            if (it.first) {
                // setReminderForNote(it.second)
                setReminderButtonColorTint(R.color.btn_primary)
            } else {
                setReminderButtonColorTint(R.color.btn_secondary)
                val color = ContextCompat.getColor(requireContext(), R.color.btn_alarm_state)
                binding.btnAddAlarmAddNoteFragment.setTextColor(color)
                binding.btnAddAlarmAddNoteFragment.iconTint = ColorStateList.valueOf(color)
            }
        }
    }

    private fun observeNoteData() {
        addNoteViewModel.noteLiveData.observe(viewLifecycleOwner) { note ->
            note?.let {
                this@AddNoteFragment.note = note
                if (noteIdArg == null) noteIdArg = note.id
                it.remindAt?.let { hasReminder = it }
                binding.apply {
                    textViewAddNoteAppbarTitle.setText(it.title)
                    editTextAddNoteTitle.setText(it.title)
                    editTextAddNoteDescription.setText(it.description)
                    textViewAddNoteCreatedDate.text = getString(
                        R.string.txt_xreated,
                        DateUtil.toPersianDateAndTime(it.createdAt.toString(), requireContext())
                    )
                    textViewAddNoteModifiedDate.text = getString(
                        R.string.txt_modified,
                        DateUtil.toPersianDateAndTime(it.modifiedAt.toString(), requireContext())
                    )
                    initializeReminderButton(it)
                }
            }
        }
    }

    private fun initializeReminderButton(note: Note) {
        if (note.remindAt.isNullOrBlank()) {
            binding.btnAddAlarmAddNoteFragment.icon =
                ContextCompat.getDrawable(requireContext(), R.drawable.all_addalarm)
            setReminderButtonColorTint(R.color.btn_secondary)
        } else {
            val formattedTime = DateUtil.alarmToPersianDateAndTime(note.remindAt.toString())
            binding.btnAddAlarmAddNoteFragment.text = formattedTime
            setReminderButtonColorTint(R.color.btn_primary)
            if (note.remindAt!!.toLong() <= calendar.timeInMillis)
                addNoteViewModel.setReminderNoteLiveData(note.remindAt!!.toLong(), false)
        }
    }

    private fun setReminderButtonColorTint(colorResourceId: Int) {
        val color = ContextCompat.getColor(requireContext(), colorResourceId)
        binding.btnAddAlarmAddNoteFragment.backgroundTintList = ColorStateList.valueOf(color)
    }

    private fun getArgs() {
        arguments?.let {
            val noteId = it.getInt(NOTE_ARGUMENT)
            if (noteId > 0) noteIdArg = noteId
        }
    }

    private fun initComponents() {
        binding.btnAddAlarmAddNoteFragment.text = getString(R.string.addrReminder_addNoteFragment)
        binding.imageViewAddNoteDelete.visibility = if (noteIdArg != null) View.VISIBLE else View.GONE
        setReminderButtonColorTint(R.color.btn_secondary)
        calendar = Calendar.getInstance()
    }

    private fun initNote() {
        noteIdArg?.let {
            addNoteViewModel.getNote(it)
        }
    }

    private fun initListeners() {
        binding.fabAddNoteSave.setOnClickListener {
            if (binding.editTextAddNoteTitle.text!!.isNotBlank()) saveNote()
            hasReminder?.let {
                handleReminderForNote(it.toLong())
            }
            popFragmrnt()
        }

        binding.imageViewAddNoteDelete.setOnClickListener {
            note?.let { note -> deleteNote(note) }
        }

        binding.btnAddAlarmAddNoteFragment.setOnClickListener {
            if (binding.editTextAddNoteTitle.text!!.isNotBlank()) {
                saveNote()
                handleReminder()
            }
        }

        binding.buttonAddNoteBackward.setOnClickListener { popFragmrnt() }
    }

    private fun handleReminder() {
        getTimeForReminder()
    }


    private fun saveNote() {
        val currentTime = System.currentTimeMillis()
        val note = Note(
            id = noteIdArg,
            title = binding.editTextAddNoteTitle.text.toString(),
            description = binding.editTextAddNoteDescription.text.toString(),
            createdAt = if (noteIdArg == null) currentTime.toString() else note?.createdAt,
            modifiedAt = currentTime.toString(),
            remindAt = hasReminder
        )
        addNoteViewModel.saveNote(note)
    }

    private fun popFragmrnt() {
        findNavController().popBackStack()
    }


    private fun handleReminderForNote(timeInMillis: Long, cancelReminder: Boolean = false) {
        val intent = Intent(requireContext(), GlobalReceiver::class.java).apply {
            putExtra(GlobalReceiver.NOTIFICATION_NOTE_ID, note?.id)
            putExtra(GlobalReceiver.NOTIFICATION_NOTE_TITLE, note?.title)
        }
        val requestCode = timeInMillis.toInt()
        val pendingIntent: PendingIntent = createPendingIntent(requestCode, intent)
        Log.d(::getTimeForReminder.name, "handleReminderForNote: ${PersianDate(timeInMillis)}")

        if (cancelReminder)
            alarmManager.cancel(pendingIntent)
        else
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent)
    }

    private fun createPendingIntent(requestCode: Int, intent: Intent): PendingIntent =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getBroadcast(requireContext(), requestCode, intent, PendingIntent.FLAG_IMMUTABLE)
        } else PendingIntent.getBroadcast(requireContext(), requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT)

    private fun deleteNote(note: Note) = Dialog(requireContext(),
        onPositiveCallback = { dialog ->
            addNoteViewModel.deleteNote(note)
            dialog.dismiss()
            popFragmrnt()
        },
        onNegativeCallback = { dialog -> dialog.dismiss() }
    ).apply {
        setTitle(getString(R.string.deleteNoteTitle_warning))
        setDescription(getString(R.string.deleteNoteTitle_description))
        setPositiveButtonText(getString(R.string.ok))
        setNegativeButtonText(getString(R.string.cancel))
    }.show()

    private fun getTimeForReminder() {
        CustomTimePickerDialog(
            requireContext(),
            dialogInterface = object : CustomTimePickerDialog.DialogInterface {
                override fun onPositiveClick(timePickerDialog: CustomTimePickerDialog) {
                    timePickerDialog.let {
                        calendar.set(Calendar.HOUR_OF_DAY, it.getTime().get(Calendar.HOUR_OF_DAY))
                        calendar.set(Calendar.MINUTE, it.getTime().get(Calendar.MINUTE))
                        calendar.set(Calendar.SECOND, 0)
                        calendar.set(Calendar.AM_PM, it.getTime().get(Calendar.AM_PM))
                        it.dismiss()
                        getDateForReminder()
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
                    datePickerDialog.let {

                        // cancel previous notification if exist
                        note?.remindAt?.let {
                            handleReminderForNote(it.toLong(), true)
                            hasReminder = null
                        }

                        calendar.set(Calendar.YEAR, it.getDate().get(Calendar.YEAR))
                        calendar.set(Calendar.MONTH, it.getDate().get(Calendar.MONTH))
                        calendar.set(Calendar.DAY_OF_MONTH, it.getDate().get(Calendar.DAY_OF_MONTH))
                        Log.d(::getTimeForReminder.name, "onPositiveClick: ${PersianDate(calendar.timeInMillis)}")
                        addNoteViewModel.setReminderNoteLiveData(calendar.timeInMillis, true)
                        it.dismiss()
                    }
                }

                override fun onNegativeClick(datePickerDialog: CustomDatePickerDialog) {
                    datePickerDialog.dismiss()
                }

            }
        ).show()
    }

}