package org.dev.assistant.util.logger

import android.content.Context
import java.io.File

private var appContext: Context? = null

/**
 * Initialize the log file utilities with Android context
 */
fun initializeLogFileUtil(context: Context) {
    appContext = context.applicationContext
}

actual fun getLogDirectory(): String {
    val context = appContext ?: throw IllegalStateException("LogFileUtil not initialized. Call initializeLogFileUtil(context) first.")
    val logDir = File(context.filesDir, "logs")

    // Create directory if it doesn't exist
    if (!logDir.exists()) {
        logDir.mkdirs()
    }

    return logDir.absolutePath
}

