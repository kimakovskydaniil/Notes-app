package com.pblcnm.notes.model

import android.graphics.Color
import org.json.JSONObject
import java.util.UUID

data class Note(
    val uid: String = UUID.randomUUID().toString(),
    val title: String,
    val content: String,
    val color: Int = Color.WHITE,
    val importance: Importance = Importance.NORMAL,
    val destructionDate: Long? = null
) {

    companion object {
        fun parse(json: JSONObject): Note? {
            return try {
                val uid = json.optString("uid", UUID.randomUUID().toString())
                val title = json.getString("title")
                val content = json.getString("content")

                val color = if (json.has("color")) {
                    json.getInt("color")
                } else {
                    Color.WHITE
                }

                val importanceValue = json.optString("importance", Importance.NORMAL.label)
                val importance = Importance.entries.find { it.label == importanceValue }
                    ?: Importance.NORMAL

                Note(uid, title, content, color, importance)
            } catch (e: Exception) {
                null
            }
        }
    }

    val json: JSONObject
        get() = JSONObject().apply {
            put("uid", uid)
            put("title", title)
            put("content", content)

            if (color != Color.WHITE) {
                put("color", color)
            }

            if (importance != Importance.NORMAL) {
                put("importance", importance.label)
            }
        }
}