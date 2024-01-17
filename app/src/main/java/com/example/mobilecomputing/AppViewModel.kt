package com.example.mobilecomputing

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobilecomputing.data.Profile
import com.example.mobilecomputing.data.ProfileDao
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class AppViewModel(private var db: ProfileDao): ViewModel() {
    var profileUiState by mutableStateOf(ProfileUiState())
        private set

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