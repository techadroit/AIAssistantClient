package org.dev.assistant.domain

import org.dev.assistant.data.UserRepository

class UserService(val userRepository: UserRepository) {

    fun getUserId(): Result<String> {
        // Placeholder implementation
        return Result.success("9053841d-8ef3-4d8b-a204-49e77ff6c1ec")
    }
}