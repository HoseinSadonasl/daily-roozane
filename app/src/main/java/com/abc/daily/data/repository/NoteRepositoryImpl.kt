package com.abc.daily.data.repository

import com.abc.daily.data.local.db.NoteDao
import com.abc.daily.domain.model.note.Note
import com.abc.daily.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NoteRepositoryImpl @Inject constructor(
    private val noteDao: NoteDao
): NoteRepository {

    override suspend fun saveNote(note: Note): Long = noteDao.save(note)

    override suspend fun deleteNote(note: Note) = noteDao.delete(note)

    override suspend fun getNoteById(id: Int): Note? = noteDao.getNote(id)

    override fun getNotesList(): Flow<List<Note>> = noteDao.getNoteList()

}