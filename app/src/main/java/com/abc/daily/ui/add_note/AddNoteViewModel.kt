package com.abc.daily.ui.add_note

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abc.daily.domain.model.note.Note
import com.abc.daily.domain.use_case.NotesDomain
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddNoteViewModel @Inject constructor(
    private val notesDomain: NotesDomain
) : ViewModel() {

    val noteLiveData = MutableLiveData<Note?>()
    val noteReminderLiveData = MutableLiveData<Pair<Boolean, Long>>()

    fun saveNote(note: Note) = viewModelScope.launch {
        val newNote = notesDomain.saveNote(note)
        getNote(newNote.toInt())
    }

    fun getNote(id: Int) {
        viewModelScope.launch {
            val note = notesDomain.getNote.invoke(id)
            noteLiveData.postValue(note)
        }
    }

    fun deleteNote(note: Note) = viewModelScope.launch {
        notesDomain.deleteNote(note)
    }

    fun setReminderNoteLiveData(timeStamp: Long, fromPast: Boolean) {
        noteReminderLiveData.value = fromPast to timeStamp
    }
}