package com.example.mychattingapp.ui.ChatAPPTheme.ChatScreenTheme

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ProfessionalDoodleBackground() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        // Draw a gradient background with more dynamic colors
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(Color(0xFFB2EBF2), Color(0xFFE0F7FA)),
                startY = 0f,
                endY = size.height
            ),
            size = size
        )

        // Define a professional-looking doodle path with smoother curves
        val doodlePath = Path().apply {
            moveTo(0f, 0f)
            cubicTo(20f, 60f, 80f, 60f, 100f, 0f)
            cubicTo(120f, -60f, 180f, -60f, 200f, 0f)
            lineTo(200f, 100f)
            cubicTo(180f, 160f, 120f, 160f, 100f, 100f)
            cubicTo(80f, 40f, 20f, 40f, 0f, 100f)
            close()
        }

        val doodleSpacing = 250f
        val doodleColor = Color(0xFF80DEEA)

        // Draw doodles with subtle rotations and scaling for variety
        for (xOffset in 0 until size.width.toInt() step doodleSpacing.toInt()) {
            for (yOffset in 0 until size.height.toInt() step doodleSpacing.toInt()) {
                val rotationAngle = (xOffset + yOffset) % 360
                val scaleFactorX = 1.0f + (xOffset % 100) / 500f
                val scaleFactorY = 1.0f + (yOffset % 100) / 500f
                withTransform({
                    translate(left = xOffset.toFloat(), top = yOffset.toFloat())
                    rotate(degrees = rotationAngle.toFloat(), pivot = Offset.Zero)
                    scale(
                        scaleX = scaleFactorX,
                        scaleY = scaleFactorY,
                        pivot = Offset.Zero
                    )
                }) {
                    drawPath(
                        path = doodlePath,
                        color = doodleColor,
                        style = Fill // Use filled doodles for a modern look
                    )
                }
            }
        }
    }
}




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoodleScaffold() {
    Box(modifier = Modifier.fillMaxSize()) {
        // Add the doodle background
//        DoodleBackground()

        // Add the Scaffold on top of the background
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Doodle Scaffold") },
                )
            },
            floatingActionButton = {

            }
        ) { paddingValues ->
            // Content inside the Scaffold
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
                Text(
                    text = "Hello, World!",
                    modifier = Modifier.fillMaxSize(),
                    color = Color.Black
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DoodleScaffoldPreview() {
    DoodleScaffold()
}