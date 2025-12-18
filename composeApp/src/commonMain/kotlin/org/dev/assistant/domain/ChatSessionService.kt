package org.dev.assistant.domain

import org.dev.assistant.data.ChatSessionRepository
import org.dev.assistant.data.model.ChatSessionCreateRequest
import org.dev.assistant.data.model.ChatSessionResponse
import org.dev.assistant.data.model.ChatSessionUpdateRequest

class ChatSessionService(
    private val userService: UserService,
    private val chatSessionRepository: ChatSessionRepository
) {

    suspend fun createChatSession(title: String?): Result<String> {
        return runCatching {
            val userId = userService.getUserId()
                .getOrElse { throw IllegalStateException("User not authenticated", it) }

            chatSessionRepository.createChatSession(
                ChatSessionCreateRequest(userId, title)
            ).getOrThrow().chatSessionId
        }
    }

    suspend fun getChatSession(chatSessionId: String): Result<ChatSessionResponse> {
        return chatSessionRepository.getChatSession(chatSessionId)
    }

    suspend fun updateChatSession(
        chatSessionId: String,
        title: String? = null,
        isArchived: Boolean? = null
    ): Result<Unit> {
        return chatSessionRepository.updateChatSession(
            chatSessionId,
            ChatSessionUpdateRequest(title, isArchived)
        ).map { }
    }

    suspend fun deleteChatSession(chatSessionId: String): Result<Unit> {
        return chatSessionRepository.deleteChatSession(chatSessionId).map { }
    }

    suspend fun archiveChatSession(chatSessionId: String): Result<Unit> {
        return chatSessionRepository.archiveChatSession(chatSessionId).map { }
    }

    suspend fun getUserChatSessions(includeArchived: Boolean = false): Result<List<ChatSessionResponse>> {
        return runCatching {
            val userId = userService.getUserId()
                .getOrElse { throw IllegalStateException("User not authenticated", it) }

            chatSessionRepository.getUserChatSessions(userId, includeArchived)
                .getOrThrow()
                .chatSessions
        }
    }
}
