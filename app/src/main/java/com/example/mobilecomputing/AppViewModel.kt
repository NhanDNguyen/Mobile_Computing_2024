package com.example.mobilecomputing

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobilecomputing.data.Profile
import com.example.mobilecomputing.data.ProfileDao
import com.example.mobilecomputing.notification.NotificationService
import com.example.mobilecomputing.notification.PRIORITY_HIGH
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class AppViewModel(
    private var db: ProfileDao,
    private val notificationService: NotificationService
): ViewModel() {

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

    //private var luxValue = 0f
    fun updateLightSensorValue(lux: Float = 0f,) {
        val notification_ID = 100
        val notification_channel_ID = "notification_theme"
        val ic_launcher_forground_id = R.drawable.notification
        val title = "light notification"
        val priority = PRIORITY_HIGH
        // Light Sensor
        //luxValue = lux
        _sensorValuesUiState.update {currentState ->
            currentState.copy(
                x = lux
            )
        }
        if (lux >= 401f) {
            if (!getTheme()) {
                _appSettingsUiState.update {currentState ->
                    if (getNotification()) {
                        showNotification(
                            ID = notification_ID,
                            CHANNEL_ID = notification_channel_ID,
                            ic_launcher_forground_id = ic_launcher_forground_id,
                            title = title,
                            text = "lux = ${lux}, switch to dark theme",
                            priority = priority
                        )
                    }
                    currentState.copy(
                        appSettings = AppSettings(
                            isDark = true,
                            lightSensorOn = getLightSensor(),
                            notificationOn = getNotification()
                        )
                    )
                }
            }
        } else if (lux < 401f) {
            if (getTheme()) {
                _appSettingsUiState.update {currentState ->
                    if (getNotification()) {
                        showNotification(
                            ID = notification_ID,
                            CHANNEL_ID = notification_channel_ID,
                            ic_launcher_forground_id = ic_launcher_forground_id,
                            title = title,
                            text = "lux = ${lux}, switch to light theme",
                            priority = priority
                        )
                    }
                    currentState.copy(
                        appSettings = AppSettings(
                            isDark = false,
                            lightSensorOn = getLightSensor(),
                            notificationOn = getNotification()
                        )
                    )
                }
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