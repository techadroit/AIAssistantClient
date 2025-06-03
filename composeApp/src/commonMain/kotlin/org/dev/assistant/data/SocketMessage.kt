package org.dev.assistant.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SocketMessage(
    @SerialName("message_type")
    val messageType: String,
    @SerialName("response_message")
    val responseMessage: String?,
    @SerialName("error_message")
    val errorMessage: String?,
    @SerialName("error_code")
    val errorCode: String?,
    @SerialName("content")
    val content: Map<String, String> = emptyMap(),
    @SerialName("content_type")
    val contentType: String
)