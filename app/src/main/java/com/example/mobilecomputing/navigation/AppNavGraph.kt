package com.example.mobilecomputing.navigation

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mobilecomputing.AppViewModel
import com.example.mobilecomputing.R
import com.example.mobilecomputing.Screen.EntryScreen
import com.example.mobilecomputing.Screen.HomeScreen

enum class AppScreen(@StringRes val title: Int) {
    Home(title = R.string.home),
    Entry(title = R.string.note_entry),
    Details(title = R.string.note_details),
    Edit(title = R.string.profile_edit),
    Settings(title = R.string.app_settings)
}

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    viewModel: AppViewModel
) {
    NavHost(
        navController = navController,
        startDestination = AppScreen.Home.name,
        modifier = modifier
    ) {
        composable(route = AppScreen.Home.name) {
            HomeScreen(
                navigateToNoteEntry = {
                    if (viewModel.currentOptionState == "text") {
                        viewModel.resetTextNoteUiState()
                    }
                    navController.navigate(route = AppScreen.Entry.name)
                },
                navigateToNoteUpdate = {
                    if (viewModel.currentOptionState == "text") {
                        viewModel.updateTextNoteUiState(it)
                    }
                    navController.navigate(route = AppScreen.Details.name)
                },
                viewModel = viewModel
            )
        }
        composable(route = AppScreen.Entry.name) {
            EntryScreen(
                viewModel = viewModel,
                onNavigateUp = { navController.navigateUp() },
                navigateBack = { navController.popBackStack() },
                screenName = "entry"
            )
        }
        composable(route = AppScreen.Details.name) {
            EntryScreen(
                viewModel = viewModel,
                onNavigateUp = { navController.navigateUp() },
                navigateBack = { navController.popBackStack() },
                screenName = "update"
            )
        }
        composable(route = AppScreen.Edit.name) {

        }
        composable(route = AppScreen.Settings.name) {

        }
    }
}