package org.dev.assistant.ui.pojo

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable



@Serializable
data class ChatMessageBody(
    val messages: String
)

@Serializable
enum class ChatModeType(val value: String) {
    @SerialName("agent")
    AGENT("agent"),
    @SerialName("offline")
    OFFLINE("offline")
}

@Serializable
data class ChatMessages(
    @SerialName("message_id") val messageId: String,
    @SerialName("utc_time") val utcTime: String,
    @SerialName("chat_session_id") val chatSessionId: String,
    val sender: String,
    val receiver: String,
    val message: ChatMessageBody,
    @SerialName("chat_mode") val chat_mode: ChatModeType = ChatModeType.OFFLINE
)