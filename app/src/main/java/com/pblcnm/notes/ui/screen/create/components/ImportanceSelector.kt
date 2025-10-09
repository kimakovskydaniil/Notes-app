package com.pblcnm.notes.ui.screen.create.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pblcnm.notes.model.Importance
import com.pblcnm.notes.ui.screen.create.NoteEntity

@Composable
fun ImportanceSelector(
    noteData: NoteEntity,
    onImportanceChange: (NoteEntity) -> Unit,
) {
    Text(
        text = "ВАЖНОСТЬ",
        color = Color(0xFF00FF9D),
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
    )

    LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        items(Importance.entries) { importance ->
            FilterChip(
                selected = noteData.importance == importance,
                onClick = {
                    onImportanceChange(noteData.copy(importance = importance))
                },
                label = {
                    Text(
                        text = importance.getInterdimensionalName(),
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Color(0xFF00FF9D),
                    selectedLabelColor = Color.Black,
                    containerColor = Color(0xFF2A2A2A),
                    labelColor = Color.White
                )
            )
        }
    }
}

fun Importance.getInterdimensionalName(): String {
    return when (this) {
        Importance.UNIMPORTANT -> "НИЗКАЯ"
        Importance.NORMAL -> "НОРМАЛЬНАЯ"
        Importance.IMPORTANT -> "ВЫСОКАЯ"
    }
}