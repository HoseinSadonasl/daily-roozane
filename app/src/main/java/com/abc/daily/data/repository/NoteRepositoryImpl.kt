package com.abc.daily.data.repository

import com.abc.daily.domain.model.note.Note
import com.abc.daily.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NoteRepositoryImpl @Inject constructor(

): NoteRepository {
    override suspend fun saveNote(note: Note) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteNoteById(note: Note) {
        TODO("Not yet implemented")
    }

    override suspend fun getNoteById(id: Int): Note? {
        TODO("Not yet implemented")
    }

    override fun getNotesList(id: Int): Flow<List<Note>> {
        TODO("Not yet implemented")
    }
}