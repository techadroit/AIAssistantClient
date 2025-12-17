package org.dev.assistant.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// Request Models
@Serializable
data class ChatSessionCreateRequest(
    @SerialName("user_id")
    val userId: String,
    @SerialName("title")
    val title: String? = null
)

@Serializable
data class ChatSessionUpdateRequest(
    @SerialName("title")
    val title: String? = null,
    @SerialName("is_archived")
    val isArchived: Boolean? = null
)

// Response Models
@Serializable
data class ChatSessionCreateResponse(
    @SerialName("chat_session_id")
    val chatSessionId: String,
    @SerialName("message")
    val message: String = "Chat session created successfully"
)

@Serializable
data class ChatSessionResponse(
    @SerialName("chat_session_id")
    val chatSessionId: String,
    @SerialName("user_id")
    val userId: String,
    @SerialName("title")
    val title: String? = null,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("updated_at")
    val updatedAt: String,
    @SerialName("is_archived")
    val isArchived: Boolean
)

@Serializable
data class ChatSessionListResponse(
    @SerialName("chat_sessions")
    val chatSessions: List<ChatSessionResponse>,
    @SerialName("count")
    val count: Int
)

@Serializable
data class OperationResponse(
    @SerialName("success")
    val success: Boolean,
    @SerialName("message")
    val message: String,
    @SerialName("data")
    val data: Map<String, String>? = null
)

