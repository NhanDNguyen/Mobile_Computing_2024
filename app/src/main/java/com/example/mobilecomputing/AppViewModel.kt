package com.example.mobilecomputing

import android.content.Context
import android.graphics.Bitmap
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobilecomputing.data.Note
import com.example.mobilecomputing.data.NoteDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.ObjectOutputStream
import java.text.SimpleDateFormat
import java.util.Date

class AppViewModel(
    private val db: NoteDao,
): ViewModel() {
    var noteUiState by mutableStateOf(NoteUiState())
        private set

    private val _timerUiState = MutableStateFlow(TimerUiState())
    val timerUiState: StateFlow<TimerUiState> = _timerUiState.asStateFlow()

    fun updateTimerUiState(isActive: Boolean = _timerUiState.value.isActive) {
        _timerUiState.update {
            it.copy(
                isActive = isActive
            )
        }
    }

    fun getTimerState() = _timerUiState.value.isActive

    var noteUiStates: StateFlow<NoteUiStates> =
        db.getAllNotes().map { NoteUiStates(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000L),
                initialValue = NoteUiStates()
            )
    fun resetNoteUiState(type: String) {
        noteUiState = NoteUiState(
            noteDetails = NoteDetails(type = type)
        )
    }

    fun updateNoteUiState(noteDetails: NoteDetails) {
        noteUiState = NoteUiState(
            noteDetails = noteDetails,
            isNoteValid = validateNoteInput(noteDetails)
        )
    }

    suspend fun insertNewNote() {
        if (validateNoteInput()) {
            if (noteUiState.noteDetails.title == "") {
                updateNoteUiState(noteUiState.noteDetails.copy(title = "Title"))
            }
            db.insertNote(noteUiState.noteDetails.toNote())
        }
    }

    suspend fun updateNote() {
        if (validateNoteInput()) {
            db.updateNote(noteUiState.noteDetails.toNote())
        }
    }

    suspend fun deleteNote() {
        db.deleteNote(noteUiState.noteDetails.toNote())
    }

    private fun validateNoteInput(noteDetails: NoteDetails = noteUiState.noteDetails): Boolean {
        return when (noteDetails.type) {
            "text" -> noteDetails.body.isNotBlank() || noteDetails.title.isNotBlank()
            "image" -> noteDetails.imageData != null
            "audio" -> noteDetails.filePath.isNotBlank()
            else -> false
        }
    }

}

data class NoteUiStates(
    val noteList: List<Note> = listOf()
)

data class NoteUiState(
    val noteDetails: NoteDetails = NoteDetails(),
    val isNoteValid: Boolean = false
)

data class NoteDetails(
    val id: Long = 0,
    val title: String = "",
    val date: Long = Date().time,
    val type: String = "",

    // Text
    val body: String = "",

    // Image
    val imageData: Bitmap? = null,

    // Audio
    val filePath: String = "",
    val durationMillis: Long = 0L,
    val ampsPath: String = "",
)

fun NoteDetails.toNote() = Note(
    id = id,
    title = title,
    date = date,
    type = type,

    body = body,

    imageData = imageData,

    filePath = filePath,
    durationMillis = durationMillis,
    ampsPath = ampsPath
)

fun Note.toNoteDetails() = NoteDetails(
    id = id,
    title = title,
    date = date,
    type = type,

    body = body,

    imageData = imageData,

    filePath = filePath,
    durationMillis = durationMillis,
    ampsPath = ampsPath
)

data class TimerUiState(
    val isActive: Boolean = false
)