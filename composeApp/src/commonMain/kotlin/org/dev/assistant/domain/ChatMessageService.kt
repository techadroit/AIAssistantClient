package org.dev.assistant.domain

import org.dev.assistant.data.ChatMessageRepository

class ChatMessageService(val repository: ChatMessageRepository) {

    suspend fun getAllMessages(sessionId: String) = repository.getAllMessages(sessionId)
}