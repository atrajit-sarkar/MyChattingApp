package com.example.mychattingapp.Utils.Network

import android.content.Context
import android.net.ConnectivityManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.lifecycle.ProcessLifecycleOwner
import com.example.mychattingapp.LocaldbLogics.ViewModel.ChatAppViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun NetworkAwareComponent(
    context: Context,
    viewModel: ChatAppViewModel
) {
    val connectivityManager = remember {
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }
    val connectivityObserver = remember {
        ConnectivityObserver(connectivityManager).apply {
            ProcessLifecycleOwner.get().lifecycle.addObserver(this)
        }
    }
    val isConnected = connectivityObserver.isConnected.collectAsState()

    LaunchedEffect(isConnected.value) {
        if (isConnected.value) {
            // Code to execute when connected to the internet
            val updateOnline = mapOf(
                "activeStatus" to "Online"
            )
            viewModel.updateUserItem(viewModel.currentUserId.value, updateOnline)
        } else {
            val currentTime = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
            // Code to execute when offline
            val updateOnline = mapOf(
                "activeStatus" to "last seen at $currentTime"
            )
            viewModel.updateUserItem(viewModel.currentUserId.value, updateOnline)
        }
    }

    // Optionally update UI
    if (isConnected.value) {
        // Online UI
    } else {
        // Offline UI
    }
}
