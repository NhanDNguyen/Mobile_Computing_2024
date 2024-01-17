package com.example.mobilecomputing.Screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.mobilecomputing.AppViewModel
import com.example.mobilecomputing.ProfileTopAppBar
import com.example.mobilecomputing.ProfileUiState
import com.example.mobilecomputing.data.Profile
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(
    modifier: Modifier = Modifier,
    navigateToEditProfile: (Profile) -> Unit,
    navigateBack: () -> Unit,
    viewModel: AppViewModel,
) {
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            ProfileTopAppBar(
                title = "Character Details",
                canNavigateBack = true,
                navigateUp = navigateBack
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navigateToEditProfile(viewModel.profileUiState.profile) },
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(20.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Character Details"
                )
            }
        },
        modifier = modifier
    ) {innerPadding ->
        DetailsBody(
            profileUiState = viewModel.profileUiState,
            onDelete = {
                coroutineScope.launch {
                    viewModel.deleteProfile()
                    navigateBack()
                }
            },
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        )
    }
}

@Composable
private fun DetailsBody(
    modifier: Modifier = Modifier,
    profileUiState: ProfileUiState,
    onDelete: () -> Unit,
) {
    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        var deleteConfirmationRequired by rememberSaveable { mutableStateOf(false) }
        Details(
            profile = profileUiState.profile,
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = { deleteConfirmationRequired = true },
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(text = "Delete")
        }
        if (deleteConfirmationRequired) {
            DeleteConfirmationDialog(
                onDeleteConfirm = {
                    deleteConfirmationRequired = false
                    onDelete()
                },
                onDeleteCancel = { deleteConfirmationRequired = false },
                modifier = Modifier.padding(16.dp)
            )
        }

    }

}

@Composable
fun Details(
    modifier: Modifier = Modifier,
    profile: Profile
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        profile.imageData?.let {
            Image(
                bitmap = it.asImageBitmap(),
                contentDescription = profile.imageDescription,
                modifier = Modifier.size(160.dp)
            )
        }
        Text(
            text = profile.name,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp)
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = profile.info,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Composable
private fun DeleteConfirmationDialog(
    modifier: Modifier = Modifier,
    onDeleteConfirm: () -> Unit,
    onDeleteCancel: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = {  },
        title = { Text(text = "Attention") },
        text = { Text(text = "Are you sure you want to delete?") },
        modifier = modifier,
        dismissButton = {
            TextButton(onClick = onDeleteCancel) {
                Text(text = "No")
            }
        },
        confirmButton = {
            TextButton(onClick = onDeleteConfirm) {
                Text(text = "Yes")
            }
        }
    )
}