package com.example.mobilecomputing.Screen

import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.mobilecomputing.AppViewModel
import com.example.mobilecomputing.ProfileTopAppBar
import com.example.mobilecomputing.ProfileUiState
import com.example.mobilecomputing.data.Profile
import com.example.mobilecomputing.data.getBitmap
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntryScreen(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    canNavigateBack: Boolean = true,
    viewModel: AppViewModel
) {
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            ProfileTopAppBar(
                title = "Add Profile",
                canNavigateBack = canNavigateBack,
                navigateUp = onNavigateUp
            )
        }
    ) {innerPadding ->
        EntryBody(
            profileUiState = viewModel.profileUiState,
            onCharacterValueChange = viewModel::updateProfileUiState,
            onDoneClick = {
                coroutineScope.launch {
                    viewModel.saveProfile()
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

@Composable
fun EntryBody(
    modifier: Modifier = Modifier,
    profileUiState: ProfileUiState,
    onCharacterValueChange: (Profile) -> Unit,
    onDoneClick: () -> Unit
) {
    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        InputForm(
            profile = profileUiState.profile,
            onValueChange = onCharacterValueChange,
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = onDoneClick,
            enabled = profileUiState.isValidateInput,
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Done")
        }
    }
}

@Composable
fun InputForm(
    modifier: Modifier = Modifier,
    profile: Profile,
    onValueChange: (Profile) -> Unit = {},
    enabled: Boolean = true
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PhotoSelector(
            onSelectedImage = {
                onValueChange(profile.copy(
                    imageData = it
                ))
            }
        )

        if (profile.imageData != null) {
            ImageLayoutView(profile = profile)
        }

        OutlinedTextField(
            value = profile.name,
            onValueChange = { onValueChange(profile.copy(name=it)) },
            label = { Text(text = "Character Name*") },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        OutlinedTextField(
            value = profile.info,
            onValueChange = { onValueChange(profile.copy(info =it)) },
            label = { Text(text = "Character Detail*") },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )

        if (enabled) {
            Text(
                text = "*required fields",
                modifier = Modifier.padding(start = 16.dp)
            )
        }
    }
}

@Composable
fun PhotoSelector(
    onSelectedImage: (Bitmap) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    var selectedImage by remember {
        mutableStateOf<Uri?>(null)
    }

    val buttonText = "Select a photo"

    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            selectedImage = uri
            coroutineScope.launch {
                onSelectedImage(getBitmap(selectedImage, context))
            }
        }
    )

    fun launchPhotoPicker() {
        singlePhotoPickerLauncher.launch(
            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
        )
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = {
            launchPhotoPicker()
        }) {
            Text(buttonText)
        }
    }

}

@Composable
fun ImageLayoutView(
    modifier: Modifier = Modifier,
    profile: Profile
) {
    profile.imageData?.let {
        Image(
            bitmap = it.asImageBitmap(),
            contentDescription = profile.imageDescription,
            modifier = modifier.size(160.dp)
        )
    }
}