package com.example.mychattingapp.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build

fun createNotificationChannel(context: Context) {
    val channel = NotificationChannel(
        "chat_notifications",
        "Chat Notifications",
        NotificationManager.IMPORTANCE_HIGH
    ).apply {
        description = "Notifications for new messages"
    }
    val notificationManager: NotificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.createNotificationChannel(channel)
}
