package com.pblcnm.notes.ui.screen.create.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PortalColorSelector(
    selectedPortalColor: Color,
    onPortalColorSelected: (Color) -> Unit,
    onExpandPortalPalette: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var currentPortalColor by remember { mutableStateOf(selectedPortalColor) }
    val dimensionColors = listOf(
        Color(0xFFFF6B6B),
        Color(0xFF4ECDC4),
        Color(0xFFFFD166),
        Color(0xFF06D6A0),
        Color(0xFF118AB2),
        Color(0xFF9D4EDD),
    )

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = "ЦВЕТ ПОРТАЛА",
            color = Color(0xFF00FF9D),
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            dimensionColors.forEach { color ->
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(38.dp)
                        .background(color, androidx.compose.foundation.shape.CircleShape)
                        .border(
                            width = if (color == currentPortalColor) 3.dp else 0.dp,
                            color = if (color == currentPortalColor) Color(0xFFFFD600) else Color.Transparent,
                            shape = androidx.compose.foundation.shape.CircleShape
                        )
                        .clickable {
                            currentPortalColor = color
                            onPortalColorSelected(color)
                        }
                ) {
                    if (color == currentPortalColor) {
                        Text(
                            text = "✓",
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .background(
                        Brush.horizontalGradient(
                            listOf(
                                Color.Red,
                                Color.Yellow,
                                Color.Green,
                                Color.Cyan,
                                Color.Blue,
                                Color.Magenta
                            )
                        ),
                        androidx.compose.foundation.shape.CircleShape
                    )
                    .clickable { onExpandPortalPalette() },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "+",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}