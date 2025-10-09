package com.pblcnm.notes.ui.screen.list

import com.pblcnm.notes.model.Note

sealed class NotesState {
    object Loading : NotesState()
    data class Success(val notes: List<Note>) : NotesState()
    data class Error(val message: String) : NotesState()
}