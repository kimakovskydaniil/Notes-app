package com.pblcnm.notes.ui.screen.list

sealed class NotesListAction {
    object LoadNotes : NotesListAction()
    data class DeleteNote(val noteId: String) : NotesListAction()
}