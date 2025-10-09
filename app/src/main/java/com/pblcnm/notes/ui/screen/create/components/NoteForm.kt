package com.pblcnm.notes.ui.screen.create.components

import android.app.DatePickerDialog
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pblcnm.notes.ui.screen.create.NoteEntity
import com.pblcnm.notes.ui.screen.create.formattedDate
import com.pblcnm.notes.ui.theme.neonGreenColor
import com.pblcnm.notes.ui.theme.rickColor
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.Calendar

@Composable
fun NoteForm(
    noteData: NoteEntity,
    onNoteDataChange: (NoteEntity) -> Unit = {},
    modifier: Modifier = Modifier,
) {
    var isPortalColorPickerVisible by remember { mutableStateOf(false) }
    var currentPortalColor by remember { mutableStateOf(Color(noteData.color)) }

    var showTemporalSelector by remember { mutableStateOf(false) }
    var showSelfDestructButton by remember { mutableStateOf(false) }


    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        OutlinedTextField(
            value = noteData.title,
            onValueChange = { onNoteDataChange(noteData.copy(title = it)) },
            label = {
                Text(
                    text = "НАЗВАНИЕ ЗАМЕТКИ",
                    color = rickColor
                )
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                capitalization = KeyboardCapitalization.Sentences
            ),
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedBorderColor = rickColor,
                unfocusedBorderColor = Color(0xFF555555),
                focusedLabelColor = rickColor,
                unfocusedLabelColor = Color(0xFF888888),
                cursorColor = rickColor,
            )
        )

        OutlinedTextField(
            value = noteData.content,
            onValueChange = {
                onNoteDataChange(noteData.copy(content = it))
            },
            label = {
                Text(
                    text = "ОПИСАНИЕ АЛЬТЕРНАТИВНОЙ РЕАЛЬНОСТИ",
                    color = rickColor
                )
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Default,
                capitalization = KeyboardCapitalization.Sentences
            ),
            modifier = Modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = 120.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedBorderColor = rickColor,
                unfocusedBorderColor = Color(0xFF555555),
                focusedLabelColor = rickColor,
                unfocusedLabelColor = Color(0xFF888888),
                cursorColor = rickColor,
            )
        )

        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = noteData.expirationDate != null,
                    onCheckedChange = { isChecked ->
                        if (isChecked) {
                            showSelfDestructButton = true
                        } else {
                            onNoteDataChange(noteData.copy(expirationDate = null))
                            showSelfDestructButton = false
                        }
                    },
                    colors = CheckboxDefaults.colors(
                        checkedColor = neonGreenColor,
                        uncheckedColor = Color(0xFF555555)
                    )
                )
                Text(
                    text = "АКТИВИРОВАТЬ САМОУНИЧТОЖЕНИЕ",
                    color = Color.White
                )
            }
            if (showSelfDestructButton) {
                Button(
                    onClick = { showTemporalSelector = true },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFF6B6B)
                    )
                ) {
                    Text(
                        text = "ВЫБРАТЬ ВРЕМЯ УНИЧТОЖЕНИЯ",
                        fontWeight = FontWeight.Bold
                    )
                }
                if (noteData.expirationDate != null) {
                    Text(
                        text = "Временная метка уничтожения: ${noteData.expirationDate.formattedDate()}",
                        color = Color(0xFFFFD600),
                        fontSize = 12.sp
                    )
                }
            }
        }

        PortalColorSelector(
            selectedPortalColor = currentPortalColor,
            onPortalColorSelected = { newColor ->
                currentPortalColor = newColor
                onNoteDataChange(noteData.copy(color = newColor.toArgb()))
            },
            onExpandPortalPalette = { isPortalColorPickerVisible = true }
        )

        ImportanceSelector(
            noteData = noteData,
            onImportanceChange = onNoteDataChange
        )

        if (isPortalColorPickerVisible) {
            PortalColorPickerDialog(
                startingPortalColor = currentPortalColor,
                onPortalColorChosen = { newColor ->
                    currentPortalColor = newColor
                    onNoteDataChange(noteData.copy(color = newColor.toArgb()))
                    isPortalColorPickerVisible = false
                },
                onClosePortal = { isPortalColorPickerVisible = false }
            )
        }

        if (showTemporalSelector) {
            val context = LocalContext.current
            val calendar = Calendar.getInstance()

            DatePickerDialog(
                context,
                { _, year, month, day ->
                    val localDateTime = LocalDateTime.of(
                        year,
                        month + 1,
                        day,
                        0,
                        0
                    )
                    val timestamp = localDateTime.toEpochSecond(ZoneOffset.UTC)
                    onNoteDataChange(noteData.copy(expirationDate = timestamp))
                    showTemporalSelector = false
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }
}