package org.dev.assistant.util.logger

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlinx.io.writeString

/**
 * Get the log directory based on the platform
 */
expect fun getLogDirectory(): String

/**
 * Utility for writing logs to files
 */
object LogFileUtil {

    /**
     * Append text to a log file
     * @param filePath The path to the log file
     * @param content The content to append
     */
    suspend fun appendToFile(filePath: String, content: String) {
        withContext(Dispatchers.Default) {
            try {
                val path = Path(filePath)
                val sink = SystemFileSystem.sink(path, append = true).buffered()
                sink.writeString(content)
                sink.flush()
                sink.close()
            } catch (e: Exception) {
                println("Error writing to log file: ${e.message}")
                e.printStackTrace()
            }
        }
    }

    /**
     * Get the default log file path for the application
     */
    fun getDefaultLogFilePath(): String {
        // This will be platform-specific
        return getLogDirectory() + "/app_logs.txt"
    }

    /**
     * Clear the log file
     */
    suspend fun clearLogFile(filePath: String) {
        withContext(Dispatchers.Default) {
            try {
                val path = Path(filePath)
                if (SystemFileSystem.exists(path)) {
                    SystemFileSystem.delete(path)
                }
            } catch (e: Exception) {
                println("Error clearing log file: ${e.message}")
            }
        }
    }
}

