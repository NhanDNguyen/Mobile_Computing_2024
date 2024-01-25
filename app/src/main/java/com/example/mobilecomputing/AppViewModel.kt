package com.example.mobilecomputing

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobilecomputing.data.Profile
import com.example.mobilecomputing.data.ProfileDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class AppViewModel(private var db: ProfileDao): ViewModel() {
    var profileUiState by mutableStateOf(ProfileUiState())
        private set

    private val _appSettingsUiState = MutableStateFlow(AppSettingsUiState())
    val appSettingsUiState: StateFlow<AppSettingsUiState> = _appSettingsUiState.asStateFlow()

    private val _sensorValuesUiState = MutableStateFlow(SensorValuesUIState())
    val sensorValuesUIState: StateFlow<SensorValuesUIState> = _sensorValuesUiState.asStateFlow()

    var profileUiStates: StateFlow<ProfileUiStates> =
        db.getAllProfiles().map { ProfileUiStates(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000L),
                initialValue = ProfileUiStates()
            )

    fun resetProfileUiState() {
        profileUiState = ProfileUiState()
    }

    fun updateProfileUiState(profile: Profile) {
        profileUiState = ProfileUiState(
            profile = profile,
            isValidateInput = validateInput(profile)
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

    //private var luxValue = 0f
    fun updateLightSensorValue(lux: Float = 0f,) {
        // Light Sensor
        //luxValue = lux
        _sensorValuesUiState.update {currentState ->
            currentState.copy(
                x = lux
            )
        }
        if (lux >= 500f) {
            _appSettingsUiState.update {currentState ->
                currentState.copy(
                    appSettings = AppSettings(
                        isDark = false,
                    )
                )
            }
        } else if (lux < 500f) {
            _appSettingsUiState.update {currentState ->
                currentState.copy(
                    appSettings = AppSettings(
                        isDark = true,
                    )
                )
            }
        }
    }

    fun getTheme() = _appSettingsUiState.value.appSettings.isDark

    fun getLightSensor() = _appSettingsUiState.value.appSettings.lightSensorOn
    fun getNotification() = _appSettingsUiState.value.appSettings.notificationOn

    suspend fun saveProfile() {
        if (validateInput()) {
            db.insert(profileUiState.profile)
        }
    }

    suspend fun updateProfile() {
        if (validateInput()) {
            db.update(profileUiState.profile)
        }
    }

    suspend fun deleteProfile() {
        db.delete(profileUiState.profile)
    }

    private fun validateInput(profile: Profile = profileUiState.profile): Boolean {
        return with(profile) {
            name.isNotBlank() && info.isNotBlank()
        }
    }

}

data class ProfileUiState(
    val profile: Profile = Profile(
        name = "",
        info = ""
    ),
    val isValidateInput: Boolean = false
)

data class ProfileUiStates(
    val profileList: List<Profile> = listOf()
)

data class AppSettings(
    val isDark: Boolean = false,
    val lightSensorOn: Boolean = true,
    val notificationOn: Boolean = true
)

data class AppSettingsUiState(
    val appSettings: AppSettings = AppSettings()
)

data class SensorValuesUIState(
    val x: Float = 0f,
    val y: Float = 0f,
    val z: Float = 0f
)