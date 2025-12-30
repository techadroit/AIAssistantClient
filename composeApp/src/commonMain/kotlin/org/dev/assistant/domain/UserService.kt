package org.dev.assistant.domain

import org.dev.assistant.data.UserLocalRepository
import org.dev.assistant.data.UserRepository

class UserService(
    val userRepository: UserRepository,
    val userLocalRepository: UserLocalRepository
) {

    suspend fun initUserId(): Result<String> {
        // check user id exist
        userLocalRepository.getUserId().getOrNull()?.let {
            println("Found existing user id: $it")
            return Result.success(it)
        }
        println("No user id found, logging in anonymously")
        return userRepository.loginAsAnonymous().getOrNull()?.userId?.let {
            userLocalRepository.saveUserId(it)
            Result.success(it)
        } ?: Result.failure(
            Exception("Failed to login anonymously")
        )

    }

    suspend fun getUserId(): Result<String> {
        // Placeholder implementation
        return userLocalRepository.getUserId().getOrNull()?.let {
            println("Found existing user id: $it")
            return Result.success(it)
        } ?: Result.failure(
            Exception("Failed to login anonymously")
        )
    }

    suspend fun loginAnonymously(): Result<String?> {
        return userLocalRepository.getUserId()
    }

    suspend fun clearUserData() {
        userLocalRepository.clearUserId()
    }
}