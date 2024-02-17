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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Draw
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.mobilecomputing.AppViewModel
import com.example.mobilecomputing.NoteDetails
import com.example.mobilecomputing.NoteTopAppBar
import com.example.mobilecomputing.data.Note
import com.example.mobilecomputing.navigation.AppScreen
import com.example.mobilecomputing.toNoteDetails
import com.example.mobilecomputing.util.getDateAsString

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navigateToNoteEntry: (AppScreen) -> Unit,
    navigateToNoteUpdate: (Note) -> Unit,
    viewModel: AppViewModel
) {
    val homeUiStates by viewModel.noteUiStates.collectAsState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    var expandOption by remember{ mutableStateOf(false) }

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
            Column {
                if (expandOption) {
                    FloatingActionButton(
                        onClick = { navigateToNoteEntry(AppScreen.Text) },
                        shape = MaterialTheme.shapes.medium,
                        modifier = Modifier
                            .padding(vertical = 5.dp)
                            .padding(horizontal = 20.dp)
                    ) {
                        Icon(imageVector = Icons.Filled.Edit, contentDescription = "Text Screen")
                    }
                    FloatingActionButton(
                        onClick = { navigateToNoteEntry(AppScreen.Image) },
                        shape = MaterialTheme.shapes.medium,
                        modifier = Modifier
                            .padding(vertical = 5.dp)
                            .padding(horizontal = 20.dp)
                    ) {
                        Icon(imageVector = Icons.Filled.Image, contentDescription = "Image Screen")
                    }
                    FloatingActionButton(
                        onClick = { navigateToNoteEntry(AppScreen.Audio) },
                        shape = MaterialTheme.shapes.medium,
                        modifier = Modifier
                            .padding(vertical = 5.dp)
                            .padding(horizontal = 20.dp)
                    ) {
                        Icon(imageVector = Icons.Filled.Mic, contentDescription = "Audio Screen")
                    }
                    FloatingActionButton(
                        onClick = { /*TODO*/ },
                        shape = MaterialTheme.shapes.medium,
                        modifier = Modifier
                            .padding(vertical = 5.dp)
                            .padding(horizontal = 20.dp)
                    ) {
                        Icon(imageVector = Icons.Filled.Draw, contentDescription = "Drawing Screen")
                    }
                }
                FloatingActionButton(
                    onClick = {expandOption = !expandOption},
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier.padding(20.dp)
                ) {
                    if (expandOption) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Create new note"
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Create new note"
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        HomeBody(
            noteList = homeUiStates.noteList,
            onNoteClick = navigateToNoteUpdate,
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize(),
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
    val widthInDp = configuration.screenWidthDp.dp / 2.25f
    val heightInDp = configuration.screenHeightDp.dp / 3

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
                        noteDetails = note.toNoteDetails(),
                        onNoteClick = {onNoteClick(note)},
                        widthInDp = widthInDp,
                        heightInDp = heightInDp
                    )
                }
                "image" -> {
                    ImageNoteCard(
                        noteDetails = note.toNoteDetails(),
                        onNoteClick = {onNoteClick(note)},
                        widthInDp = widthInDp,
                        heightInDp = heightInDp
                    )
                }
                "audio" -> {
                    AudioNoteCard(
                        noteDetails = note.toNoteDetails(),
                        onNoteClick = {onNoteClick(note)},
                        widthInDp = widthInDp,
                        heightInDp = heightInDp)
                }
                "drawing" -> {
                    /*TODO*/
                }
                else -> {}
            }
        }
    }
}




























