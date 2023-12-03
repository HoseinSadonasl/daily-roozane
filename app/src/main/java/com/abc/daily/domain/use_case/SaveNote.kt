package com.abc.daily.domain.use_case

import com.abc.daily.domain.model.note.Note
import com.abc.daily.domain.repository.NoteRepository

class SaveNote(
    private val noteRepository: NoteRepository
) {

    suspend operator fun invoke(note: Note): Long = note.title.isNotBlank().let {
        noteRepository.saveNote(note)
    }

}