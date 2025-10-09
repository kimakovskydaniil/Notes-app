package com.pblcnm.notes.ui.screen.create

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pblcnm.notes.data.FileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateNoteViewModel @Inject constructor(
    private val fileRepository: FileRepository
) : ViewModel() {

    var uiState by mutableStateOf(CreateNoteState())
        private set

    fun processAction(action: CreateNoteAction) {
        when (action) {
            is CreateNoteAction.ModifyNoteData -> updateNoteDataState(action.note)
            is CreateNoteAction.LoadNote -> loadNote(action.noteUid)
            is CreateNoteAction.CreateNote -> {
                if (action.noteUid == null) createNote() else editNote()
            }
        }
    }

    private fun updateNoteDataState(note: NoteEntity) {
        uiState = uiState.copy(
            selectedNote = note,
            isValid = validateNote(note)
        )
    }

    private fun validateNote(note: NoteEntity): Boolean {
        return note.title.trim().isNotEmpty() && note.content.trim().isNotEmpty()
    }

    private fun loadNote(uid: String) {
        viewModelScope.launch {
            val note = fileRepository.getNote(uid)
            note?.let {
                uiState = uiState.copy(
                    selectedNote = it.toUi()
                )
            }
        }
    }

    private fun editNote() {
        viewModelScope.launch {
            if (validateNote(uiState.selectedNote)) {
                fileRepository.updateNote(
                    updatedNote = uiState.selectedNote.toData()
                )
            }
        }
    }

    private fun createNote() {
        viewModelScope.launch {
            if (validateNote(uiState.selectedNote)) {
                fileRepository.addNote(
                    note = uiState.selectedNote.toData()
                )
            }
        }
    }
}