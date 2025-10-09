package com.pblcnm.notes.data

import com.pblcnm.notes.model.Note
import org.json.JSONArray
import org.slf4j.LoggerFactory
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

interface FileRepository {
    val allNotes: List<Note>
    fun addNote(note: Note)
    fun deleteNote(uid: String)
    fun getNote(uid: String): Note?
    fun updateNote(updatedNote: Note)
    fun saveToFile(file: File)
    fun loadFromFile(file: File)
}

@Singleton
class FileNotebook @Inject constructor() : FileRepository {
    private val notes: MutableList<Note> = mutableListOf()
    private val logger = LoggerFactory.getLogger(FileNotebook::class.java)

    override val allNotes: List<Note>
        get() = notes.toList()

    override fun addNote(note: Note) {
        notes.add(note)
        logger.info("Added note: uid=${note.uid}, title='${note.title}'")
    }

    override fun deleteNote(uid: String) {
        val removed = notes.removeIf { it.uid == uid }
        if (removed) {
            logger.info("Removed note with uid=$uid")
        } else {
            logger.warn("Attempt to remove non-existing note with uid=$uid")
        }
    }

    override fun getNote(uid: String): Note? {
        return notes.find { it.uid == uid }
    }

    override fun updateNote(updatedNote: Note) {
        val index = notes.indexOfFirst { it.uid == updatedNote.uid }
        if (index != -1) {
            notes[index] = updatedNote
            logger.info("Updated note: uid=${updatedNote.uid}, title='${updatedNote.title}'")
        } else {
            logger.warn("Attempt to update non-existing note with uid=${updatedNote.uid}")
        }
    }

    override fun saveToFile(file: File) {
        try {
            val jsonArray = JSONArray()
            notes.forEach { note ->
                jsonArray.put(note.json)
            }
            file.writeText(jsonArray.toString(2))
            logger.info("Saved ${notes.size} notes to ${file.absolutePath}")
        } catch (e: Exception) {
            logger.error("Failed to save notes to file", e)
        }
    }


    override fun loadFromFile(file: File) {
        if (!file.exists()) {
            logger.info("File does not exist: ${file.absolutePath}. Starting with empty notebook.")
        }

        return try {
            val content = file.readText()
            val jsonArray = JSONArray(content)
            val notes = mutableListOf<Note>()

            for (i in 0 until jsonArray.length()) {
                val json = jsonArray.getJSONObject(i)
                Note.parse(json)?.let { notes.add(it) }
            }

            logger.info("Loaded ${notes.size} notes from ${file.absolutePath}")
        } catch (e: Exception) {
            logger.error("Failed to load notes from file: ${file.absolutePath}", e)
        }
    }
}
