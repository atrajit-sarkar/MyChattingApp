package com.example.mychattingapp.Utils.DateUtils

import androidx.compose.animation.animateColor
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun DateHeader(date: String) {
    val formattedDate =
        formatDateForDisplay(date) // Format date to "Today", "Yesterday", or actual date
    val isToday = formattedDate == "Today"

    StyledCard(formattedDate, isToday)


//    Text(
//        text = formattedDate,
//        color = Color.Gray,
//        modifier = Modifier
////            .fillMaxWidth()
//            .padding(vertical = 8.dp),
//        textAlign = TextAlign.Center,
//        style = MaterialTheme.typography.bodySmall
//    )

}

@Composable
fun StyledCard(formattedDate: String, isToday: Boolean) {
    // Infinite transition for pulsating scale
    val infiniteTransition = rememberInfiniteTransition()

    // Pulsating scale animation
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = if (isToday) 1.1f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "Pulsating Scale"
    )

    // Pulsating border color animation
    val borderColor by infiniteTransition.animateColor(
        initialValue = Color(0xFF4CAF50),
        targetValue = Color(0xFFB05FC2),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "Border Color Animation"
    )

    // Background color animation for today's messages
    val backgroundColor by animateColorAsState(
        targetValue = if (isToday) Color(0xFFE8F5E9) else Color(0xFFA1A4A1),
        animationSpec = tween(durationMillis = 300),
        label = "Background Color Animation"
    )

    Card(
        modifier = Modifier
            .padding(10.dp)
            .scale(scale), // Apply pulsating scale animation
        shape = RoundedCornerShape(16.dp),
        border = if (isToday) BorderStroke(2.dp, borderColor) else null, // Pulsating border
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Text(
            text = formattedDate,
            color = if (isToday) Color(0xFF4CAF50) else Color.Black, // WhatsApp green for today
            modifier = Modifier
                .padding(vertical = 8.dp, horizontal = 16.dp),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodySmall
        )
    }
}