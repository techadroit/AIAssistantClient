package org.dev.assistant.ui.pojo

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable



@Serializable
data class ChatMessageBody(
    val messages: String
)

@Serializable
data class AgentMode(
    val mode: Int = 0
)

fun AgentMode.on() = copy(mode = 1)
fun AgentMode.off() = copy(mode = 0)

@Serializable
data class ChatMessages(
    @SerialName("message_id") val messageId: String,
    @SerialName("utc_time") val utcTime: String,
    val sender: String,
    val receiver: String,
    val message: ChatMessageBody,
    val mode: AgentMode = AgentMode()
)