package com.example.mychattingapp.Media.Audio

// General Android and Compose imports

// ExoPlayer imports
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.example.mychattingapp.LocaldbLogics.ViewModel.ChatAppViewModel
import com.guru.fontawesomecomposelib.FaIcon
import com.guru.fontawesomecomposelib.FaIcons
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

//@Preview
@Composable
fun MyChattingAppAudioPlayer(
    githubFileUrl: String = "https://raw.githubusercontent.com/your-repo/audio-file.mp3",
    viewModel: ChatAppViewModel
) {
    var isPlaying by remember { mutableStateOf(false) }
    var progress by remember { mutableStateOf(0f) }
    var duration: Long by remember { mutableStateOf(0) }
    var currentTime: Long by remember { mutableStateOf(0) }
    val context = LocalContext.current
    var isPlayerReady by remember { mutableStateOf(false) }

    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(githubFileUrl))
            prepare()
            addListener(object : Player.Listener {
                override fun onPlaybackStateChanged(state: Int) {
                    if (state == Player.STATE_READY) {
                        duration = this@apply.duration
                        isPlayerReady = true
                    }
                    if (state == Player.STATE_ENDED) {
                        isPlaying = false // Reset play button
//                        seekTo(0) // Optionally reset to the start but don't play automatically
                    }
                }
            })
        }
    }

    val uploadingState by viewModel.uploadingProfilePhoto.collectAsState()
    val privateUploadingState = remember { mutableStateOf(uploadingState) }

    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }

    // UI
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(16.dp)
    ) {
        // Play/Pause Button
        if (!privateUploadingState.value) {
            IconButton(onClick = {
                if (isPlaying) {
                    exoPlayer.pause()
                    isPlaying = false
                } else {
                    exoPlayer.seekTo(0) // Reset the audio to the beginning
                    exoPlayer.play()
                    isPlaying = true
                }
            }) {
                FaIcon(
                    faIcon = if (isPlaying) FaIcons.Pause else FaIcons.Play,
                    tint = Color.LightGray
                )
            }
        } else {
            CircularProgressIndicator()
        }

        // Progress Bar and Time
        Column(modifier = Modifier.fillMaxWidth()) {
            LinearProgressIndicator(progress = progress)
            Text(
                if (isPlayerReady) "${formatTime(currentTime)} / ${formatTime(duration)}"
                else "Loading..."
            )
        }
    }

    // Track Progress
    LaunchedEffect(Unit) {
        while (true) {
            withContext(Dispatchers.Main) {
                if (exoPlayer.isPlaying) {
                    currentTime = exoPlayer.currentPosition
                    duration = exoPlayer.duration
                    progress = if (duration > 0) currentTime / duration.toFloat() else 0f
                }
            }
            delay(500L)
        }
    }
}



// Format time as mm:ss
@Composable
fun formatTime(milliseconds: Long): String {
    val minutes = (milliseconds / 1000) / 60
    val seconds = (milliseconds / 1000) % 60
    return String.format("%02d:%02d", minutes, seconds)
}


@Composable
fun SoundWaveIndicator(isPlaying: Boolean) {
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val waveHeight = infiniteTransition.animateFloat(
        initialValue = if (isPlaying) 10f else 5f,
        targetValue = if (isPlaying) 20f else 5f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )

    Canvas(modifier = Modifier.size(50.dp)) {
        for (i in 0..4) {
            drawRoundRect(
                color = Color(0xFF128C7E),
                topLeft = Offset(
                    x = (i * 12).toFloat(),
                    y = size.height / 2 - waveHeight.value / 2
                ),
                size = Size(8f, waveHeight.value),
                cornerRadius = CornerRadius(4f)
            )
        }
    }
}

private fun formatTime(milliseconds: Int): String {
    val totalSeconds = milliseconds / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return String.format("%02d:%02d", minutes, seconds)
}
