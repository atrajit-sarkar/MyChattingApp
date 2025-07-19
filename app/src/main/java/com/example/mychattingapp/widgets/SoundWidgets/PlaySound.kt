package com.example.mychattingapp.widgets.SoundWidgets

import android.media.MediaPlayer
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext

@Composable
fun PlaySound(
    soundFile: Int
) {
    val context = LocalContext.current
    var isPlaying by remember { mutableStateOf(true) }

    // MediaPlayer setup
    val mediaPlayer = remember {
        MediaPlayer.create(context, soundFile).apply {
            setOnCompletionListener {
                isPlaying = false // Reset the state when the sound is finished
            }
        }
    }

    // Start or stop the sound based on the state
    LaunchedEffect(isPlaying) {
        if (isPlaying) {
            mediaPlayer.start()
        } else {
            mediaPlayer.pause()
        }
    }

    // Clean up MediaPlayer to release resources
    DisposableEffect(Unit) {
        onDispose {
            mediaPlayer.release()
        }
    }

    // Start the sound if not playing
//    if (!isPlaying) {
//        isPlaying = true
//    }
}

@Composable
fun StopSound(
    soundFile: Int
) {
    val context = LocalContext.current
    var isPlaying by remember { mutableStateOf(false) }

    // MediaPlayer setup
    val mediaPlayer = remember {
        MediaPlayer.create(context, soundFile).apply {
            setOnCompletionListener {
                isPlaying = false // Reset the state when the sound is finished
            }
        }
    }

    // Start or stop the sound based on the state
    LaunchedEffect(isPlaying) {
        if (isPlaying) {
            mediaPlayer.start()
        } else {
            mediaPlayer.pause()
        }
    }

    // Clean up MediaPlayer to release resources
    DisposableEffect(Unit) {
        onDispose {
            mediaPlayer.release()
        }
    }

    // Start the sound if not playing
    if (!isPlaying) {
        isPlaying = true
    }
}

