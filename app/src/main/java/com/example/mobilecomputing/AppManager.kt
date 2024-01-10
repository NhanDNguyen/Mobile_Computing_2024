package com.example.mobilecomputing

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.mobilecomputing.data.messages
import androidx.lifecycle.viewmodel.compose.viewModel

enum class AppScreen(@StringRes val title: Int) {
    Start(title = R.string.message),
    Biography(title = R.string.biography),
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    currentScreen: AppScreen,
    navigateBack: Boolean,
    navigateUp:() -> Unit,
    modifier: Modifier=Modifier
) {
    TopAppBar(
        title = { Text(stringResource(id = currentScreen.title)) },
        colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = MaterialTheme.colorScheme.primary),
        modifier = modifier,
        navigationIcon = {
            if(navigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }
        }
    )
}

@Composable
fun App(
    bioViewModel: BioViewModel = viewModel(),
    navController: NavHostController = rememberNavController()
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = AppScreen.valueOf(
        backStackEntry?.destination?.route?: AppScreen.Start.name
    )

    Scaffold(
        topBar = {
            AppBar(
                currentScreen = currentScreen,
                navigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() })
        }
    ) { innerPadding ->
        val bioUiState by bioViewModel.uiState.collectAsState()
        NavHost(
            navController = navController,
            startDestination = AppScreen.Start.name,
            modifier = Modifier.padding(innerPadding),
        ) {
            composable(route = AppScreen.Start.name) {
                ImageContainerScreen(
                    messages = messages,
                    onClick = {
                        bioViewModel.setData(it)
                        navController.navigate(route = AppScreen.Biography.name)
                    }
                )
            }
            composable(route = AppScreen.Biography.name) {
                BioScreen(
                    message = bioUiState.message,
                    onClick = {
                        navController.navigate(route = AppScreen.Start.name)
                    }
                )
            }

        }
    }

}

























