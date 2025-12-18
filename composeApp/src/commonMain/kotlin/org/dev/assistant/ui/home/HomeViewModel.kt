package org.dev.assistant.ui.home

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.dev.assistant.base.BaseViewModel
import org.dev.assistant.domain.ChatSessionService

class HomeViewModel(
    val sessionService: ChatSessionService
) : BaseViewModel() {

    val stateFlow = MutableStateFlow<HomeState>(HomeState())

    fun getAllSessions() {
        scope.launch {
            sessionService.getUserChatSessions(includeArchived = true).fold(
                onSuccess = { sessions ->
                    val sessionItems = sessions.map {
                        ChatSessionItem(
                            id = it.chatSessionId,
                            title = it.title ?: "Untitled",
                            isArchived = it.isArchived
                        )
                    }
                    stateFlow.value = stateFlow.value.copy(
                        isLoading = false,
                        sessionList = sessionItems
                    )
                },
                onFailure = {
                    // Handle error appropriately
                    stateFlow.value = stateFlow.value.copy(
                        isLoading = false,
                        sessionList = emptyList()
                    )
                }
            )
        }
    }
}

data class HomeState(
    val isLoading: Boolean = false,
    val sessionList: List<ChatSessionItem> = emptyList()
)

data class ChatSessionItem(
    val id: String,
    val title: String,
    val isArchived: Boolean
)