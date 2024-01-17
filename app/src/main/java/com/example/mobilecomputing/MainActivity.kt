package com.example.mobilecomputing

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.room.Room
import com.example.mobilecomputing.data.ProfileDatabase
import com.example.mobilecomputing.ui.theme.MobileComputingTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val db = Room.databaseBuilder(
                applicationContext,
                ProfileDatabase::class.java, "profiles_database"
            ).build()

            val profileDao = db.profileDao()
            val viewModel = AppViewModel(profileDao)
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