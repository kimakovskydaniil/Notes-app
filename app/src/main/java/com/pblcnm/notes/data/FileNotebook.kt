package com.pblcnm.notes.data

import com.pblcnm.notes.model.Note
import org.json.JSONArray
import org.slf4j.LoggerFactory
import java.io.File

class FileNotebook private constructor(
    private val notes: MutableList<Note> = mutableListOf()
) {

    private val logger = LoggerFactory.getLogger(FileNotebook::class.java)

    val allNotes: List<Note>
        get() = notes.toList()

    fun addNote(note: Note) {
        notes.add(note)
        logger.info("Added note: uid=${note.uid}, title='${note.title}'")
    }

    fun removeNote(uid: String) {
        val removed = notes.removeIf { it.uid == uid }
        if (removed) {
            logger.info("Removed note with uid=$uid")
        } else {
            logger.warn("Attempt to remove non-existing note with uid=$uid")
        }
    }

    fun saveToFile(file: File) {
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

    companion object {
        private val logger = LoggerFactory.getLogger(FileNotebook::class.java)

        fun loadFromFile(file: File): FileNotebook {
            if (!file.exists()) {
                logger.info("File does not exist: ${file.absolutePath}. Starting with empty notebook.")
                return FileNotebook()
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
                FileNotebook(notes)
            } catch (e: Exception) {
                logger.error("Failed to load notes from file: ${file.absolutePath}", e)
                FileNotebook()
            }
        }
    }
}