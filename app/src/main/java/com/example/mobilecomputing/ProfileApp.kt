package com.example.mobilecomputing

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.mobilecomputing.navigation.AppNavHost
import com.example.mobilecomputing.sensor.LightSensor
import com.example.mobilecomputing.sensor.LightSensorOperation
import com.example.mobilecomputing.ui.theme.MobileComputingTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileTopAppBar(
    modifier: Modifier = Modifier,
    title: String,
    canNavigateBack: Boolean,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    navigateUp: () -> Unit = {},
) {
    CenterAlignedTopAppBar(
        title = { Text(text = title) },
        modifier = modifier,
        scrollBehavior = scrollBehavior,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back")
                }
            }
        }
    )
}

@Composable
fun ProfileApp(
    modifier: Modifier = Modifier,
    viewModel: AppViewModel
) {
    val appSettingsUiState by viewModel.appSettingsUiState.collectAsState()
    val lightSensor = LightSensor(LocalContext.current)
    MobileComputingTheme(darkTheme = appSettingsUiState.appSettings.isDark) {
        LightSensorOperation(
            lightSensor = lightSensor,
            viewModel = viewModel,
            delay = 0,
        )
        AppNavHost(
            viewModel = viewModel,
            modifier = modifier
        )
    }
}