package com.pblcnm.notes.ui.screen.create

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateNoteViewModel @Inject constructor() : ViewModel() {

    var uiState by mutableStateOf(CreateNoteState())
        private set

    fun processAction(action: CreateNoteAction) {
        when (action) {
            is CreateNoteAction.ModifyNoteData -> updateNoteDataState(action.note)
            CreateNoteAction.CreateNote -> createNote()
        }
    }

    private fun updateNoteDataState(note: NoteEntity) {
        uiState = uiState.copy(
            selectedNote = note,
            isValid = validateInterdimensionalNote(note)
        )
    }

    private fun validateInterdimensionalNote(note: NoteEntity): Boolean {
        return note.title.trim().isNotEmpty() && note.content.trim().isNotEmpty()
    }

    private fun createNote() {
        viewModelScope.launch {
            if (validateInterdimensionalNote(uiState.selectedNote)) {
                // сохранение в репозиторий
            }
        }
    }
}