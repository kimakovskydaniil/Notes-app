package com.pblcnm.notes.data

import com.pblcnm.notes.data.local.LocalDataSource
import com.pblcnm.notes.data.remote.RemoteDataSource
import com.pblcnm.notes.model.Note
import kotlinx.coroutines.flow.Flow
import org.slf4j.LoggerFactory
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NoteRepository @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val remoteRepository: RemoteDataSource
) {
    private val logger = LoggerFactory.getLogger(NoteRepository::class.java)

    val notesFlow: Flow<List<Note>> = localDataSource.notesFlow

    suspend fun addNote(note: Note) {
        logger.info("Adding note: uid=${note.uid}, title='${note.title}'")

        localDataSource.addNote(note)

        try {
            logger.debug("Syncing note with backend: uid=${note.uid}")
            remoteRepository.createNote(note)
            logger.info("Note successfully synced with backend: uid=${note.uid}")
        } catch (e: Exception) {
            logger.error("Failed to sync note with backend: uid=${note.uid}, error=${e.message}", e)
        }
    }

    suspend fun deleteNote(uid: String): Boolean {
        logger.info("Removing note: uid=$uid")

        val localRemoved = localDataSource.deleteNote(uid)
        logger.debug("Local removal result: $localRemoved for uid=$uid")

        if (localRemoved) {
            try {
                logger.debug("Removing note from backend: uid=$uid")
                remoteRepository.deleteNote(uid)
                logger.info("Note successfully removed from backend: uid=$uid")
            } catch (e: Exception) {
                logger.error("Failed to remove note from backend: uid=$uid, error=${e.message}", e)
            }
        } else {
            logger.warn("Note not found locally, skipping backend removal: uid=$uid")
        }

        return localRemoved
    }

    suspend fun getNoteByUid(noteUid: String): Note? {
        logger.debug("Getting note by uid: uid=$noteUid")

        val localNote = localDataSource.getNoteByUid(noteUid)
        if (localNote != null) {
            logger.debug("Note found locally: uid=$noteUid")
            return localNote
        }

        logger.debug("Note not found locally, trying backend: uid=$noteUid")

        return try {
            when (val result = remoteRepository.fetchNote(noteUid)) {
                is com.pblcnm.notes.data.remote.util.ResultWrapper.Success -> {
                    val note = result.payload
                    logger.info("Note loaded from backend, caching locally: uid=$noteUid")
                    localDataSource.addNote(note)
                    note
                }

                is com.pblcnm.notes.data.remote.util.ResultWrapper.Error -> {
                    logger.warn("Failed to load note from backend: uid=$noteUid, error=${result.exception.message}")
                    null
                }
            }
        } catch (e: Exception) {
            logger.error("Exception while fetching note from backend: uid=$noteUid, error=${e.message}", e)
            null
        }
    }

    suspend fun updateNote(updatedNote: Note) {
        logger.info("Updating note: uid=${updatedNote.uid}, title='${updatedNote.title}'")

        localDataSource.updateNote(updatedNote)

        try {
            logger.debug("Syncing note update with backend: uid=${updatedNote.uid}")
            remoteRepository.updateNote(updatedNote)
            logger.info("Note update successfully synced with backend: uid=${updatedNote.uid}")
        } catch (e: Exception) {
            logger.error("Failed to sync note update with backend: uid=${updatedNote.uid}, error=${e.message}", e)
        }
    }
}