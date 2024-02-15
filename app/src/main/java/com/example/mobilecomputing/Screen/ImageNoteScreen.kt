package com.example.mobilecomputing.Screen

import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import com.example.mobilecomputing.data.getBitmap
import kotlinx.coroutines.launch

@Composable
fun CameraAndPhotoSelector(
    onSelectedImage: (Bitmap?) -> Unit,
    onCancel: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

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

    AlertDialog(
        onDismissRequest = {  },
        title = { Text(text = "Attention") },
        text = { Text(text = "Choose one the options") },
        dismissButton = {
            TextButton(onClick = onCancel) {
                Text(text = "No")
            }
            TextButton(onClick = {onSelectedImage(null)}) {
                Text(text = "Delete current image")
            }
        },
        confirmButton = {
            TextButton(onClick = {singlePictureCaptureLauncher.launch()}) {
                Text(text = "take picture")
            }
            TextButton(onClick = {launchPhotoPicker()}) {
                Text(text = "pick photo")
            }
        }
    )
}
