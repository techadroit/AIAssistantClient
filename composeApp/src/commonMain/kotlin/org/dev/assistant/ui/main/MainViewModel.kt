package org.dev.assistant.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.dev.assistant.data.model.ChatSessionResponse
import org.dev.assistant.domain.ChatSessionService

class MainViewModel(
    private val chatSessionService: ChatSessionService
) : ViewModel() {

    private val _chatSessions = MutableStateFlow<List<ChatSessionResponse>>(emptyList())
    val chatSessions: StateFlow<List<ChatSessionResponse>> = _chatSessions.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        loadChatSessions()
    }

    fun loadChatSessions() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            chatSessionService.getUserChatSessions(includeArchived = false)
                .onSuccess { sessions ->
                    _chatSessions.value = sessions.sortedByDescending { it.updatedAt }
                }
                .onFailure { throwable ->
                    _error.value = throwable.message ?: "Failed to load chat sessions"
                }

            _isLoading.value = false
        }
    }

    fun refreshSessions() {
        loadChatSessions()
    }
}

