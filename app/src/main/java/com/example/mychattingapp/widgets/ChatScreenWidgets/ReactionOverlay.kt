package com.example.mychattingapp.widgets.ChatScreenWidgets

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties

@Composable
fun ReactionOverlay(
    isVisible: Boolean,
    onReactionSelect: (String) -> Unit,
    onDismiss: () -> Unit
) {
    // Track the currently selected reaction
    val selectedReaction = remember { mutableStateOf<String?>(null) }
    val scale by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(durationMillis = 700)
    )
    val isSelected = remember {
        mutableStateOf(false)
    }
    Popup(
        alignment = Alignment.TopCenter,
        onDismissRequest = onDismiss,
        offset = IntOffset(y = -69, x = 0),
        properties = PopupProperties(
            focusable = false,
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
            usePlatformDefaultWidth = true,
            clippingEnabled = true,
            excludeFromSystemGesture = false
        )
    ) {

        Card(
            modifier = Modifier
                .clip(RoundedCornerShape(24.dp))
                .height(45.dp)
                .graphicsLayer {
                    scaleX = scale // Horizontal fold animation
                    // Keep vertical scale constant
                },
            colors = CardDefaults.cardColors(MaterialTheme.colorScheme.background),
            elevation = CardDefaults.cardElevation(20.dp),
            border = BorderStroke(width = 1.dp, color = Color.DarkGray)
        ) {
            LazyRow(
                modifier = Modifier
                    .background(
                        MaterialTheme.colorScheme.background,
                        shape = RoundedCornerShape(24.dp)
                    )
                    .padding(horizontal = 6.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(SampleReactionList) { reaction ->

                    isSelected.value = selectedReaction.value == reaction

                    Card(
                        colors = CardDefaults.cardColors(containerColor = if (isSelected.value) Color.Green else Color.Transparent)
                    ) {

                        Text(
                            text = reaction,
                            fontSize = 24.sp,
                            modifier = Modifier
                                .clickable {
                                    selectedReaction.value = reaction // Mark this as selected
                                    onReactionSelect(reaction) // Select a reaction

                                }
                        )
                    }
                }
            }
        }

    }
}