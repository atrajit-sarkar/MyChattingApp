package com.example.mychattingapp.widgets.HomeScreenWidgets

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ChatRow(
    contactName: String,
    recentMessage: String,
    messageSentTime: String,
    onClick: () -> Unit = {},
    onLongPress: () -> Unit = {},
    messageCounterEnabled: Boolean = true,
    messageCounterNumber: String = "1",
    profilePicUri: String = "",
    messageDeliveryIcon: @Composable () -> Unit = {}
) {

    val showProfilePicDialogues = remember {
        mutableStateOf(false)
    }

    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
            .height(69.dp)
            .combinedClickable(
                onLongClick = {
                    onLongPress()
                }
            ) {
                onClick()
            }
    ) {
        // Profile Picture
        if (profilePicUri.isEmpty()) {
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = null,
                tint = Color.LightGray,
                modifier = Modifier
                    .size(50.dp)
                    .clickable {
                        showProfilePicDialogues.value = true
                    }
            )
        } else {
            Image(
                painter = rememberAsyncImagePainter(
                    model = "https://raw.githubusercontent.com/gongobongofounder/MyChattingAppProfilePics/main/${profilePicUri}"
                ),
                contentDescription = "WhatsApp DP",
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .border(2.dp, Color.LightGray, CircleShape)
                    .clickable {
                        showProfilePicDialogues.value = true
                    },
                contentScale = ContentScale.Crop
            )
        }

        // Show profile picture dialog if enabled
        if (showProfilePicDialogues.value) {
            ProfileDialogDemo(
                isVisible = showProfilePicDialogues.value,
                profilePicUri = profilePicUri,
                contactName = contactName,
                onDismiss = { showProfilePicDialogues.value = false }
            )
        }

        Spacer(modifier = Modifier.width(5.dp))

        // Chat content
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .width(200.dp)
                    .height(79.dp)
            ) {
                Text(
                    text = contactName,
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )

                Row(
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (recentMessage.isNotEmpty()) {
                        messageDeliveryIcon()
                        Spacer(modifier = Modifier.width(7.dp))

                        Text(
                            text = recentMessage,
                            style = MaterialTheme.typography.labelLarge,
                            color = Color.LightGray,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1
                        )
                    }
                }
            }

            // Time and message counter
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(end = 8.dp)
            ) {
                if (recentMessage.isNotEmpty()) {
                    Text(
                        text = messageSentTime,
                        color = if (messageCounterNumber == "0") Color.LightGray else Color.Green,
                        fontSize = 12.sp
                    )
                    if (messageCounterNumber != "0") {
                        Card(
                            shape = CircleShape,
                            colors = CardDefaults.cardColors(Color.Green),
                            modifier = Modifier.size(23.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Color.Green),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = messageCounterNumber,
                                    fontSize = 13.sp,
                                    color = MaterialTheme.colorScheme.inversePrimary,
                                    textAlign = TextAlign.Center,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileDialogDemo(
    isVisible: Boolean,
    profilePicUri: String,
    contactName: String,
    onDismiss: () -> Unit
) {
    // Dialog with animations
    AnimatedVisibility(
        visible = isVisible,
        enter = slideInHorizontally(initialOffsetX = { fullWidth -> -fullWidth / 2 }) +
                fadeIn(),
        exit = slideOutVertically(targetOffsetY = { fullHeight -> fullHeight / 2 }) +
                fadeOut()
    ) {
        Dialog(onDismissRequest = { onDismiss() }) {
            Surface(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .size(250.dp),
                color = MaterialTheme.colorScheme.background
            ) {
                Box {
                    if (profilePicUri.isEmpty()) {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = null,
                            tint = Color.LightGray,
                            modifier = Modifier.size(50.dp)
                        )
                    } else {
                        Image(
                            painter = rememberAsyncImagePainter(
                                model = "https://raw.githubusercontent.com/gongobongofounder/MyChattingAppProfilePics/main/${profilePicUri}"
                            ),
                            contentDescription = "WhatsApp DP",
                            modifier = Modifier
                                .size(250.dp)
                                .clip(RoundedCornerShape(6.dp)),
                            contentScale = ContentScale.Crop
                        )
                    }
                    Box(
                        modifier = Modifier
                            .background(Color.Black.copy(alpha = 0.3f))
                            .fillMaxWidth()
                            .height(30.dp)
                    ) {
                        Text(
                            text = contactName,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier
                                .padding(top = 6.dp, start = 8.dp)
                                .clip(RoundedCornerShape(6.dp)),
                            fontSize = 15.sp
                        )
                    }
                }
            }
        }
    }
}
