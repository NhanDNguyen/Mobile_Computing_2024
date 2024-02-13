package com.example.mobilecomputing

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import androidx.room.Room
import com.example.mobilecomputing.Screen.EntryScreen
import com.example.mobilecomputing.data.NoteDatabase
import com.example.mobilecomputing.notification.NotificationService
import com.example.mobilecomputing.notification.REQUEST_CODE
import com.example.mobilecomputing.ui.theme.MobileComputingTheme

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val db = Room.databaseBuilder(
                applicationContext,
                NoteDatabase::class.java, "profiles_database"
            ).build()
            val notificationService = NotificationService(applicationContext)
            val notificationChannel = NotificationChannel(
                "notification_theme",
                "Changing Theme",
                NotificationManager.IMPORTANCE_HIGH
            )
            with(NotificationManagerCompat.from(applicationContext)) {
                if (ActivityCompat.checkSelfPermission(
                        this@MainActivity,
                        android.Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED)  {
                    ActivityCompat.requestPermissions(
                        this@MainActivity,
                        arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                        REQUEST_CODE
                    )
                }
            }
            notificationService.notificationManager.createNotificationChannel(notificationChannel)
            val profileDao = db.noteDao()
            val viewModel = AppViewModel(profileDao, notificationService)
            NoteApp(viewModel = viewModel)
            //EntryScreen(viewModel = viewModel, onNavigateUp = { /*TODO*/ }, navigateBack = { /*TODO*/ })
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MobileComputingTheme {

    }
}