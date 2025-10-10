package com.pblcnm.notes.ui.screen.create

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.pblcnm.notes.R
import com.pblcnm.notes.navigation.components.TopBar
import com.pblcnm.notes.ui.screen.create.components.NoteForm
import com.pblcnm.notes.ui.theme.rickColor

@Composable
fun CreateNoteScreen(
    noteUid: String? = null,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CreateNoteViewModel = hiltViewModel(),
) {
    LaunchedEffect(noteUid) {
        if (noteUid != null) {
            viewModel.processAction(CreateNoteAction.LoadNote(noteUid))
        }
    }

    Scaffold(
        topBar = {
            TopBar(
                titleRes = when (noteUid) {
                    null -> R.string.create_note_title
                    else -> R.string.note_edit_title
                },
                showBackButton = true,
                onBackClick = { onBack() }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Black,
                            Color.Black
                        )
                    )
                )
        ) {
            Column(
                modifier = modifier
                    .verticalScroll(rememberScrollState())
                    .padding(innerPadding)
                    .padding(
                        horizontal = 16.dp,
                        vertical = 12.dp
                    ),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                NoteForm(
                    noteData = viewModel.state.selectedNote,
                    onNoteDataChange = {
                        viewModel.processAction(
                            CreateNoteAction.ModifyNoteData(note = it)
                        )
                    },
                    modifier = modifier
                )

                Button(
                    onClick = {
                        viewModel.processAction(CreateNoteAction.CreateNote(noteUid))
                        onBack()
                    },
                    enabled = viewModel.state.isValid,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = rickColor,
                        disabledContainerColor = Color(0xFF555555)
                    )
                ) {
                    Text(
                        text = "СОЗДАТЬ МЕЖПРОСТРАНСТВЕННУЮ ЗАМЕТКУ",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        fontSize = 14.sp
                    )
                }

                Text(
                    text = "\"WUBBA LUBBA DUB DUB!\"",
                    color = Color(0xFFFFD600),
                    fontSize = 12.sp,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                )
            }
        }
    }
}