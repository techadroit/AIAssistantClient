package org.dev.assistant.data

import org.dev.assistant.domain.UserService

class SessionManager(val userService: UserService) {

    suspend fun getSessionId(): String? {
        // Placeholder implementation
        return userService.getUserId().getOrNull()
    }
}