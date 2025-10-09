package com.pblcnm.notes.navigation

import kotlinx.serialization.Serializable

@Serializable
data object NotesList

@Serializable
data class NoteEdit(val uid: String)

@Serializable
data object CreateNote