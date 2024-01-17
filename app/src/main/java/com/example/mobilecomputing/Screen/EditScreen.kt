package com.example.mobilecomputing.Screen

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.example.mobilecomputing.AppViewModel
import com.example.mobilecomputing.ProfileTopAppBar
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditScreen(
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    viewModel: AppViewModel
) {
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            ProfileTopAppBar(
                title = "Edit Character",
                canNavigateBack = true,
                navigateUp = onNavigateUp
            )
        },
        modifier = modifier
    ) {innerPadding ->
        EntryBody(
            profileUiState = viewModel.profileUiState,
            onCharacterValueChange = viewModel::updateProfileUiState,
            onDoneClick = {
                coroutineScope.launch {
                    viewModel.updateProfile()
                    navigateBack()
                }
            },
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .fillMaxWidth()
        )
    }
}