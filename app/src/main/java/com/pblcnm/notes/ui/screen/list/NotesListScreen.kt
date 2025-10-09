package com.pblcnm.notes.ui.screen.list

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pblcnm.notes.R
import com.pblcnm.notes.model.Note
import com.pblcnm.notes.navigation.components.TopBar
import com.pblcnm.notes.ui.screen.list.components.SwipeNoteCard
import com.pblcnm.notes.ui.theme.neonGreenColor

@Composable
fun NotesListScreen(
    onNoteClick: (String) -> Unit,
    onCreateNote: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: NotesListViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val showDeleteDialog = remember { mutableStateOf(false) }
    val noteToDelete = remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        viewModel.processAction(NotesListAction.LoadNotes)
    }

    Scaffold(
        topBar = {
            TopBar(titleRes = R.string.notes_title)
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onCreateNote,
                containerColor = neonGreenColor,
                contentColor = Color.Black
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Note"
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (val currentState = state) {
                is NotesState.Loading -> {
                    Text(
                        text = "Loading interdimensional notes...",
                        modifier = Modifier.align(Alignment.Center),
                        color = Color.White
                    )
                }

                is NotesState.Success -> {
                    if (showDeleteDialog.value) {
                        AlertDialog(
                            onDismissRequest = {
                                showDeleteDialog.value = false
                                noteToDelete.value = null
                            },
                            title = {
                                Text(
                                    text = "Удаление заметки",
                                    color = Color.Black
                                )
                            },
                            text = {
                                Text(
                                    text = "WUBBA LUBBA DUB DUB! Вы уверены?",
                                    color = Color.Black
                                )
                            },
                            confirmButton = {
                                Button(
                                    onClick = {
                                        noteToDelete.value?.let { noteId ->
                                            viewModel.processAction(
                                                NotesListAction.DeleteNote(
                                                    noteId
                                                )
                                            )
                                        }
                                        showDeleteDialog.value = false
                                        noteToDelete.value = null
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFFFF6B6B)
                                    )
                                ) {
                                    Text("УДАЛИТЬ")
                                }
                            },
                            dismissButton = {
                                Button(
                                    onClick = {
                                        showDeleteDialog.value = false
                                        noteToDelete.value = null
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFF555555)
                                    )
                                ) {
                                    Text("ОТМЕНА")
                                }
                            }
                        )
                    }

                    NotesContent(
                        notes = currentState.notes,
                        onClickNote = onNoteClick,
                        onSwipeDelete = { noteId ->
                            noteToDelete.value = noteId
                            showDeleteDialog.value = true
                        },
                        modifier = Modifier.fillMaxSize()
                    )
                }

                is NotesState.Error -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Portal malfunction!",
                            color = Color(0xFFFF6B6B),
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = currentState.message,
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                        Button(
                            onClick = { viewModel.processAction(NotesListAction.LoadNotes) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = neonGreenColor
                            ),
                            modifier = Modifier.padding(top = 16.dp)
                        ) {
                            Text("RETRY", color = Color.Black)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun NotesContent(
    notes: List<Note>,
    onClickNote: (String) -> Unit,
    onSwipeDelete: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Image(
            painter = painterResource(id = R.drawable.empty_notes),
            contentDescription = "Empty list",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        )

        if (notes.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "No interdimensional notes found!",
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 32.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.Top
            ) {
                items(items = notes, key = { it.uid }) { note ->
                    SwipeNoteCard(
                        note = note,
                        onDelete = { onSwipeDelete(note.uid) },
                        onClick = { onClickNote(note.uid) },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}