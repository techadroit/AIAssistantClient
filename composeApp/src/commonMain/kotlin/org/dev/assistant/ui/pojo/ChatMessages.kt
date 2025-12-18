package org.dev.assistant.ui.pojo

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable



@Serializable
data class ChatMessageBody(
    val messages: String
)

@Serializable
enum class ChatModeType(val value: String) {
    @SerialName("document")
    DOCUMENT("document"),
    @SerialName("websearch")
    WEBSEARCH("websearch"),
    @SerialName("agent")
    AGENT("agent"),
    @SerialName("none")
    NONE("none")
}

@Serializable
data class ChatMode(
    val mode: ChatModeType = ChatModeType.NONE
)

fun ChatMode.on() = copy(mode = ChatModeType.AGENT)
fun ChatMode.off() = copy(mode = ChatModeType.NONE)

@Serializable
data class ChatMessages(
    @SerialName("message_id") val messageId: String,
    @SerialName("utc_time") val utcTime: String,
    @SerialName("chat_session_id") val chatSessionId: String,
    val sender: String,
    val receiver: String,
    val message: ChatMessageBody,
    val chat_mode: ChatMode = ChatMode()
)