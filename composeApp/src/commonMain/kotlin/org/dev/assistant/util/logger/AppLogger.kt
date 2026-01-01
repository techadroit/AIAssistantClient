package org.dev.assistant.util.logger

import co.touchlab.kermit.Logger
import co.touchlab.kermit.Severity

/**
 * Application logger wrapper class that provides a convenient interface
 * for logging throughout the application using Kermit library.
 *
 * Usage:
 * ```
 * val logger = AppLogger("MyClass")
 * logger.debug("Debug message")
 * logger.info("Info message")
 * logger.warn("Warning message")
 * logger.error("Error message", exception)
 * ```
 */
class AppLogger(tag: String) {

    private val logger: Logger = Logger.withTag(tag)

    /**
     * Log a verbose message
     * @param message The message to log
     */
    fun verbose(message: String) {
        logger.v { message }
    }

    /**
     * Log a debug message
     * @param message The message to log
     */
    fun debug(message: String) {
        logger.d { message }
    }

    /**
     * Log an info message
     * @param message The message to log
     */
    fun info(message: String) {
        logger.i { message }
    }

    /**
     * Log a warning message
     * @param message The message to log
     */
    fun warn(message: String) {
        logger.w { message }
    }

    /**
     * Log a warning message with throwable
     * @param message The message to log
     * @param throwable The throwable to log
     */
    fun warn(message: String, throwable: Throwable) {
        logger.w(throwable) { message }
    }

    /**
     * Log an error message
     * @param message The message to log
     */
    fun error(message: String) {
        logger.e { message }
    }

    /**
     * Log an error message with throwable
     * @param message The message to log
     * @param throwable The throwable to log
     */
    fun error(message: String, throwable: Throwable) {
        logger.e(throwable) { message }
    }

    /**
     * Log an error with only throwable
     * @param throwable The throwable to log
     */
    fun error(throwable: Throwable) {
        logger.e(throwable) { throwable.message ?: "An error occurred" }
    }

    /**
     * Log an assert message
     * @param message The message to log
     */
    fun assert(message: String) {
        logger.a { message }
    }

    /**
     * Log an assert message with throwable
     * @param message The message to log
     * @param throwable The throwable to log
     */
    fun assert(message: String, throwable: Throwable) {
        logger.a(throwable) { message }
    }

    /**
     * Log a message with custom severity
     * @param severity The severity level
     * @param message The message to log
     */
    fun log(severity: Severity, message: String) {
        when (severity) {
            Severity.Verbose -> verbose(message)
            Severity.Debug -> debug(message)
            Severity.Info -> info(message)
            Severity.Warn -> warn(message)
            Severity.Error -> error(message)
            Severity.Assert -> assert(message)
        }
    }

    /**
     * Log a message with custom severity and throwable
     * @param severity The severity level
     * @param message The message to log
     * @param throwable The throwable to log
     */
    fun log(severity: Severity, message: String, throwable: Throwable) {
        when (severity) {
            Severity.Verbose -> logger.v(throwable) { message }
            Severity.Debug -> logger.d(throwable) { message }
            Severity.Info -> logger.i(throwable) { message }
            Severity.Warn -> warn(message, throwable)
            Severity.Error -> error(message, throwable)
            Severity.Assert -> assert(message, throwable)
        }
    }

    /**
     * Check if verbose logging is enabled
     */
    fun isVerboseEnabled(): Boolean = logger.config.minSeverity <= Severity.Verbose

    /**
     * Check if debug logging is enabled
     */
    fun isDebugEnabled(): Boolean = logger.config.minSeverity <= Severity.Debug

    /**
     * Check if info logging is enabled
     */
    fun isInfoEnabled(): Boolean = logger.config.minSeverity <= Severity.Info

    /**
     * Check if warn logging is enabled
     */
    fun isWarnEnabled(): Boolean = logger.config.minSeverity <= Severity.Warn

    /**
     * Check if error logging is enabled
     */
    fun isErrorEnabled(): Boolean = logger.config.minSeverity <= Severity.Error

    companion object {
        /**
         * Create a logger instance with the specified tag
         * @param tag The tag for the logger
         * @return AppLogger instance
         */
        fun create(tag: String): AppLogger = AppLogger(tag)

        /**
         * Create a logger instance using the class name as tag
         * @param clazz The class to use for the tag
         * @return AppLogger instance
         */
        fun create(clazz: Any): AppLogger = AppLogger(clazz::class.simpleName ?: "Unknown")
    }
}

