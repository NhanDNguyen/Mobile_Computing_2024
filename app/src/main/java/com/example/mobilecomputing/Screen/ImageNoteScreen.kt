package com.example.mobilecomputing.Screen

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Matrix
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.camera.core.ImageCapture.OnImageCapturedCallback
import androidx.camera.core.ImageProxy
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.mobilecomputing.AppViewModel
import com.example.mobilecomputing.NoteDetails
import com.example.mobilecomputing.data.getBitmap
import com.example.mobilecomputing.navigation.NoteOption
import com.example.mobilecomputing.util.DeleteConfirmationDialog
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date

enum class ImageOption {
    Delete, Choose, None
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageScreen(
    viewModel: AppViewModel,
    onNavigateUp: () -> Unit,
    canNavigateBack: Boolean = true,
    navigateBack: () -> Unit,
    noteOption: NoteOption
) {
    val date: Long = Date().time
    val coroutineScope = rememberCoroutineScope()
    var imageOption by rememberSaveable { mutableStateOf(ImageOption.None) }

    Scaffold(
        topBar = {
            var isTitleClicked by remember{ mutableStateOf(false) }
            ImageTopAppBar(
                title = {
                    Box(
                        modifier = Modifier
                            .clickable(onClick = { isTitleClicked = !isTitleClicked })
                            .padding(16.dp)
                    ) {
                        if (isTitleClicked) {
                            TextField(
                                value = viewModel.noteUiState.noteDetails.title,
                                label = { Text(text = "Title")},
                                onValueChange = {
                                    viewModel.updateNoteUiState(
                                        viewModel.noteUiState.noteDetails.copy(title = it)
                                    )
                                },
                                singleLine = true,
                                enabled = true,
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                    unfocusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                    disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                                ),
                                keyboardActions = KeyboardActions(
                                    onDone = {isTitleClicked = !isTitleClicked},
                                    onNext = {isTitleClicked = !isTitleClicked}
                                ),
                            )
                        } else {
                            if (viewModel.noteUiState.noteDetails.title.isNotBlank()) {
                                Text(text = viewModel.noteUiState.noteDetails.title)
                            }
                            else {
                                Text(text = "Title", color = Color.LightGray)
                            }
                        }
                    }
                },
                navigateUp = {
                    viewModel.updateNoteUiState(viewModel.noteUiState.noteDetails.copy(date = date))

                    coroutineScope.launch {
                        if (noteOption == NoteOption.Entry) {
                            viewModel.insertNewNote()
                        } else if (noteOption == NoteOption.Update) {
                            viewModel.updateNote()
                        }
                    }
                    navigateBack()
                },
                onDeleteClick = { imageOption = ImageOption.Delete },
                onImageClick = { imageOption = ImageOption.Choose }
            )
        }
    ) { innerPadding ->
        Surface(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)) // Add this to fix it
        {
            ImageBody(
                noteDetails = viewModel.noteUiState.noteDetails,
                modifier = Modifier
            )
            when (imageOption) {
                ImageOption.Delete -> {
                    DeleteConfirmationDialog(
                        onDeleteConfirm = {
                            imageOption = ImageOption.None
                            coroutineScope.launch {
                                viewModel.deleteNote()
                                navigateBack()
                            }
                        },
                        onDeleteCancel = { imageOption = ImageOption.None },
                        modifier = Modifier.padding(16.dp)
                    )
                }
                ImageOption.Choose -> {
                    CameraAndPhotoSelector(
                        onSelectedImage = {
                            viewModel.updateNoteUiState(viewModel.noteUiState.noteDetails.copy(imageData = it))
                            imageOption = ImageOption.None
                        },
                        onCancel = {imageOption = ImageOption.None}
                    )
                }
                else -> {}
            }
        }
    }
}

@Composable
fun ImageBody(
    modifier: Modifier = Modifier,
    noteDetails: NoteDetails,
) {
    if (noteDetails.imageData != null) {
        ImageLayoutView(noteDetails = noteDetails)
    }
}

@Composable
fun ImageLayoutView(
    modifier: Modifier = Modifier,
    noteDetails: NoteDetails
) {
    noteDetails.imageData?.let {
        Image(
            bitmap = it.asImageBitmap(),
            contentDescription = noteDetails.title,
            modifier = modifier.fillMaxSize(),
            contentScale = ContentScale.Fit
        )
    }
}


@Composable
fun CameraAndPhotoSelector(
    onSelectedImage: (Bitmap?) -> Unit,
    onCancel: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    val file = context.createImageFile()
    val tempUri = FileProvider.getUriForFile(
        context,
        "com.example.mobilecomputing.fileprovider", file
    )

    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            if (uri != null) {
                coroutineScope.launch {
                    onSelectedImage(getBitmap(uri, context))
                }
            }
        }
    )
    fun launchPhotoPicker() {
        singlePhotoPickerLauncher.launch(
            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
        )
    }

    val singlePictureCaptureLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicturePreview()
    ) { capturedImage ->
        if (capturedImage != null) {
            onSelectedImage(capturedImage)
        }
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            if (success) {
                coroutineScope.launch {
                    onSelectedImage(getBitmap(tempUri, context))
                }
            }
        }
    )


    AlertDialog(
        onDismissRequest = {  },
        title = { Text(text = "Attention") },
        text = { Text(text = "Choose one the options") },
        dismissButton = {
            TextButton(onClick = onCancel) {
                Text(text = "Cancel")
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    //singlePictureCaptureLauncher.launch()
                    cameraLauncher.launch(tempUri)
                }
            ) {
                Text(text = "take picture")
            }
            TextButton(onClick = {launchPhotoPicker()}) {
                Text(text = "pick photo")
            }
        }
    )
}

fun Context.createImageFile(): File {
    val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
    val imageFileName = "PNG_" + timestamp + "_"
    return File.createTempFile(
        imageFileName,
        ".PNG",
        externalCacheDir
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageTopAppBar(
    modifier: Modifier = Modifier,
    title: @Composable () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    navigateUp: () -> Unit = {},
    onDeleteClick: () -> Unit,
    onImageClick: () -> Unit,
) {
    SmallTopAppBar(
        title = title,
        modifier = modifier
            .drawBehind {
                val borderSize = 1.dp.toPx()
                drawLine(
                    color = Color.LightGray,
                    start = Offset(0f, size.height),
                    end = Offset(size.width, size.height),
                    strokeWidth = borderSize
                )
            },
        scrollBehavior = scrollBehavior,
        navigationIcon = {
            IconButton(onClick = navigateUp) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back")
            }
        },
        actions = {
            TextButton(
                onClick = onImageClick,
                border = BorderStroke(width = 2.dp, Color.DarkGray)
            ) {
                Text(text = "Choose Photo", color = Color.DarkGray)
            }
            IconButton(onClick = onDeleteClick) {
                Icon(imageVector = Icons.Filled.Delete, contentDescription = "Delete")
            }
        },
    )
}
