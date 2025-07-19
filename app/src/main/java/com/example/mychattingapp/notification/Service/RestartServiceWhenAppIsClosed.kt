package com.example.mychattingapp.notification.Service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat

class RestartServiceReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        val serviceIntent = Intent(context, MessageService::class.java)
        ContextCompat.startForegroundService(context, serviceIntent)
    }

}
