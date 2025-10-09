package com.pblcnm.notes.ui.screen.create

sealed interface CreateNoteAction {
    data class ModifyNoteData(val note: NoteEntity) : CreateNoteAction
    data class LoadNote(val noteUid: String) : CreateNoteAction
    data class CreateNote(val noteUid: String?) : CreateNoteAction
}