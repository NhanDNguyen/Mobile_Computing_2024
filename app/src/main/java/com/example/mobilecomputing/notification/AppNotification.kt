package com.example.mobilecomputing.notification

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.mobilecomputing.MainActivity
import com.example.mobilecomputing.R


const val PRIORITY_HIGH = NotificationCompat.PRIORITY_HIGH
const val PRIORITY_DEFAULT = NotificationCompat.PRIORITY_DEFAULT
const val PRIORITY_LOW = NotificationCompat.PRIORITY_LOW
const val PRIORITY_MAX = NotificationCompat.PRIORITY_MAX

const val REQUEST_CODE = 200
class NotificationService(private val context: Context) {

    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    private val myIntent = Intent(context, MainActivity::class.java)
    private val pendingIntent = PendingIntent.getActivity(
        context,
        REQUEST_CODE,
        myIntent,
        PendingIntent.FLAG_IMMUTABLE
    )


    fun showNotification(
        ID: Int,
        CHANNEL_ID: String,
        ic_launcher_forground_id: Int,
        title: String,
        text: String,
        priority: Int,
        bigText: String = ""
        ) {
        val notification = NotificationCompat
            .Builder(context, CHANNEL_ID)
            .setSmallIcon(ic_launcher_forground_id)
            .setContentTitle(title)
            .setContentText(text)
            .setPriority(priority)
            .setContentIntent(pendingIntent)
            .setStyle(NotificationCompat.BigTextStyle().bigText(bigText))
            .build()
        notificationManager.notify(ID, notification)
    }
}
