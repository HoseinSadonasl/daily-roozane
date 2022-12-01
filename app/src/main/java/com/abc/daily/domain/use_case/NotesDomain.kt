package com.abc.daily.domain.use_case

data class NotesDomain(
    val saveNote: SaveNote,
    val getNote: GetNote,
    val deleteNote: DeleteNote,
    val getNotesList: GetNotesList
)