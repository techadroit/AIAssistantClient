package org.dev.assistant.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.dev.assistant.ui.pojo.ChatMessageBody
import org.dev.assistant.ui.pojo.ChatMessages
import org.dev.assistant.ui.pojo.ChatModeType

@Serializable
data class MessageResponse(
    @SerialName("message_id") val messageId: String,
    @SerialName("chat_session_id") val chatSessionId: String,
    @SerialName("utc_time") val utcTime: String,
    val sender: String,
    val receiver: String,
    val messages: String,
    val mode: String,
    // Python datetime usually serializes to an ISO-8601 string in JSON
    @SerialName("created_at") val createdAt: String? = null
)

fun MessageResponse.toChatMessages() = ChatMessages(
    messageId = this.messageId,
    chatSessionId = this.chatSessionId,
    utcTime = this.utcTime,
    sender = this.sender,
    receiver = this.receiver,
    message = ChatMessageBody(this.messages),
    chat_mode = ChatModeType.valueOf(this.mode.uppercase())
)

@Serializable
data class PaginatedChatResponse(
    val messages: List<MessageResponse>,
    val page: Int,
    @SerialName("page_size") val pageSize: Int,
    @SerialName("total_count") val totalCount: Int,
    @SerialName("total_pages") val totalPages: Int,
    @SerialName("has_next") val hasNext: Boolean,
    @SerialName("has_previous") val hasPrevious: Boolean
)
