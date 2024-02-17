package com.example.mobilecomputing.Screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.rounded.Circle
import androidx.compose.material.icons.rounded.Mic
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mobilecomputing.AppViewModel
import com.example.mobilecomputing.NoteDetails
import com.example.mobilecomputing.audio.AndroidAudioPlayer
import com.example.mobilecomputing.audio.AndroidAudioRecorder
import com.example.mobilecomputing.audio.AudioTimer
import com.example.mobilecomputing.navigation.NoteOption
import com.example.mobilecomputing.util.DeleteConfirmationDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AudioScreen(
    viewModel: AppViewModel,
    onNavigateUp: () -> Unit,
    canNavigateBack: Boolean = true,
    navigateBack: () -> Unit,
    noteOption: NoteOption
) {
    val date: Long = Date().time
    val coroutineScope = rememberCoroutineScope()
    var onDeleteOption by rememberSaveable { mutableStateOf(false) }
    var isRecording by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            var isTitleClicked by remember{ mutableStateOf(false) }
            TextTopAppBar(
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
        Surface(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding))
        {
            AudioBody(
                noteDetails = viewModel.noteUiState.noteDetails,
                onValueChange = viewModel::updateNoteUiState,
                modifier = Modifier.padding(innerPadding),
                viewModel = viewModel
            )
            if (onDeleteOption) {
                DeleteConfirmationDialog(
                    onDeleteConfirm = {
                        onDeleteOption = false
                        coroutineScope.launch {
                            File(viewModel.noteUiState.noteDetails.filePath).delete()
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
}

@Composable
fun AudioBody(
    modifier: Modifier = Modifier,
    noteDetails: NoteDetails,
    onValueChange: (NoteDetails) -> Unit,
    viewModel: AppViewModel
) {
    val context = LocalContext.current
    var isRecording by remember { mutableStateOf(false) }
    var isPlaying by remember { mutableStateOf(false) }

    val simpleDateFormat = SimpleDateFormat("yyyy.MM.DD_hh.mm.ss")
    val date = simpleDateFormat.format(Date())
    val fileName = "audio_record_$date.mp3"

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var audioFile: File?
        val recorder by lazy {
            AndroidAudioRecorder(context)
        }

        val player by lazy {
            AndroidAudioPlayer(context)
        }
        audioFile = File(context.externalCacheDir?.absolutePath, fileName)
        File(context.externalCacheDir?.absolutePath, fileName).also {
            audioFile = it
        }
        /*TimerScreen(
            newStartTime = 0L,
            activateTimer = timerUiState.isActive
        )*/
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = {
                    if (!isRecording) {
                           isPlaying = if(!isPlaying) {
                               if (noteDetails.filePath.isNotBlank()) {
                                   player.playFile(File(noteDetails.filePath))
                                   true
                               } else {
                                   false
                               }
                           } else {
                               player.stop()
                               false
                           }
                    }
                },
                modifier= Modifier.size(80.dp),
                shape = CircleShape,
                border= BorderStroke(5.dp, Color(0XFF0F9D58)),
                contentPadding = PaddingValues(0.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor =  Color.Black)
            ) {
                if (!isPlaying) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Play Audio"
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Pause,
                        contentDescription = "Play Audio"
                    )
                }

            }
            Button(
                onClick = {
                    if (!isPlaying) {
                        if (!isRecording) {
                            audioFile?.delete()
                            /*File(context.externalCacheDir?.absolutePath, fileName).also {
                                recorder.start(it)
                                audioFile = it
                                if (recorder.isRecorderPLaying()) {
                                    isRecording = true
                                }
                            }*/
                            recorder.start(audioFile!!)
                            if (recorder.isRecorderPLaying()) {
                                isRecording = true
                            }
                        } else {
                            recorder.stop()
                            audioFile?.let { noteDetails.copy(filePath = it.absolutePath) }
                                ?.let { onValueChange(it) }
                            isRecording = false
                        }
                    }
                },
                modifier= Modifier.size(80.dp),
                shape = CircleShape,
                border= BorderStroke(5.dp, Color(0XFF0F9D58)),
                contentPadding = PaddingValues(0.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Black)
            ) {
                if (!isRecording) {
                    Icon(
                        imageVector = Icons.Rounded.Mic,
                        contentDescription = "Record Audio",
                        modifier = Modifier.size(50.dp)
                    )
                } else {
                    Icon(
                        imageVector = Icons.Rounded.Circle,
                        contentDescription = "Pause Record Audio",
                        modifier = Modifier.size(50.dp)
                    )
                }
            }
        }
        /*if (!isPlaying) {
            viewModel.updateTimerUiState(isActive = true)
        } else {
            viewModel.updateTimerUiState(isActive = false)
        }*/
    }
}

@Composable
fun TimerScreen(
    activateTimer: Boolean = false,
    newStartTime: Long
) {
    var time by remember { mutableStateOf(0L) }

    var isActive by remember { mutableStateOf(activateTimer) }

    var startTime by remember { mutableStateOf(System.currentTimeMillis()) }

    Text(
        text = formatTime(timeMi = time),
        fontWeight = FontWeight.Bold,
        fontSize = 30.sp,
        color = Color.Black
    )

    LaunchedEffect(activateTimer) {
        while (activateTimer) {
            delay(1000)
            time = System.currentTimeMillis() - startTime
        }
    }

}

@Composable
fun formatTime(
    timeMi: Long
): String {
    val hours = TimeUnit.MILLISECONDS.toHours(timeMi)
    val min = TimeUnit.MILLISECONDS.toMinutes(timeMi) % 60
    val sec = TimeUnit.MILLISECONDS.toSeconds(timeMi) % 60

    return String.format("%02d:%02d:%02d", hours, min, sec)
}



























