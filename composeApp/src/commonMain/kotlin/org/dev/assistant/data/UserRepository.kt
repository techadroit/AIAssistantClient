package org.dev.assistant.data

import io.ktor.client.request.request
import org.dev.assistant.data.model.*
import org.dev.assistant.network.NetworkClient

class UserRepository(private val networkClient: NetworkClient) {

    /**
     * Create a new user
     */
    suspend fun createUser(request: UserCreateRequest): Result<UserCreateResponse> {
        return networkClient.post(
            path = "/api/users/create",
            body = request
        )
    }

    /**
     * Create a new user
     */
    suspend fun loginAsAnonymous(): Result<AnonymousUserResponse> {
        return networkClient.post(
            path = "/api/users/anonymous",
        )
    }

    /**
     * Get a user by their ID
     */
    suspend fun getUser(userId: String): Result<UserResponse> {
        return networkClient.get(
            path = "/api/users/$userId"
        )
    }

    /**
     * Update a user's information
     */
    suspend fun updateUser(userId: String, request: UserUpdateRequest): Result<UserOperationResponse> {
        return networkClient.put(
            path = "/api/users/$userId",
            body = request
        )
    }

    /**
     * Permanently delete a user and all associated data
     */
    suspend fun deleteUser(userId: String): Result<UserOperationResponse> {
        return networkClient.delete(
            path = "/api/users/$userId"
        )
    }

    /**
     * Get all users
     */
    suspend fun getAllUsers(includeInactive: Boolean = false): Result<UserListResponse> {
        return networkClient.get(
            path = "/api/users",
            queryParams = mapOf("include_inactive" to includeInactive.toString())
        )
    }

    /**
     * Deactivate a user (soft delete)
     */
    suspend fun deactivateUser(userId: String): Result<UserOperationResponse> {
        return networkClient.put(
            path = "/api/users/$userId/deactivate"
        )
    }

    /**
     * Authenticate a user
     */
    suspend fun login(request: UserLoginRequest): Result<UserLoginResponse> {
        return networkClient.post(
            path = "/api/users/auth/login",
            body = request
        )
    }

    /**
     * Legacy login endpoint (kept for backwards compatibility)
     */
    suspend fun legacyLogin(request: UserLoginRequest): Result<UserLoginResponse> {
        return networkClient.post(
            path = "/api/login",
            body = request
        )
    }

    /**
     * Legacy get user ID endpoint (kept for backwards compatibility)
     */
    suspend fun getUserId(): Result<String> {
        return networkClient.get(
            path = "/api/login/get_user_id"
        )
    }
}