package com.pblcnm.notes.data.local.file

import com.pblcnm.notes.data.local.LocalDataSource
import com.pblcnm.notes.model.Note
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.json.JSONArray
import org.slf4j.LoggerFactory
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FileNotebook @Inject constructor() : LocalDataSource {
    private val _notes = mutableListOf<Note>()
    private val _notesFlow = MutableStateFlow<List<Note>>(emptyList())

    override val notesFlow: StateFlow<List<Note>> = _notesFlow.asStateFlow()
    private val logger = LoggerFactory.getLogger(FileNotebook::class.java)

    private fun emitNotes() {
        _notesFlow.value = _notes.toList()
    }

    override suspend fun addNote(note: Note) {
        _notes.add(note)
        emitNotes()
        logger.info("Added note: uid=${note.uid}, title='${note.title}'")
    }

    override suspend fun deleteNote(uid: String): Boolean {
        val removed = _notes.removeIf { it.uid == uid }
        if (removed) {
            emitNotes()
            logger.info("Removed note with uid=$uid")
        } else {
            logger.warn("Attempt to remove non-existing note with uid=$uid")
        }
        return removed
    }

    override suspend fun getNoteByUid(noteUid: String): Note? {
        return _notes.find { it.uid == noteUid }
    }

    override suspend fun updateNote(updatedNote: Note) {
        val index = _notes.indexOfFirst { it.uid == updatedNote.uid }
        if (index != -1) {
            _notes[index] = updatedNote
            emitNotes()
            logger.info("Updated note: uid=${updatedNote.uid}, title='${updatedNote.title}'")
        } else {
            logger.warn("Attempt to update non-existing note with uid=${updatedNote.uid}")
        }
    }

    override suspend fun saveToFile(file: File) {
        try {
            val jsonArray = JSONArray()
            _notes.forEach { note ->
                jsonArray.put(note.json)
            }
            file.writeText(jsonArray.toString(2))
            logger.info("Saved ${_notes.size} notes to ${file.absolutePath}")
        } catch (e: Exception) {
            logger.error("Failed to save notes to file", e)
        }
    }

    override suspend fun loadFromFile(file: File) {
        if (!file.exists()) {
            logger.info("File does not exist: ${file.absolutePath}. Starting with empty notebook.")
            return
        }

        try {
            val content = file.readText()
            val jsonArray = JSONArray(content)
            val loadedNotes = mutableListOf<Note>()

            for (i in 0 until jsonArray.length()) {
                val json = jsonArray.getJSONObject(i)
                Note.parse(json)?.let { loadedNotes.add(it) }
            }

            _notes.clear()
            _notes.addAll(loadedNotes)
            emitNotes()
            logger.info("Loaded ${loadedNotes.size} notes from ${file.absolutePath}")
        } catch (e: Exception) {
            logger.error("Failed to load notes from file: ${file.absolutePath}", e)
        }
    }
}