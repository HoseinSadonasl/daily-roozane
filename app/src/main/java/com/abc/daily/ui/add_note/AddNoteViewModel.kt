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

    fun saveNote(note: Note) = viewModelScope.launch {
        notesDomain.saveNote(note)
    }

    fun getNote(id: Int) = viewModelScope.launch {
        notesDomain.getNote(id)
    }

    fun deleteNote(note: Note) = viewModelScope.launch {
        notesDomain.deleteNote(note)
    }

}