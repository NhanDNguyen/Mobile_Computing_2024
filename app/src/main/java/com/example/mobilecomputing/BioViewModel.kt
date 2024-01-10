package com.example.mobilecomputing

import androidx.lifecycle.ViewModel
import com.example.mobilecomputing.data.BioUiState
import com.example.mobilecomputing.data.Message
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class BioViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(BioUiState())
    val uiState: StateFlow<BioUiState> = _uiState.asStateFlow()

    fun setData(message: Message) {
        _uiState.update {currentState ->
            currentState.copy(
                message = Message(
                    author = message.author,
                    body = message.body,
                    imageId = message.imageId,
                    contentDescription = message.contentDescription
                )
            )
        }
    }
}