package com.pblcnm.notes.data.local

import com.pblcnm.notes.model.Note
import kotlinx.coroutines.flow.Flow
import java.io.File

interface LocalDataSource {
    val notesFlow: Flow<List<Note>>
    suspend fun addNote(note: Note)
    suspend fun deleteNote(uid: String): Boolean
    suspend fun getNoteByUid(noteUid: String): Note?
    suspend fun updateNote(updatedNote: Note)
    suspend fun saveToFile(file: File)
    suspend fun loadFromFile(file: File)
}