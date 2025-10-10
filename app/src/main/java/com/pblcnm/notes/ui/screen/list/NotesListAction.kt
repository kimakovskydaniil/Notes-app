package com.pblcnm.notes.ui.screen.list

sealed interface NotesListAction {
    object LoadNotes : NotesListAction
    data class DeleteNote(val noteId: String) : NotesListAction
}