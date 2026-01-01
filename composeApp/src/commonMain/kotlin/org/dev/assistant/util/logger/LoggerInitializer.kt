package org.dev.assistant.util.logger

import co.touchlab.kermit.Logger
import co.touchlab.kermit.Severity
import co.touchlab.kermit.platformLogWriter

/**
 * Initializer for application-wide logging configuration
 */
object LoggerInitializer {

    private var isInitialized = false

    /**
     * Initialize logging for the application
     * @param enableFileLogging Whether to enable file logging
     * @param minSeverity Minimum severity level for logs
     */
    fun initialize(
        enableFileLogging: Boolean = true,
        minSeverity: Severity = Severity.Debug
    ) {
        if (isInitialized) {
            println("Logger already initialized")
            return
        }

        val logWriters = mutableListOf(platformLogWriter())

        if (enableFileLogging) {
            try {
                // Initialize file logger
                FileLogger.initialize(
                    getLogFilePath = { LogFileUtil.getDefaultLogFilePath() },
                    writeToFile = { filePath, content ->
                        LogFileUtil.appendToFile(filePath, content)
                    }
                )

                FileLogger.getWriter()?.let { writer ->
                    logWriters.add(writer)
                    println("File logging enabled at: ${LogFileUtil.getDefaultLogFilePath()}")
                }
            } catch (e: Exception) {
                println("Failed to initialize file logging: ${e.message}")
            }
        }

        // Set log writers and minimum severity
        Logger.setLogWriters(logWriters)
        Logger.setMinSeverity(minSeverity)

        isInitialized = true

        // Log initialization
        val logger = AppLogger("LoggerInitializer")
        logger.info("Logger initialized successfully")
        logger.info("Minimum severity: ${minSeverity.name}")
        logger.info("File logging: ${if (enableFileLogging) "enabled" else "disabled"}")
        if (enableFileLogging) {
            logger.info("Log file path: ${LogFileUtil.getDefaultLogFilePath()}")
        }
    }

    /**
     * Clear all log files
     */
    suspend fun clearLogs() {
        try {
            LogFileUtil.clearLogFile(LogFileUtil.getDefaultLogFilePath())
            val logger = AppLogger("LoggerInitializer")
            logger.info("Log files cleared successfully")
        } catch (e: Exception) {
            println("Failed to clear logs: ${e.message}")
        }
    }
}

