package com.example.mychattingapp.notification.Service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.mychattingapp.LocaldbLogics.ViewModel.FetchChatsRepository
import com.example.mychattingapp.MainActivity
import com.example.mychattingapp.R
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject




class MessageService : Service() {

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForegroundService()
        startListeningForMessages()
        return START_NOT_STICKY
    }

    private fun startForegroundService() {
        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            this, 0,
            Intent(this, MainActivity::class.java), // Replace with your app's activity
            PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.mipmap.appicon) // Replace with your icon
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        startForeground(1, notification)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Channel for chat notifications"
            }
            val manager = getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(channel)
        }
    }

    private fun startListeningForMessages() {
        // Add logic to handle incoming messages
        // Example: fetchChatsRepository.fetchChatsFunctionInit()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    companion object {
        const val channelId = "chat_notifications"
        const val channelName = "Chat Notifications"
        const val title = "App Name"
        const val message = "Listening for messages..."
    }
}

