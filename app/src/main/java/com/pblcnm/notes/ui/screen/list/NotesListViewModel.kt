package com.pblcnm.notes.ui.screen.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pblcnm.notes.data.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesListViewModel @Inject constructor(
    private val repository: NoteRepository
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
            _state.value = NotesState.Loading

            try {
                repository.notesFlow.collect { notes ->
                    _state.value = NotesState.Success(notes)
                }
            } catch (e: Exception) {
                _state.value = NotesState.Error("Failed to load notes: ${e.message}")
            }
        }
    }

    private fun deleteNote(noteUid: String) {
        viewModelScope.launch {
            try {
                repository.deleteNote(noteUid)
                loadNotes()
            } catch (e: Exception) {
                _state.value = NotesState.Error("Failed to delete note: ${e.message}")
            }
        }
    }
}