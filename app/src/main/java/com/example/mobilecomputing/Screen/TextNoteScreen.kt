package com.example.mobilecomputing.Screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Text
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
import androidx.compose.ui.unit.dp
import com.example.mobilecomputing.AppViewModel
import com.example.mobilecomputing.NoteDetails
import com.example.mobilecomputing.navigation.NoteOption
import com.example.mobilecomputing.util.DeleteConfirmationDialog
import kotlinx.coroutines.launch
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextTopAppBar(
    modifier: Modifier = Modifier,
    title: @Composable () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    navigateUp: () -> Unit = {},
    onDeleteClick: () -> Unit,
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
            IconButton(onClick = onDeleteClick) {
                Icon(imageVector = Icons.Filled.Delete, contentDescription = "Delete")
            }
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntryScreen(
    viewModel: AppViewModel,
    onNavigateUp: () -> Unit,
    canNavigateBack: Boolean = true,
    navigateBack: () -> Unit,
    noteOption: NoteOption
) {
    val date: Long = Date().time
    val coroutineScope = rememberCoroutineScope()
    var onDeleteOption by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        topBar = {
            var isTitleClicked by remember{mutableStateOf(false)}
            TextTopAppBar(
                title = {
                    Box(
                        modifier = Modifier
                            .clickable(onClick = {isTitleClicked = !isTitleClicked})
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
                    coroutineScope.launch {
                        if (noteOption == NoteOption.Entry) {
                            viewModel.updateNoteUiState(viewModel.noteUiState.noteDetails.copy(date = date))
                            viewModel.insertNewNote()
                        } else if (noteOption == NoteOption.Update) {
                            viewModel.updateNoteUiState(viewModel.noteUiState.noteDetails.copy(date = date))
                            viewModel.updateNote()
                        }
                        navigateBack()
                    }
                 },
                onDeleteClick = {onDeleteOption = true},
            )
        }
    ) { innerPadding ->
        EntryBody(
            noteDetails = viewModel.noteUiState.noteDetails,
            onValueChange = viewModel::updateNoteUiState,
            modifier = Modifier.padding(innerPadding)
        )
        if (onDeleteOption) {
            DeleteConfirmationDialog(
                onDeleteConfirm = {
                    onDeleteOption = false
                    coroutineScope.launch {
                        viewModel.deleteNote()
                        navigateBack()
                    }
                },
                onDeleteCancel = { onDeleteOption = false },
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Composable
fun EntryBody(
    modifier: Modifier = Modifier,
    noteDetails: NoteDetails,
    onValueChange: (NoteDetails) -> Unit
) {
    InputForm(
        noteDetails = noteDetails,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxSize()
    )
}

@Composable
fun InputForm   (
    modifier: Modifier = Modifier,
    noteDetails: NoteDetails,
    onValueChange: (NoteDetails) -> Unit,
    enabled: Boolean = true
) {
    Box(
        modifier = modifier.background(color = Color.LightGray)
    ) {
        TextField(
            value = noteDetails.body,
            onValueChange = {onValueChange(noteDetails.copy(body = it))},
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 10.dp),
            singleLine = false,
            enabled = enabled,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.background,
                unfocusedContainerColor = MaterialTheme.colorScheme.background,
                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            shape = RoundedCornerShape(0.dp)
        )
    }
}


