package com.abc.daily.domain.use_case

import com.abc.daily.domain.model.note.Note
import com.abc.daily.domain.repository.NoteRepository

class GetNote(
    private val noteRepository: NoteRepository
) {

    suspend fun invoke(id: Int): Note? = noteRepository.getNoteById(id)

}