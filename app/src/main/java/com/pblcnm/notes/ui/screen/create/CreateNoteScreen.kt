package com.pblcnm.notes.ui.screen.create

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.pblcnm.notes.ui.screen.create.components.NoteForm

@Composable
fun CreateNoteScreen(
    onBackToDimension: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CreateNoteViewModel = hiltViewModel(),
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(
                horizontal = 16.dp,
                vertical = 12.dp
            ),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        NoteForm(
            noteData = viewModel.uiState.selectedNote,
            onNoteDataChange = { viewModel.processAction(CreateNoteAction.ModifyNoteData(it)) },
            modifier = modifier
        )

        Button(
            onClick = {
                viewModel.processAction(CreateNoteAction.CreateNote)
                onBackToDimension()
            },
            enabled = viewModel.uiState.isValid,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF00FF9D),
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