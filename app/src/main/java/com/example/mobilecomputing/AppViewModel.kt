package com.example.mobilecomputing

import android.graphics.Bitmap
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobilecomputing.data.Note
import com.example.mobilecomputing.data.NoteDao
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.util.Date

class AppViewModel(
    private val db: NoteDao,
): ViewModel() {
    var currentOptionState by mutableStateOf("text")
        private set

    var noteUiState by mutableStateOf(NoteUiState())
        private set

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
    val timestamp: Long = 0,
    val duration: String = "",
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
    timestamp = timestamp,
    duration = duration,
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
    timestamp = timestamp,
    duration = duration,
    ampsPath = ampsPath
)