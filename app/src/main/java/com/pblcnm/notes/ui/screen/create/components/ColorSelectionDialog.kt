package com.pblcnm.notes.ui.screen.create.components


import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp

@Composable
fun PortalColorDimensionDialog(
    startingPortalColor: Color,
    onPortalColorChosen: (Color) -> Unit,
    onClosePortal: () -> Unit,
) {
    var colorState by remember {
        mutableStateOf(ColorState(startingPortalColor, 1f))
    }

    AlertDialog(
        onDismissRequest = onClosePortal,
        confirmButton = {
            Button(onClick = {
                onPortalColorChosen(colorState.toFinal())
                onClosePortal()
            }) {
                Text(text = "Выбрать")
            }
        },
        text = {
            ColorPickerBody(
                colorState = colorState,
                onColorChange = { colorState = it },
                modifier = Modifier.padding(16.dp)
            )
        }
    )
}

@Composable
private fun ColorPickerBody(
    colorState: ColorState,
    onColorChange: (ColorState) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        ColorPreviewWithBrightness(
            color = colorState.color,
            brightness = colorState.brightness,
            onBrightnessChange = { onColorChange(colorState.copy(brightness = it)) }
        )
        HueGradientSelector(
            selectedColor = colorState.color,
            onColorSelected = { onColorChange(colorState.copy(color = it)) }
        )
    }
}

@Composable
private fun ColorPreviewWithBrightness(
    color: Color,
    brightness: Float,
    onBrightnessChange: (Float) -> Unit,
) {
    val hsv = FloatArray(3)
    android.graphics.Color.colorToHSV(color.toArgb(), hsv)
    hsv[2] = brightness.coerceIn(0f, 1f)

    val adjustedColor = Color(android.graphics.Color.HSVToColor(hsv))

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(adjustedColor)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Slider(
            value = brightness,
            onValueChange = onBrightnessChange,
            valueRange = 0.2f..1f,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun HueGradientSelector(
    selectedColor: Color,
    onColorSelected: (Color) -> Unit,
) {
    var selectorX by remember { mutableFloatStateOf(0f) }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { offset ->
                        selectorX = offset.x.coerceIn(0f, size.width.toFloat())
                        val hue = (selectorX / size.width) * 360f
                        onColorSelected(Color.hsv(hue, 1f, 1f))
                    },
                    onDrag = { change, _ ->
                        selectorX = change.position.x.coerceIn(0f, size.width.toFloat())
                        val hue = (selectorX / size.width) * 360f
                        onColorSelected(Color.hsv(hue, 1f, 1f))
                    }
                )
            }
    ) {
        val widthPx = constraints.maxWidth.toFloat()

        LaunchedEffect(selectedColor) {
            val hsv = FloatArray(3)
            android.graphics.Color.colorToHSV(selectedColor.toArgb(), hsv)
            selectorX = (hsv[0] / 360f) * widthPx
        }

        GradientCanvas()

        Canvas(modifier = Modifier.fillMaxSize()) {
            val selectorRadius = 12.dp.toPx()
            val centerY = size.height / 2f
            val centerX = selectorX

            val crossSize = selectorRadius * 0.6f
            drawLine(
                color = Color.White,
                start = Offset(centerX - crossSize, centerY),
                end = Offset(centerX + crossSize, centerY),
                strokeWidth = 3f
            )
            drawLine(
                color = Color.White,
                start = Offset(centerX, centerY - crossSize),
                end = Offset(centerX, centerY + crossSize),
                strokeWidth = 3f
            )
        }
    }
}


@Composable
private fun GradientCanvas() {
    val gradientColors = remember {
        listOf(
            Color.Red,
            Color.Yellow,
            Color.Green,
            Color.Cyan,
            Color.Blue,
            Color.Magenta,
            Color.Red
        )
    }

    Canvas(modifier = Modifier.fillMaxSize()) {
        drawRect(
            brush = Brush.horizontalGradient(
                colors = gradientColors,
                tileMode = TileMode.Clamp
            )
        )
    }
}

private data class ColorState(
    val color: Color,
    val brightness: Float,
) {
    fun toFinal(): Color {
        val hsv = FloatArray(3)
        android.graphics.Color.colorToHSV(color.toArgb(), hsv)
        hsv[2] = brightness.coerceIn(0f, 1f)
        return Color(android.graphics.Color.HSVToColor(hsv))
    }
}