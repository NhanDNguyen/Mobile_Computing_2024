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
import com.example.mobilecomputing.Screen.DetailsScreen
import com.example.mobilecomputing.Screen.EditScreen
import com.example.mobilecomputing.Screen.EntryScreen
import com.example.mobilecomputing.Screen.HomeScreen
import com.example.mobilecomputing.Screen.SettingsScreen

enum class AppScreen(@StringRes val title: Int) {
    Home(title = R.string.home),
    Entry(title = R.string.profile_entry),
    Details(title = R.string.profile_details),
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
                navigateToProfileEntry = {
                    viewModel.resetProfileUiState()
                    navController.navigate(route = AppScreen.Entry.name)
                },
                navigateToProfileUpdate = {
                    viewModel.updateProfileUiState(it)
                    navController.navigate(route = AppScreen.Details.name)
                },
                navigateToAppSettings = {
                    navController.navigate(route = AppScreen.Settings.name)
                },
                viewModel = viewModel
            )
        }
        composable(route = AppScreen.Entry.name) {
            EntryScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() },
                viewModel = viewModel
            )
        }
        composable(route = AppScreen.Details.name) {
            DetailsScreen(
                navigateToEditProfile = {
                    viewModel.updateProfileUiState(it)
                    navController.navigate(route = AppScreen.Edit.name)
                },
                navigateBack = { navController.navigateUp() },
                viewModel = viewModel
            )
        }
        composable(route = AppScreen.Edit.name) {
            EditScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() },
                viewModel = viewModel
            )
        }
        composable(route = AppScreen.Settings.name) {
            SettingsScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() },
                viewModel = viewModel
            )
        }
    }
}