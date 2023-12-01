package com.abc.daily.domain.use_case

import com.abc.daily.domain.model.note.Note
import com.abc.daily.domain.repository.NoteRepository
import com.abc.daily.domain.util.NoteOrder
import com.abc.daily.domain.util.OrderType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map

class GetNotesList(
    private val noteRepository: NoteRepository
) {
    operator fun invoke(noteOrder: NoteOrder, char: String?): Flow<List<Note>> {
        return noteRepository.getNotesList().map { notes ->
            char?.let {
                val filteredNotes = notes.filter {note: Note -> note.title.contains(char) }
                orderedNotes(noteOrder, filteredNotes)
            } ?: run {
                orderedNotes(noteOrder, notes)
            }
        }
    }

    private fun orderedNotes(
        noteOrder: NoteOrder,
        notes: List<Note>
    ) = when (noteOrder.orderType) {
        is OrderType.Ascending -> {
            when (noteOrder) {
                is NoteOrder.ByName -> notes.sortedBy { it.title }
                is NoteOrder.ByDate -> notes.sortedBy { it.createdAt }
            }
        }

        is OrderType.Descending -> {
            when (noteOrder) {
                is NoteOrder.ByName -> notes.sortedByDescending { it.title }
                is NoteOrder.ByDate -> notes.sortedByDescending { it.createdAt }
            }
        }
    }

}