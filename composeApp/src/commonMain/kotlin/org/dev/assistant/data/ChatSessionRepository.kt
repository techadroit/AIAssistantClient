package org.dev.assistant.data

import org.dev.assistant.data.model.ChatSessionCreateRequest
import org.dev.assistant.data.model.ChatSessionCreateResponse
import org.dev.assistant.data.model.ChatSessionListResponse
import org.dev.assistant.data.model.ChatSessionResponse
import org.dev.assistant.data.model.ChatSessionUpdateRequest
import org.dev.assistant.data.model.OperationResponse
import org.dev.assistant.network.NetworkClient

class ChatSessionRepository(private val networkClient: NetworkClient) {

    /**
     * Create a new chat session
     */
    suspend fun createChatSession(request: ChatSessionCreateRequest): Result<ChatSessionCreateResponse> {
        return networkClient.post(
            path = "/api/chat/sessions",
            body = request
        )
    }

    /**
     * Get a specific chat session by ID
     */
    suspend fun getChatSession(chatSessionId: String): Result<ChatSessionResponse> {
        return networkClient.get(
            path = "/api/chat/sessions/$chatSessionId"
        )
    }

    /**
     * Update chat session
     */
    suspend fun updateChatSession(
        chatSessionId: String,
        request: ChatSessionUpdateRequest
    ): Result<OperationResponse> {
        return networkClient.put(
            path = "/api/chat/sessions/$chatSessionId",
            body = request
        )
    }

    /**
     * Permanently delete a chat session and all its messages
     */
    suspend fun deleteChatSession(chatSessionId: String): Result<OperationResponse> {
        return networkClient.delete(
            path = "/api/chat/sessions/$chatSessionId"
        )
    }

    /**
     * Get all chat sessions for a user
     */
    suspend fun getUserChatSessions(
        userId: String,
        includeArchived: Boolean = false
    ): Result<ChatSessionListResponse> {
        val response: Result<ChatSessionListResponse> = networkClient.get(
            path = "/api/chat/users/$userId/sessions",
            queryParams = mapOf("include_archived" to includeArchived.toString())
        )
        return response
    }

    /**
     * Archive a chat session
     */
    suspend fun archiveChatSession(chatSessionId: String): Result<OperationResponse> {
        return networkClient.put(
            path = "/api/chat/sessions/$chatSessionId/archive"
        )
    }
}