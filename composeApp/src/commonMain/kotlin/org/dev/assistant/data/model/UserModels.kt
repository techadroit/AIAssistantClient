package org.dev.assistant.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// Request Models
@Serializable
data class AnonymousUserRequest(
    @SerialName("device_id")
    val username: String = ""
)

@Serializable
data class AnonymousUserResponse(
    @SerialName("user_id")
    val userId: String,
    @SerialName("is_anonymous")
    val isAnonymous: Boolean,
    @SerialName("message")
    val message: String,
)


// Request Models
@Serializable
data class UserCreateRequest(
    @SerialName("username")
    val username: String,
    @SerialName("email")
    val email: String,
    @SerialName("password")
    val password: String,
    @SerialName("full_name")
    val fullName: String? = null
)

@Serializable
data class UserUpdateRequest(
    @SerialName("username")
    val username: String? = null,
    @SerialName("email")
    val email: String? = null,
    @SerialName("password")
    val password: String? = null,
    @SerialName("full_name")
    val fullName: String? = null,
    @SerialName("is_active")
    val isActive: Boolean? = null
)

@Serializable
data class UserLoginRequest(
    @SerialName("username")
    val username: String,
    @SerialName("password")
    val password: String
)

// Response Models
@Serializable
data class UserCreateResponse(
    @SerialName("user_id")
    val userId: String,
    @SerialName("username")
    val username: String,
    @SerialName("email")
    val email: String,
    @SerialName("message")
    val message: String = "User created successfully"
)

@Serializable
data class UserLoginResponse(
    @SerialName("user_id")
    val userId: String,
    @SerialName("username")
    val username: String,
    @SerialName("email")
    val email: String,
    @SerialName("token")
    val token: String? = null,
    @SerialName("message")
    val message: String = "Login successful"
)

@Serializable
data class UserResponse(
    @SerialName("user_id")
    val userId: String,
    @SerialName("username")
    val username: String,
    @SerialName("email")
    val email: String,
    @SerialName("full_name")
    val fullName: String? = null,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("updated_at")
    val updatedAt: String,
    @SerialName("is_active")
    val isActive: Boolean,
    @SerialName("last_login")
    val lastLogin: String? = null
)

@Serializable
data class UserListResponse(
    @SerialName("users")
    val users: List<UserResponse>,
    @SerialName("count")
    val count: Int
)

@Serializable
data class UserOperationResponse(
    @SerialName("success")
    val success: Boolean,
    @SerialName("message")
    val message: String,
    @SerialName("user_id")
    val userId: String? = null,
    @SerialName("data")
    val data: Map<String, String>? = null
)

