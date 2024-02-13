package com.example.mobilecomputing

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobilecomputing.data.Note
import com.example.mobilecomputing.data.NoteDao
import com.example.mobilecomputing.notification.NotificationService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class AppViewModel(
    private var db: NoteDao,
    private val notificationService: NotificationService
): ViewModel() {
    var noteUiState by mutableStateOf(noteUiState())
        private set

    private val _appSettingsUiState = MutableStateFlow(AppSettingsUiState())
    val appSettingsUiState: StateFlow<AppSettingsUiState> = _appSettingsUiState.asStateFlow()

    var noteUiStates: StateFlow<noteUiStates> =
        db.getAllNotes().map { noteUiStates(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000L),
                initialValue = noteUiStates()
            )


    fun resetnoteUiState() {
        noteUiState = noteUiState()
    }

    fun updatenoteUiState(note: Note) {
        noteUiState = noteUiState(
            note = note,
            isValidateInput = validateInput(note)
        )
    }

    private fun showNotification(
        ID: Int,
        CHANNEL_ID: String,
        ic_launcher_forground_id: Int,
        title: String,
        text: String,
        priority: Int,
        bigText: String = ""
    ) {
        notificationService.showNotification(
            ID = ID,
            CHANNEL_ID = CHANNEL_ID,
            ic_launcher_forground_id = ic_launcher_forground_id,
            title = title,
            text = text,
            priority = priority,
            bigText = bigText
        )
    }

    fun updateAppSettings(
        isDark: Boolean = _appSettingsUiState.value.appSettings.isDark,
        lightSensorOn: Boolean = _appSettingsUiState.value.appSettings.lightSensorOn,
        notificationOn: Boolean = _appSettingsUiState.value.appSettings.notificationOn
    ) {
        _appSettingsUiState.update {currentState ->
            currentState.copy(
                appSettings = AppSettings(
                    isDark = isDark,
                    lightSensorOn = lightSensorOn,
                    notificationOn = notificationOn
                )
            )
        }
    }

    fun getTheme() = _appSettingsUiState.value.appSettings.isDark
    fun getNotification() = _appSettingsUiState.value.appSettings.notificationOn

    suspend fun insertNewNote() {
        if (validateInput()) {
            db.insertNote(noteUiState.note)
        }
    }

    suspend fun updatenote() {
        if (validateInput()) {
            db.updateNote(noteUiState.note)
        }
    }

    suspend fun deletenote() {
        db.deleteNote(noteUiState.note)
    }

    private fun validateInput(note: Note = noteUiState.note): Boolean {
        return with(note) {
            title.isNotBlank() && body.isNotBlank()
        }
    }

}

data class noteUiState(
    val note: Note = Note(),
    val isValidateInput: Boolean = false
)

data class noteUiStates(
    val noteList: List<Note> = listOf()
)

data class AppSettings(
    val isDark: Boolean = false,
    val lightSensorOn: Boolean = true,
    val notificationOn: Boolean = true
)

data class AppSettingsUiState(
    val appSettings: AppSettings = AppSettings()
)