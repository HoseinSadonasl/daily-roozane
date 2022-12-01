package com.abc.daily.ui.notes

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abc.daily.domain.model.note.Note
import com.abc.daily.domain.use_case.NotesDomain
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val notesDomain: NotesDomain
) : ViewModel() {

    val notesList = MutableLiveData<List<Note>>()

    fun getNotes() = notesDomain.getNotesList()
        .onEach {
            notesList.value = it
        }.launchIn(viewModelScope)


}