package com.example.mobilecomputing.Screen

import android.media.MediaPlayer
import android.media.MediaRecorder
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
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
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
import com.example.mobilecomputing.navigation.NoteOption
import com.example.mobilecomputing.util.DeleteConfirmationDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import java.lang.IllegalStateException
import java.text.SimpleDateFormat
import java.util.Date

@RequiresApi(Build.VERSION_CODES.S)
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

    val context = LocalContext.current
    val recorder = MediaRecorder(context)
    val dirPath = "${context.externalCacheDir?.absolutePath}/"
    //val audioRecorder = AudioRecorderAndPLayer(recorder, dirPath)
    val audioRecorder = AudioRecorderAndPLayer(dirPath)

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
                        viewModel.updateNoteUiState(viewModel.noteUiState.noteDetails.copy(date = date))
                        if (noteOption == NoteOption.Entry) {
                            viewModel.insertNewNote()
                        } else if (noteOption == NoteOption.Update) {
                            viewModel.updateNote()
                        }
                        audioRecorder.releaseRecording()
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
                viewModel = viewModel,
                modifier = Modifier.padding(innerPadding),
                audioRecorder = audioRecorder
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

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun AudioBody(
    modifier: Modifier = Modifier,
    viewModel: AppViewModel,
    audioRecorder: AudioRecorderAndPLayer
) {
    var isPlayingAudio by remember { mutableStateOf(false) }
    var isRecording by remember { mutableStateOf(false) }
    var duration by remember { mutableLongStateOf(0L) }
    var tick by remember { mutableIntStateOf(viewModel.noteUiState.noteDetails.durationMillis.toInt()) }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = if (isRecording || isPlayingAudio) formatTime(tick) else viewModel.noteUiState.noteDetails.ampsPath,
            fontWeight = FontWeight.Bold,
            fontSize = 30.sp,
            color = Color.Black
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = {
                    if (!isRecording) {
                        isPlayingAudio = !isPlayingAudio
                        audioRecorder.onPlaying(isPlayingAudio, viewModel.noteUiState.noteDetails.filePath)
                    }
                },
                modifier= Modifier.size(80.dp),
                shape = CircleShape,
                border= BorderStroke(5.dp, Color(0XFF0F9D58)),
                contentPadding = PaddingValues(0.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor =  Color.Black)
            ) {
                Icon(
                    imageVector = if (isPlayingAudio) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = "Play Audio",
                    modifier = Modifier.size(50.dp)
                )

            }
            Button(
                onClick = {
                    if (!isPlayingAudio) {
                        if (!isRecording) {
                            duration = System.currentTimeMillis()
                            audioRecorder.startRecording()
                            if (audioRecorder.recording())
                                isRecording = true
                        } else {
                            audioRecorder.stopRecording()
                            duration = System.currentTimeMillis() - duration
                            viewModel.updateNoteUiState(
                                viewModel.noteUiState.noteDetails.copy(
                                    filePath = audioRecorder.getFilePath(),
                                    ampsPath = formatTime(duration),
                                    durationMillis = duration / 1000
                                )
                            )

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
                Icon(
                    imageVector = if (isRecording) Icons.Default.Stop else Icons.Default.Mic,
                    contentDescription = "Play Audio",
                    modifier = Modifier.size(50.dp)
                )
            }
        }
    }
    LaunchedEffect(isRecording || isPlayingAudio) {
        if (isRecording)
            tick = 0
        else if (isPlayingAudio) {
            tick = viewModel.noteUiState.noteDetails.durationMillis.toInt()
        }
        while (isRecording || isPlayingAudio) {
            delay(1000)
            if (isRecording)
                tick++
            else if (isPlayingAudio) {
                tick--
                if (tick == 0) {
                    isPlayingAudio = false
                }
            }
        }
    }
}


class AudioRecorderAndPLayer(
    //private var recorder: MediaRecorder? = null,
    private val dirPath: String = ""
) {
    private val simpleDateFormat = SimpleDateFormat("yyyy.MM.DD_hh.mm.ss")
    private val date = simpleDateFormat.format(Date())
    private var fileName = "audio_record_$date"
    private var filePath = "$dirPath$fileName.mp3"

    var errorMessage = ""

    var isRecoding = false

    private var player: MediaPlayer? = null
    private var recorder: MediaRecorder? = null

    fun onRecord(start: Boolean) = if (start) {
        startRecording()
    } else {
        stopRecording()
    }


    fun startRecording() {
        recorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setAudioEncodingBitRate(16 * 44100)
            setAudioSamplingRate(96000)
            setOutputFile(filePath)

            try {
                prepare()
            }catch (_: IOException){}

            start()
        }

        isRecoding = true
    }

    fun stopRecording() {
        recorder?.apply {
            try {
                stop()
                reset()
            } catch (_: IllegalStateException) {

            }
        }
        isRecoding = false

    }

    fun releaseRecording() {
        recorder?.release()
        recorder = null
    }

    fun onPlaying(start: Boolean, filePath: String) = if (start) {
        startPlaying(filePath)
    } else {
        stopPlaying()
    }

    private fun startPlaying(filePath: String) {
        player = MediaPlayer().apply {
            try {
                setDataSource(filePath)
                prepare()
                start()
            } catch (_: IOException) {
                errorMessage = "cannot set file path"
            }
        }
    }


    private fun stopPlaying() {
        //player?.stop()
        //player?.reset()
        player?.release()
        player = null
    }

    fun getFilePath() = filePath
    fun recording() = isRecoding

}


fun formatTime(duration: Long): String {
    val seconds = (duration / 1000) % 60
    val minutes = (duration / (1000 * 60)) % 60
    val hours = (duration / (1000 * 60 * 60))

    return  "%02d:%02d:%02d".format(hours, minutes, seconds)
}

fun formatTime(duration: Int): String {
    val seconds = duration % 60
    val minutes = (duration / 60) % 60
    val hours = (duration / (60 * 60))
    return  "%02d:%02d:%02d".format(hours, minutes, seconds)
}























