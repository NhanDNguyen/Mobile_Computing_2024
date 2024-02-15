package com.example.mobilecomputing.navigation

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mobilecomputing.AppViewModel
import com.example.mobilecomputing.R
import com.example.mobilecomputing.Screen.EntryScreen
import com.example.mobilecomputing.Screen.HomeScreen
import com.example.mobilecomputing.Screen.ImageScreen
import com.example.mobilecomputing.toNoteDetails

/*enum class AppScreen(@StringRes val title: Int) {
    Home(title = R.string.home),
    Entry(title = R.string.note_entry),
    Details(title = R.string.note_details),
    Edit(title = R.string.profile_edit),
    Settings(title = R.string.app_settings)
}*/

enum class AppScreen {
    Home, Text, Image, Audio, Drawing
}

enum class NoteOption {
    Entry, Update
}

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    viewModel: AppViewModel
) {
    var noteOption by remember { mutableStateOf(NoteOption.Entry) }
    NavHost(
        navController = navController,
        startDestination = AppScreen.Home.name,
        modifier = modifier
    ) {
        composable(route = AppScreen.Home.name) {
            HomeScreen(
                navigateToNoteEntry = { noteType ->
                    noteOption = NoteOption.Entry
                    when (noteType) {
                        AppScreen.Text -> {
                            viewModel.resetNoteUiState("text")
                            navController.navigate(route = AppScreen.Text.name)
                        }
                        AppScreen.Image -> {
                            viewModel.resetNoteUiState("image")
                            navController.navigate(route = AppScreen.Image.name)
                        }
                        AppScreen.Audio -> {
                            viewModel.resetNoteUiState("audio")
                            navController.navigate(route = AppScreen.Audio.name)
                        }
                        AppScreen.Drawing -> {
                            viewModel.resetNoteUiState("drawing")
                            navController.navigate(route = AppScreen.Drawing.name)
                        }
                        else -> {}
                    }
                },
                navigateToNoteUpdate = { note ->
                    noteOption = NoteOption.Update
                    viewModel.updateNoteUiState(note.toNoteDetails())
                    when (note.type) {
                        "text" -> {
                            navController.navigate(route = AppScreen.Text.name)
                        }
                        "image" -> {
                            navController.navigate(route = AppScreen.Image.name)
                        }
                        "audio" -> {
                            navController.navigate(route = AppScreen.Audio.name)
                        }
                        "drawing" -> {
                            navController.navigate(route = AppScreen.Drawing.name)
                        }
                        else -> {}
                    }
                },
                viewModel = viewModel
            )
        }
        composable(route = AppScreen.Text.name) {
            EntryScreen(
                viewModel = viewModel,
                onNavigateUp = { navController.navigateUp() },
                navigateBack = { navController.popBackStack() },
                noteOption = noteOption
            )
        }
        composable(route = AppScreen.Image.name) {
            ImageScreen(
                viewModel = viewModel,
                onNavigateUp = { navController.navigateUp() },
                navigateBack = { navController.popBackStack() },
                noteOption = noteOption
            )
        }
        composable(route = AppScreen.Audio.name) {

        }
        composable(route = AppScreen.Drawing.name) {

        }
    }
}