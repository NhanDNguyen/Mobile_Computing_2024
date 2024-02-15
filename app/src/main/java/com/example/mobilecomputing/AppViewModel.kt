package com.example.mobilecomputing

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobilecomputing.data.AudioNote
import com.example.mobilecomputing.data.AudioNoteDao
import com.example.mobilecomputing.data.ImageNote
import com.example.mobilecomputing.data.ImageNoteDao
import com.example.mobilecomputing.data.Note
import com.example.mobilecomputing.data.NoteDao
import com.example.mobilecomputing.data.TextNote
import com.example.mobilecomputing.data.TextNoteDao
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.util.Date

class AppViewModel(
    private val noteDB: NoteDao,
    private val textNoteDB: TextNoteDao,
    private val imageNoteDB: ImageNoteDao,
    private val audioNoteDB: AudioNoteDao
): ViewModel() {
    var currentOptionState by mutableStateOf("text")
        private set
    var textNoteUiState by mutableStateOf(TextNoteUiState())
        private set

    var currentTextNoteId by mutableStateOf(0L)
    var imageNoteUiState by mutableStateOf(ImageNoteUiState())
        private set

    var audioNoteUiState by mutableStateOf(AudioNoteUiState())
        private set

    var noteUiStates: StateFlow<NoteUiStates> =
        noteDB.getAllNotes().map { NoteUiStates(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000L),
                initialValue = NoteUiStates()
            )

    var currentTextNote: StateFlow<TextNote> =
        textNoteDB.getTextNote(currentTextNoteId)
            .filterNotNull()
            .map { it }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Eagerly,
                initialValue = TextNote()
            )
    /****************************************** TextNote ******************************************/
    fun resetTextNoteUiState() {
        textNoteUiState = TextNoteUiState()
    }

    fun updateTextNoteUiState(textNoteUiState: TextNoteUiState) {
        this.textNoteUiState = textNoteUiState
    }

    fun updateTextNoteUiState(note: Note) {
        currentTextNoteId = note.id
        textNoteUiState = TextNoteUiState(
            id = note.id,
            title = note.title,
            date = note.date,
            type = note.type,
            body = currentTextNote.value.body
        )
    }

    fun getTextNoteUiState(note: Note): TextNoteUiState {
        currentTextNoteId = note.id
        return TextNoteUiState(
            id = note.id,
            title = note.title,
            date = note.date,
            type = note.type,
            body = String()
        )
    }

    suspend fun insertNewTextNote() {
        if (validateTextNoteInput()) {
            if (textNoteUiState.title == "") {
                updateTextNoteUiState(textNoteUiState.copy(title = "Title"))
            }
            val id = noteDB.insertNote(textNoteUiState.toNote())
            textNoteDB.insertTextNote(TextNote(id = id, body = textNoteUiState.body))
        }
    }

    suspend fun updateTextNote() {
        if (validateTextNoteInput()) {
            noteDB.updateNote(textNoteUiState.toNote())
            textNoteDB.updateTextNote(textNoteUiState.toTextNote())
        }
    }

    fun getTextNote() = textNoteDB.getTextNote(textNoteUiState.id)
    /*private fun getTextNote(id: Long): TextNote {
        val note = noteDB.getNote(id).value?: Note()
        return textNoteDB.getTextNote(id).value ?: TextNote(body = note.id.toString())
    }*/

    suspend fun deleteTextNote() {
        noteDB.deleteNote(textNoteUiState.toNote())
        textNoteDB.deleteTextNote(textNoteUiState.toTextNote())
    }

    private fun validateTextNoteInput(): Boolean = textNoteUiState.body.isNotBlank()
    /****************************************** TextNote ******************************************/

    /***************************************** ImageNote *****************************************/
    fun resetImageNoteUiState() {
        imageNoteUiState = ImageNoteUiState()
    }

    fun updateImageNoteUiState(note: Note, imageNote: ImageNote) {
        imageNoteUiState = ImageNoteUiState(
            note = note,
            imageNote = imageNote,
            isValidateInput = validateImageNoteInput()
        )
    }

    suspend fun insertNewImageNote() {
        if (validateImageNoteInput()) {
            val id = noteDB.insertNote(imageNoteUiState.note)
            imageNoteDB.insertImageNote(imageNoteUiState.imageNote.copy(id=id))
        }
    }

    suspend fun updateImageNote() {
        if (validateImageNoteInput()) {
            noteDB.updateNote(imageNoteUiState.note)
            imageNoteDB.updateImageNote(imageNoteUiState.imageNote)
        }
    }

    suspend fun deleteImageNote() {
        noteDB.deleteNote(imageNoteUiState.note)
        imageNoteDB.deleteImageNote(imageNoteUiState.imageNote)
    }

    private fun validateImageNoteInput(): Boolean = imageNoteUiState.imageNote.imageData != null
    /***************************************** ImageNote *****************************************/

    /******************************************AudioNote***********************************/
    fun resetAudioNoteUiState() {
        audioNoteUiState = AudioNoteUiState()
    }

    fun updateAudioNoteUiState(note: Note, audioNote: AudioNote) {
        audioNoteUiState = AudioNoteUiState(
            note = note,
            audioNote = audioNote,
            isValidateInput = validateAudioNoteInput()
        )
    }

    suspend fun insertNewAudioNote() {
        if (validateAudioNoteInput()) {
            val id = noteDB.insertNote(audioNoteUiState.note)
            audioNoteDB.insertAudioNote(audioNoteUiState.audioNote.copy(id=id))
        }
    }

    suspend fun updateAudioNote() {
        if (validateImageNoteInput()) {
            noteDB.updateNote(audioNoteUiState.note)
            audioNoteDB.updateAudioNote(audioNoteUiState.audioNote)
        }
    }

    suspend fun deleteAudioNote() {
        noteDB.deleteNote(audioNoteUiState.note)
        audioNoteDB.deleteAudioNote(audioNoteUiState.audioNote)
    }

    private fun validateAudioNoteInput(): Boolean = audioNoteUiState.audioNote.filePath != ""
    /******************************************AudioNote***********************************/
}

data class NoteUiStates(
    val noteList: List<Note> = listOf()
)

data class TextNoteUiState(
    val id: Long = 0,
    val title: String = "",
    val date: Long = Date().time,
    val type: String = "text",
    val body: String = "",
    val isValidateInput: Boolean = false
)

fun TextNoteUiState.toNote() = Note(
    id = id,
    title = title,
    date = date,
    type = type
)

fun TextNoteUiState.toTextNote() = TextNote(
    id = id,
    body = body
)

data class ImageNoteUiState(
    val note: Note = Note(),
    val imageNote: ImageNote = ImageNote(),
    val isValidateInput: Boolean = false
)

data class AudioNoteUiState(
    val note: Note = Note(),
    val audioNote: AudioNote = AudioNote(),
    val isValidateInput: Boolean = false
)