package org.dev.assistant.ui.chat

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ChatSessionHandler {

    private val sessionList: StateFlow<List<ChatSessionItem>> =
        MutableStateFlow(emptyList<ChatSessionItem>())

    fun getChatSessions(): StateFlow<List<ChatSessionItem>> {
        return sessionList
    }

    fun addChatSession(chatSession: List<ChatSessionItem> = emptyList()) {
        val currentList = sessionList.value.toMutableList()
        currentList.addAll(chatSession)
        (sessionList as MutableStateFlow).value = currentList
    }

    fun removeChatSession(chatSessionId: String) {
        val currentList = sessionList.value.toMutableList()
        currentList.removeAll { it.id == chatSessionId }
        (sessionList as MutableStateFlow).value = currentList
    }
}

data class ChatSessionState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val chatSessions: List<ChatSessionItem> = emptyList()
)

data class ChatSessionItem(
    val id: String,
    val title: String,
    val isArchived: Boolean = false
)