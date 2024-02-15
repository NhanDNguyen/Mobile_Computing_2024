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
            val viewModel = AppViewModel(
                noteDB = db.noteDao(),
                textNoteDB = db.textNoteDao(),
                imageNoteDB = db.imageNoteDao(),
                audioNoteDB = db.audioNoteDao()
            )
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