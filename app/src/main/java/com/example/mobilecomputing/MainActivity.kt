package com.example.mobilecomputing

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.room.Room
import com.example.mobilecomputing.data.ProfileDatabase
import com.example.mobilecomputing.notification.NotificationService
import com.example.mobilecomputing.ui.theme.MobileComputingTheme

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val db = Room.databaseBuilder(
                applicationContext,
                ProfileDatabase::class.java, "profiles_database"
            ).build()
            val notificationService = NotificationService(applicationContext)
            val notificationChannel = NotificationChannel(
                "notification_theme",
                "Changing Theme",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationService.notificationManager.createNotificationChannel(notificationChannel)
            val profileDao = db.profileDao()
            val viewModel = AppViewModel(profileDao, notificationService)
            ProfileApp(viewModel = viewModel)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MobileComputingTheme {

    }
}