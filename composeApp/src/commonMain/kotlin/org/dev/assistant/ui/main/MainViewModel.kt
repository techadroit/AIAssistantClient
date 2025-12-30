package org.dev.assistant.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.dev.assistant.data.model.ChatSessionResponse
import org.dev.assistant.domain.ChatSessionService
import org.dev.assistant.domain.UserService

class MainViewModel(
    private val chatSessionService: ChatSessionService,
    private val userService: UserService
) : ViewModel() {

    private val _chatSessions = MutableStateFlow<List<ChatSessionResponse>>(emptyList())
    val chatSessions: StateFlow<List<ChatSessionResponse>> = _chatSessions.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        initialize()
    }

    fun initialize() {
        viewModelScope.launch {
            loadUserId()
            loadChatSessions()
        }
    }

    suspend fun loadUserId() {
        userService.initUserId()
    }

    suspend fun loadChatSessions() {

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

    suspend fun loadUser() {
        println("loading")
        userService.loginAnonymously().onSuccess {
            println("logged in anonymously, user id: $it")
        }.onFailure {
            println("failed to login anonymously: ${it.message}")
        }

    }

    fun refreshSessions() {
//        loadChatSessions()
    }

    fun deleteSession(sessionId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            chatSessionService.deleteChatSession(sessionId)
                .onSuccess {
                    // Remove the session from the list
                    _chatSessions.value = _chatSessions.value.filter { it.chatSessionId != sessionId }
                }
                .onFailure { throwable ->
                    _error.value = throwable.message ?: "Failed to delete chat session"
                }

            _isLoading.value = false
        }
    }
}

