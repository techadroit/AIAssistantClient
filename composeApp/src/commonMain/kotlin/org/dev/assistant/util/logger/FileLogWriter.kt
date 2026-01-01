package org.dev.assistant.util.logger

import co.touchlab.kermit.LogWriter
import co.touchlab.kermit.Severity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.dev.assistant.util.getTimeInMilliseconds
import kotlin.time.Clock

/**
 * A log writer that writes logs to a file
 */
class FileLogWriter(
    private val getLogFilePath: () -> String,
    private val writeToFile: suspend (String, String) -> Unit
) : LogWriter() {

    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    override fun log(severity: Severity, message: String, tag: String, throwable: Throwable?) {
        scope.launch {
            try {
                val timestamp = getTimeInMilliseconds()
                val logMessage = buildString {
                    append("[$timestamp] ")
                    append("[${severity.name}] ")
                    append("[$tag] ")
                    append(message)
                    if (throwable != null) {
                        append("\n")
                        append(throwable.stackTraceToString())
                    }
                    append("\n")
                }
                writeToFile(getLogFilePath(), logMessage)
            } catch (e: Exception) {
                // Failed to write log, print to console instead
                println("Failed to write log: ${e.message}")
            }
        }
    }
}

/**
 * Global file log writer instance
 */
object FileLogger {
    private var fileLogWriter: FileLogWriter? = null

    fun initialize(
        getLogFilePath: () -> String,
        writeToFile: suspend (String, String) -> Unit
    ) {
        fileLogWriter = FileLogWriter(getLogFilePath, writeToFile)
    }

    fun getWriter(): FileLogWriter? = fileLogWriter
}

