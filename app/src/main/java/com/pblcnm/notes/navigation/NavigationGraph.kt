package com.pblcnm.notes.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.scene.rememberSceneSetupNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.pblcnm.notes.ui.screen.create.CreateNoteScreen
import com.pblcnm.notes.ui.screen.list.NotesListScreen

@Composable
fun NavigationGraph() {
    val backStack = remember { mutableStateListOf<Any>(NotesList) }

    NavDisplay(
        entryDecorators = listOf(
            rememberSceneSetupNavEntryDecorator(),
            rememberSavedStateNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = { key ->
            when (key) {
                is NotesList -> NavEntry(key) {
                    NotesListScreen(
                        onNoteClick = { noteUid ->
                            backStack.add(NoteEdit(noteUid))
                        },
                        onCreateNote = {
                            backStack.add(CreateNote)
                        },
                        modifier = Modifier
                    )
                }

                is NoteEdit -> NavEntry(key) {
                    CreateNoteScreen(
                        noteUid = key.uid,
                        onBack = { backStack.removeLastOrNull() },
                        modifier = Modifier
                    )
                }

                is CreateNote -> NavEntry(key) {
                    CreateNoteScreen(
                        noteUid = null,
                        onBack = { backStack.removeLastOrNull() },
                        modifier = Modifier
                    )
                }

                else -> NavEntry(Unit) { Text("Unknown route") }
            }
        }
    )
}