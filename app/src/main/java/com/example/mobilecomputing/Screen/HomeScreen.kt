package com.example.mobilecomputing.Screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.mobilecomputing.AppViewModel
import com.example.mobilecomputing.NoteTopAppBar
import com.example.mobilecomputing.TextNoteUiState
import com.example.mobilecomputing.data.Note
import com.example.mobilecomputing.util.getDateAsString

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navigateToNoteEntry: () -> Unit,
    navigateToNoteUpdate: (Note) -> Unit,
    viewModel: AppViewModel
) {
    val homeUiStates by viewModel.noteUiStates.collectAsState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            NoteTopAppBar(
                title = "All notes\n${homeUiStates.noteList.size} note(s)",
                canNavigateBack = false,
                scrollBehavior = scrollBehavior
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = navigateToNoteEntry,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(20.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Create,
                    contentDescription = "Create new note"
                )
            }
        }
    ) { innerPadding ->
        HomeBody(
            noteList = homeUiStates.noteList,
            onNoteClick = navigateToNoteUpdate,
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize(),
            viewModel = viewModel
        )
    }
}

@Composable
private fun HomeBody(
    modifier: Modifier = Modifier,
    noteList: List<Note>,
    onNoteClick: (Note) -> Unit,
    viewModel: AppViewModel,
) {
    val configuration = LocalConfiguration.current
    val widthInDp = configuration.screenWidthDp.dp
    val heightInDp = configuration.screenHeightDp.dp

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
            .padding(horizontal = 16.dp)
            .padding(top = 8.dp)
    ) {
        items(noteList) { note ->
            when(note.type) {
                "text" -> {
                    TextNoteCard(
                        textNoteUiState = viewModel.getTextNoteUiState(note),
                        onNoteClick = {onNoteClick(note)},
                        widthInDp = widthInDp/2.25f,
                        heightInDp = heightInDp/3
                    )
                }
                else -> {}
            }
        }
    }
}

@Composable
private fun TextNoteCard(
    modifier: Modifier = Modifier,
    textNoteUiState: TextNoteUiState,
    onNoteClick: (TextNoteUiState) -> Unit,
    widthInDp: Dp,
    heightInDp: Dp
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ElevatedCard(
            elevation = CardDefaults.cardElevation(6.dp),
            modifier = Modifier
                .size(width = widthInDp, height = heightInDp)
                .clickable { onNoteClick(textNoteUiState) }
                .padding(6.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        ) {
            Text(
                text = textNoteUiState.body,
                modifier = Modifier.padding(8.dp),
            )
        }
        Text(text = textNoteUiState.title, fontWeight = FontWeight.Bold)
        Text(text = getDateAsString(textNoteUiState.date), color = Color.Gray)
    }

}


























