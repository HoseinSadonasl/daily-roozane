package com.abc.daily.domain.use_case

import com.abc.daily.domain.model.note.Note
import com.abc.daily.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow

class GetNotesList (
    private val noteRepository: NoteRepository
) {

    fun invoke(): Flow<List<Note>> = noteRepository.getNotesList()

}