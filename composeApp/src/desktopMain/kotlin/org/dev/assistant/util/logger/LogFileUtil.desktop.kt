package org.dev.assistant.util.logger

import java.io.File

actual fun getLogDirectory(): String {
    // Use user's home directory for desktop
    val homeDir = System.getProperty("user.home")
    val logDir = File(homeDir, ".ai_client/logs")

    // Create directory if it doesn't exist
    if (!logDir.exists()) {
        logDir.mkdirs()
    }

    return logDir.absolutePath
}

