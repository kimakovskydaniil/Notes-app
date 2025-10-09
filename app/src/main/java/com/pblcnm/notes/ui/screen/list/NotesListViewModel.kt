package com.pblcnm.notes.ui.screen.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pblcnm.notes.data.FileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesListViewModel @Inject constructor(
    private val fileRepository: FileRepository
) : ViewModel() {

    private val _state = MutableStateFlow<NotesState>(NotesState.Loading)
    val state: StateFlow<NotesState> = _state.asStateFlow()

    fun processAction(action: NotesListAction) {
        when (action) {
            is NotesListAction.LoadNotes -> loadNotes()
            is NotesListAction.DeleteNote -> deleteNote(action.noteId)
        }
    }

    private fun loadNotes() {
        viewModelScope.launch {
            try {
                _state.value = NotesState.Success(fileRepository.allNotes)
            } catch (e: Exception) {
                _state.value = NotesState.Error("Failed to load notes: ${e.message}")
            }
        }
    }

    private fun deleteNote(noteUid: String) {
        viewModelScope.launch {
            try {
                fileRepository.deleteNote(noteUid)
                loadNotes()
            } catch (e: Exception) {
                _state.value = NotesState.Error("Failed to delete note: ${e.message}")
            }
        }
    }
}