package com.abc.daily.domain.repository

import com.abc.daily.domain.model.note.Note
import kotlinx.coroutines.flow.Flow

interface NoteRepository {

    suspend fun saveNote(note: Note)

    suspend fun deleteNote(note: Note)

    suspend fun getNoteById(id: Int): Note?

    fun getNotesList(): Flow<List<Note>>

}
