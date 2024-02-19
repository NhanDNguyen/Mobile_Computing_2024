package com.example.mobilecomputing

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.mobilecomputing.navigation.AppNavHost
import com.example.mobilecomputing.ui.theme.MobileComputingTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteTopAppBar(
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

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun NoteApp(
    modifier: Modifier = Modifier,
    viewModel: AppViewModel
) {
    MobileComputingTheme() {
        AppNavHost(
            viewModel = viewModel,
            modifier = modifier
        )
    }
}
