package com.example.mychattingapp.widgets.ChatThemeWidgets

import android.util.Log
import android.graphics.Color as AndroidColor
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp

@Composable
fun ColorWheelPicker(
    modifier: Modifier = Modifier,
    hue: MutableState<Float>,
    offSet: MutableState<Offset?> = mutableStateOf(Offset(0f, 0f)),
    onColorSelected: (Long) -> Unit = {
        Log.d("PickedColor", "AdvancedColorPicker: ${Color(it)}")
    }
) {
    val boxWidth = 300.dp
    val boxHeight = 200.dp
    val boxSizePx = with(LocalDensity.current) { boxWidth.toPx() to boxHeight.toPx() }

    var selectedColor by remember { mutableStateOf(Color.Black) }
    var selectedOffset by remember { offSet }
    var hue by remember { hue } // Default hue is blue
    var saturation by remember { mutableStateOf(1f) }
    var brightness by remember { mutableStateOf(1f) }

    Column(
        modifier = modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Color Box
        Box(
            modifier = Modifier
                .size(boxWidth, boxHeight)
                .pointerInput(Unit) {
                    detectTapAndDragGestures(
                        boxSizePx = boxSizePx,
                        hue = hue,
                        onColorUpdated = { offset, sat, bri ->
                            saturation = sat
                            brightness = bri
                            selectedOffset = offset
                            selectedColor = Color.hsv(hue, saturation, brightness)
                            onColorSelected(selectedColor.toHexLong())
                        }
                    )
                }
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                // Horizontal gradient for saturation
                drawRect(
                    brush = Brush.horizontalGradient(
                        colors = listOf(Color.White, Color.hsv(hue, 1f, 1f))
                    )
                )
                // Vertical gradient for brightness
                drawRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black)
                    )
                )
                // Draw the indicator at the selected position
                selectedOffset?.let { offset ->
                    drawCircle(
                        color = Color.White,
                        radius = 8.dp.toPx(),
                        center = offset,
                        style = Stroke(width = 2.dp.toPx())
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Hue Slider with Color Gradient
        SliderWithColorBar(
            label = "Hue",
            value = hue,
            onValueChange = {
                hue = it
                selectedColor = Color.hsv(hue, saturation, brightness)
                onColorSelected(selectedColor.toHexLong())
            },
            range = 0f..360f
        )

        Spacer(modifier = Modifier.height(16.dp))

    }
}

private suspend fun PointerInputScope.detectTapAndDragGestures(
    boxSizePx: Pair<Float, Float>,
    hue: Float,
    onColorUpdated: (Offset, Float, Float) -> Unit
) {
    val (widthPx, heightPx) = boxSizePx

    awaitPointerEventScope {
        while (true) {
            val event = awaitPointerEvent()
            val position = event.changes.first().position

            val constrainedOffset = Offset(
                x = position.x.coerceIn(0f, widthPx),
                y = position.y.coerceIn(0f, heightPx)
            )

            val localSaturation = (constrainedOffset.x / widthPx).coerceIn(0f, 1f)
            val localBrightness = (1f - constrainedOffset.y / heightPx).coerceIn(0f, 1f)

            onColorUpdated(constrainedOffset, localSaturation, localBrightness)

            // Consume the gesture to prevent conflicts
            event.changes.forEach { it.consume() }
        }
    }
}


// Slider with a Color Gradient Bar
@Composable
fun SliderWithColorBar(
    label: String,
    value: Float,
    onValueChange: (Float) -> Unit,
    range: ClosedFloatingPointRange<Float>
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("$label: ${value.format(0)}")
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(30.dp)
                .background(
                    brush = Brush.horizontalGradient(
                        colors = List(361) { Color.hsv(it.toFloat(), 1f, 1f) }
                    ),
                    shape = RoundedCornerShape(8.dp)
                )
                .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
        ) {
            Slider(
                value = value,
                onValueChange = onValueChange,
                valueRange = range,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(30.dp)
                    .align(Alignment.Center)
            )
        }
    }
}

fun Float.format(digits: Int) = "%.${digits}f".format(this)

fun Color.toHexLong(): Long {
    val intColor = AndroidColor.argb(
        (alpha * 255).toInt(),
        (red * 255).toInt(),
        (green * 255).toInt(),
        (blue * 255).toInt()
    )
    return intColor.toLong() and 0xFFFFFFFF
}
