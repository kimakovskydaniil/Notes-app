package com.pblcnm.notes.data.remote

import com.pblcnm.notes.data.remote.model.UidResponse
import com.pblcnm.notes.data.remote.util.ResultWrapper
import com.pblcnm.notes.model.Note

interface RemoteDataSource {
    suspend fun fetchNotes(): ResultWrapper<List<Note>>
    suspend fun fetchNote(uid: String): ResultWrapper<Note>
    suspend fun createNote(note: Note): ResultWrapper<UidResponse>
    suspend fun updateNote(note: Note): ResultWrapper<UidResponse>
    suspend fun deleteNote(uid: String): ResultWrapper<Unit>
    suspend fun patchNotes(notes: List<Note>): ResultWrapper<List<Note>>
    suspend fun fetchNotesWithThreshold(threshold: Int?): ResultWrapper<List<Note>>
    suspend fun clearNotes()
}