package org.dev.assistant.domain

import org.dev.assistant.data.UserLocalRepository
import org.dev.assistant.data.UserRepository
import org.dev.assistant.util.logger.AppLogger
import kotlin.math.log

class UserService(
    val userRepository: UserRepository,
    val userLocalRepository: UserLocalRepository
) {
    private val logger = AppLogger("UserService")
    suspend fun initUserId(): Result<String> {
        // check user id exist
        userLocalRepository.getUserId().getOrNull()?.let {
            logger.debug("Found existing user id: $it")
            return Result.success(it)
        }
        logger.debug("No user id found, logging in anonymously")
        return userRepository.loginAsAnonymous().getOrNull()?.userId?.let {
            logger.debug("Saving user id locally: $it")
            userLocalRepository.saveUserId(it)
            logger.debug("saved user id locally: $it")
            Result.success(it)
        } ?: run {
            logger.debug("Failed to login anonymously")
            Result.failure(

                Exception("Failed to login anonymously")
            )
        }

    }

    suspend fun getUserId(): Result<String> {
        // Placeholder implementation
        return userLocalRepository.getUserId().getOrNull()?.let {
            logger.debug("Found existing user id: $it")
            return Result.success(it)
        } ?: run {
            logger.debug("No user id found, logging in anonymously")
            Result.failure(
                Exception("Failed to login anonymously")
            )
        }
    }

    suspend fun loginAnonymously(): Result<String?> {
        return userLocalRepository.getUserId()
    }

    suspend fun clearUserData() {
        userLocalRepository.clearUserId()
    }
}