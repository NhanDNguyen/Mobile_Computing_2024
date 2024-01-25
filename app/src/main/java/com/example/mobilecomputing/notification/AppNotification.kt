package com.example.mobilecomputing.notification

import android.content.Context
import androidx.core.app.NotificationCompat
import com.example.mobilecomputing.R

class NotificationService(private val context: Context) {
    var builder = NotificationCompat.Builder(context, "brightness_notification")
        .setSmallIcon(R.drawable.notification)
        .setContentTitle("Brightness notification")
        .setContentText("Changing App Theme")
        .setStyle(NotificationCompat.BigTextStyle().bigText("Chaning App Theme"))
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)

    
}