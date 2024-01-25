package com.example.mobilecomputing.Screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mobilecomputing.AppViewModel
import com.example.mobilecomputing.ProfileTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    canNavigateBack: Boolean = true,
    viewModel: AppViewModel
) {
    Scaffold(
        topBar = {
            ProfileTopAppBar(
                title = "Settings",
                canNavigateBack = canNavigateBack,
                navigateUp = onNavigateUp
            )
        }
    ) {innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // App theme
            SettingsForm(
                text = "Dark Theme",
                checked = viewModel.getTheme(),
                canChecked = !viewModel.getLightSensor(),
                onCheckedChange = {viewModel.updateAppSettings(isDark = it)}
            )
            // Light sensor
            SettingsForm(
                text = "Light Sensor",
                checked = viewModel.getLightSensor(),
                onCheckedChange = {viewModel.updateAppSettings(lightSensorOn = it)}
            )
            // Notification
            SettingsForm(
                text = "Notification",
                checked = viewModel.getNotification(),
                onCheckedChange = {viewModel.updateAppSettings(notificationOn = it)}
            )
        }
    }
}

@Composable
fun SettingsForm(
    modifier: Modifier = Modifier,
    text: String,
    checked: Boolean,
    canChecked: Boolean = true,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = text)
        Switch(
            checked = checked,
            onCheckedChange = {
                if (canChecked) {
                    onCheckedChange(it)
                }
            },
            thumbContent = if (checked) {
                {
                    Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = null,
                        modifier = Modifier.size(SwitchDefaults.IconSize),
                    )
                }
            } else {
                null
            }
        )
    }
}