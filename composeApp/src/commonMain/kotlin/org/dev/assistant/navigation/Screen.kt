package org.dev.assistant.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Screen {
    @Serializable
    data object Home : Screen()

    @Serializable
    data class Chat(val sessionId: String? = null) : Screen()

    @Serializable
    data object Settings : Screen()
}

