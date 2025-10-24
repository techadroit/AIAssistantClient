package org.dev.assistant.util

/**
 * Represents file data that can be uploaded
 */
data class FileData(
    val name: String,
    val path: String,
    val size: Long,
    val mimeType: String,
    val bytes: ByteArray? = null
)

