package com.pblcnm.notes.data.remote

import com.pblcnm.notes.data.remote.model.UidResponse
import com.pblcnm.notes.data.remote.util.ResultWrapper
import com.pblcnm.notes.model.Note
import org.slf4j.LoggerFactory
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteDataSourceImpl @Inject constructor() : RemoteDataSource {
    private val logger = LoggerFactory.getLogger(RemoteDataSourceImpl::class.java)

    override suspend fun fetchNotes(): ResultWrapper<List<Note>> {
        logger.warn("fetchNotes() - Not implemented yet")
        return ResultWrapper.Error(NotImplementedError("fetchNotes not implemented"))
    }

    override suspend fun fetchNote(uid: String): ResultWrapper<Note> {
        logger.warn("fetchNoteById($uid) - Not implemented yet")
        return ResultWrapper.Error(NotImplementedError("fetchNoteById not implemented"))
    }

    override suspend fun createNote(note: Note): ResultWrapper<UidResponse> {
        logger.warn("createNote(${note.title}) - Not implemented yet")
        return ResultWrapper.Error(NotImplementedError("createNote not implemented"))
    }

    override suspend fun updateNote(note: Note): ResultWrapper<UidResponse> {
        logger.warn("updateNote(${note.uid}) - Not implemented yet")
        return ResultWrapper.Error(NotImplementedError("updateNote not implemented"))
    }

    override suspend fun deleteNote(uid: String): ResultWrapper<Unit> {
        logger.warn("removeNoteById($uid) - Not implemented yet")
        return ResultWrapper.Error(NotImplementedError("removeNoteById not implemented"))
    }

    override suspend fun patchNotes(notes: List<Note>): ResultWrapper<List<Note>> {
        logger.warn("patchNotes(${notes.size} notes) - Not implemented yet")
        return ResultWrapper.Error(NotImplementedError("patchNotes not implemented"))
    }

    override suspend fun fetchNotesWithThreshold(threshold: Int?): ResultWrapper<List<Note>> {
        logger.warn("fetchNotesWithThreshold($threshold) - Not implemented yet")
        return ResultWrapper.Error(NotImplementedError("fetchNotesWithThreshold not implemented"))
    }

    override suspend fun clearNotes() {
        logger.warn("clearNotes() - Not implemented yet")
        throw NotImplementedError("clearNotes not implemented")
    }
}