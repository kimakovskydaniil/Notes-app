package com.pblcnm.notes.data

import com.pblcnm.notes.model.Note
import org.json.JSONArray
import java.io.File

class FileNotebook private constructor(
    private val notes: MutableList<Note> = mutableListOf()
) {
    val allNotes: List<Note>
        get() = notes.toList()

    fun addNote(note: Note) = notes.add(note)

    fun removeNote(uid: String) = notes.removeIf { it.uid == uid }

    fun saveToFile(file: File) {
        val jsonArray = JSONArray()
        notes.forEach { note ->
            jsonArray.put(note.json)
        }

        file.writeText(jsonArray.toString(2))
    }

    companion object {
        fun loadFromFile(file: File): FileNotebook {
            if (!file.exists()) return FileNotebook()

            return try {
                val content = file.readText()
                val jsonArray = JSONArray(content)
                val notes = mutableListOf<Note>()

                for (i in 0 until jsonArray.length()) {
                    val json = jsonArray.getJSONObject(i)
                    Note.parse(json)?.let { notes.add(it) }
                }

                FileNotebook(notes)
            } catch (e: Exception) {
                e.printStackTrace()
                FileNotebook()
            }
        }
    }
}