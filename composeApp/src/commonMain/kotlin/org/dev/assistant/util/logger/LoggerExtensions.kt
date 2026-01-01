package org.dev.assistant.util.logger

import kotlinx.datetime.Clock

/**
 * Extension functions for AppLogger to provide additional logging capabilities
 */

/**
 * Log the entry into a method/function
 * @param methodName The name of the method being entered
 * @param params Optional parameters to log
 */
fun AppLogger.entering(methodName: String, vararg params: Any?) {
    if (isDebugEnabled()) {
        val paramStr = if (params.isNotEmpty()) {
            params.joinToString(", ") { it.toString() }
        } else ""
        debug("→ Entering $methodName${if (paramStr.isNotEmpty()) " with params: $paramStr" else ""}")
    }
}

/**
 * Log the exit from a method/function
 * @param methodName The name of the method being exited
 * @param result Optional result to log
 */
fun AppLogger.exiting(methodName: String, result: Any? = null) {
    if (isDebugEnabled()) {
        debug("← Exiting $methodName${if (result != null) " with result: $result" else ""}")
    }
}

/**
 * Log a method/function execution time
 * @param methodName The name of the method
 * @param durationMs The duration in milliseconds
 */
fun AppLogger.timing(methodName: String, durationMs: Long) {
    if (isDebugEnabled()) {
        debug("⏱ $methodName took ${durationMs}ms")
    }
}

/**
 * Execute a block and log its execution time
 * @param methodName The name of the operation
 * @param block The block to execute
 * @return The result of the block execution
 */
inline fun <T> AppLogger.measureTime(methodName: String, block: () -> T): T {
    val startTime = Clock.System.now().toEpochMilliseconds()
    return try {
        block()
    } finally {
        val duration = Clock.System.now().toEpochMilliseconds() - startTime
        timing(methodName, duration)
    }
}

/**
 * Log a network request
 * @param url The request URL
 * @param method The HTTP method
 */
fun AppLogger.logRequest(url: String, method: String = "GET") {
    if (isDebugEnabled()) {
        debug("🌐 $method request to: $url")
    }
}

/**
 * Log a network response
 * @param url The request URL
 * @param statusCode The response status code
 * @param durationMs Optional request duration
 */
fun AppLogger.logResponse(url: String, statusCode: Int, durationMs: Long? = null) {
    if (isDebugEnabled()) {
        debug("✅ Response from: $url | Status: $statusCode${if (durationMs != null) " | Duration: ${durationMs}ms" else ""}")
    }
}

/**
 * Log a network error
 * @param url The request URL
 * @param error The error that occurred
 */
fun AppLogger.logNetworkError(url: String, error: Throwable) {
    error("❌ Network error for: $url", error)
}

/**
 * Log a success message with icon
 * @param message The success message
 */
fun AppLogger.success(message: String) {
    info("✅ $message")
}

/**
 * Log a failure message with icon
 * @param message The failure message
 * @param throwable Optional throwable
 */
fun AppLogger.failure(message: String, throwable: Throwable? = null) {
    if (throwable != null) {
        error("❌ $message", throwable)
    } else {
        error("❌ $message")
    }
}

