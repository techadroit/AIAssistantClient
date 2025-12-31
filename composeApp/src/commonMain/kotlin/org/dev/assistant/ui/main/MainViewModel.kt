package org.dev.assistant.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.dev.assistant.data.WebSocketClient
import org.dev.assistant.data.model.ChatSessionResponse
import org.dev.assistant.domain.ChatSessionService
import org.dev.assistant.domain.UserService
import org.dev.assistant.ui.chat.ChatSessionHandler
import org.dev.assistant.ui.chat.ChatSessionItem

class MainViewModel(
    private val chatSessionService: ChatSessionService,
    private val userService: UserService,
    private val webSocketClient: WebSocketClient,
    private val chatSessionHandler: ChatSessionHandler
) : ViewModel() {

    private val _chatSessions = MutableStateFlow<List<ChatSessionItem>>(emptyList())
    val chatSessions: StateFlow<List<ChatSessionItem>> = chatSessionHandler.getChatSessions()

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
        userService.initUserId().onSuccess {
            webSocketClient.connect()
        }
    }

    suspend fun loadChatSessions() {

        _isLoading.value = true
        _error.value = null

        chatSessionService.getUserChatSessions(includeArchived = false)


        _isLoading.value = false

    }

    fun deleteSession(sessionId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            chatSessionService.deleteChatSession(sessionId)
                .onSuccess {
                    // Remove the session from the list
                    _chatSessions.value = _chatSessions.value.filter { it.id != sessionId }
                }
                .onFailure { throwable ->
                    _error.value = throwable.message ?: "Failed to delete chat session"
                }

            _isLoading.value = false
        }
    }
}

