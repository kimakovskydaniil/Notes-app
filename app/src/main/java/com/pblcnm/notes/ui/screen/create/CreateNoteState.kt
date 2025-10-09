package com.pblcnm.notes.ui.screen.create

data class CreateNoteState(
    val selectedNote: NoteEntity = NoteEntity(),
    val isValid: Boolean = false,
)