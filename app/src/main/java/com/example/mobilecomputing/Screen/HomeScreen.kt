package com.example.mobilecomputing.Screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.mobilecomputing.AppViewModel
import com.example.mobilecomputing.NoteTopAppBar
import com.example.mobilecomputing.data.Note

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navigateToNoteEntry: () -> Unit,
    navigateToNoteUpdate: (Note) -> Unit,
    viewModel: AppViewModel
) {
    val homeUiState by viewModel.noteUiStates.collectAsState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            NoteTopAppBar(
                title = "All notes\n${homeUiState.noteList.size} note(s)",
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
            noteList = homeUiState.noteList,
            onNoteClick = navigateToNoteUpdate,
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize()
        )
    }
}

@Composable
private fun HomeBody(
    modifier: Modifier = Modifier,
    noteList: List<Note>,
    onNoteClick: (Note) -> Unit,
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
            NoteCard(
                note = note,
                onNoteClick = {onNoteClick(note)},
                widthInDp = widthInDp/2.25f,
                heightInDp = heightInDp/3)
        }
    }
}

@Composable
private fun NoteCard(
    modifier: Modifier = Modifier,
    note: Note,
    onNoteClick: (Note) -> Unit,
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
                .clickable { onNoteClick(note) }
                .padding(6.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        ) {
            if (note.imageData != null) {
                Image(
                    bitmap = note.imageData.asImageBitmap(),
                    contentDescription = "image"
                )
            } else {
                Text(
                    text = note.body,
                    modifier = Modifier.padding(8.dp),
                )
            }
        }
        //Spacer(modifier = Modifier.height(8.dp))
        Text(text = note.title, fontWeight = FontWeight.Bold)
        //Spacer(modifier = Modifier.height(8.dp))
        Text(text = note.date, color = Color.Gray)
    }

}


























