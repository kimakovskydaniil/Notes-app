package com.pblcnm.notes.ui.screen.create

sealed interface CreateNoteAction {
    data class ModifyNoteData(val note: NoteEntity) : CreateNoteAction
    data object CreateNote : CreateNoteAction
}