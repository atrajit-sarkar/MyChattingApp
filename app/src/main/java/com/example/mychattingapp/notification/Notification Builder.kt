package com.example.mychattingapp.notification

import android.Manifest
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.mychattingapp.MainActivity
import com.example.mychattingapp.R
import com.squareup.wire.Service

fun sendChatNotification(context: Context, message: String, title: String) {
    val channelId = "chat_notifications" // Unique channel ID
    val channelName = "Chat Notifications" // Human-readable channel name

    // Step 1: Create Notification Channel for Android 8+ (API 26+)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(channelId, channelName, importance).apply {
            description = "Notifications for chat messages"
            enableVibration(true)
        }
        notificationManager.createNotificationChannel(channel)
    }

    // Step 2: Check Notification Permission for Android 13+ (API 33+)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
        ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.POST_NOTIFICATIONS
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        if (context is Activity) {
            ActivityCompat.requestPermissions(
                context,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                PERMISSIONS_REQUEST_CODE
            )
        }
        return
    }

    // Step 3: Create the Notification
    val pendingIntent: PendingIntent = PendingIntent.getActivity(
        context,
        0,
        Intent(context, MainActivity::class.java), // Replace with your activity
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    val builder = NotificationCompat.Builder(context, channelId)
        .setSmallIcon(R.mipmap.appicon) // Replace with your app icon
        .setContentTitle(title)
        .setContentText(message)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setContentIntent(pendingIntent)
        .setAutoCancel(true)
        .setBadgeIconType(NotificationCompat.BADGE_ICON_LARGE)
        .setSound(android.media.RingtoneManager.getDefaultUri(android.media.RingtoneManager.TYPE_NOTIFICATION))
//        .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))

    // Step 4: Display the Notification
    with(NotificationManagerCompat.from(context)) {
        notify((System.currentTimeMillis() % Int.MAX_VALUE).toInt(), builder.build())
    }

}

const val PERMISSIONS_REQUEST_CODE = 101



