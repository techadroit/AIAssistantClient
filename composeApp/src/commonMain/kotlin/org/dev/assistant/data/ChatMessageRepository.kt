package org.dev.assistant.data

import org.dev.assistant.data.model.PaginatedChatResponse
import org.dev.assistant.network.NetworkClient

class ChatMessageRepository(val networkClient: NetworkClient) {

    suspend fun getAllMessages(sessionId: String): Result<PaginatedChatResponse> {
        return networkClient.get<PaginatedChatResponse>("/api/messages/${sessionId}")
    }
}