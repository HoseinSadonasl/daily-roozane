package com.abc.daily.domain.repository

import com.abc.daily.domain.model.note.Note
import kotlinx.coroutines.flow.Flow

interface NoteRepository {

    suspend fun saveNote(note: Note)

    suspend fun deleteNoteById(note: Note)

    suspend fun getNoteById(id: Int): Note?

    fun getNotesList(id: Int): Flow<List<Note>>

}
