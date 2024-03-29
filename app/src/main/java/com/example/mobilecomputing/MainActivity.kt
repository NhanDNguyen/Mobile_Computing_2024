package com.example.mobilecomputing

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.room.Room
import com.example.mobilecomputing.data.NoteDatabase
import com.example.mobilecomputing.permission.isPermissionGranted
import com.example.mobilecomputing.permission.permissionGranted
import com.example.mobilecomputing.permission.permissionList
import com.example.mobilecomputing.ui.theme.MobileComputingTheme

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!isPermissionGranted(this)) {
            ActivityCompat.requestPermissions(
                this, permissionList, 0
            )
        }
        if (isPermissionGranted(this)) {
            permissionGranted = true
        }

        setContent {

            val db = Room.databaseBuilder(
                applicationContext,
                NoteDatabase::class.java, "profiles_database"
            ).build()
            val viewModel = AppViewModel( db = db.noteDao() )
            NoteApp(viewModel = viewModel)

        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MobileComputingTheme {

    }
}