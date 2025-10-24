package org.dev.assistant.util

import kotlinx.io.files.*
import kotlinx.io.Buffer
import kotlinx.io.readByteArray

/**
 * Unified file system utilities using kotlinx-io
 * This provides cross-platform file operations
 */
object FileSystemUtil {

    /**
     * Get the default file system for the current platform
     */
    fun getFileSystem(): FileSystem = SystemFileSystem

    /**
     * Read file bytes from a path
     */
    fun readFileBytes(path: Path): ByteArray {
        val fileSystem = getFileSystem()
        val buffer = Buffer()
        fileSystem.source(path).use { source ->
            // Read all available bytes into buffer
            var bytesRead: Long
            do {
                bytesRead = source.readAtMostTo(buffer, 8192)
            } while (bytesRead != -1L)
        }
        return buffer.readByteArray()
    }

    /**
     * Get file metadata
     */
    fun getFileMetadata(path: Path): FileMetadata? {
        val fileSystem = getFileSystem()
        return try {
            fileSystem.metadataOrNull(path)
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Check if file exists
     */
    fun exists(path: Path): Boolean {
        val fileSystem = getFileSystem()
        return fileSystem.exists(path)
    }

    /**
     * Get file size
     */
    fun getFileSize(path: Path): Long {
        return getFileMetadata(path)?.size ?: 0L
    }

    /**
     * Determine MIME type from file extension
     */
    fun getMimeType(fileName: String): String {
        val extension = fileName.substringAfterLast('.', "").lowercase()
        return when (extension) {
            "pdf" -> "application/pdf"
            "doc" -> "application/msword"
            "docx" -> "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
            "xls" -> "application/vnd.ms-excel"
            "xlsx" -> "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
            "ppt" -> "application/vnd.ms-powerpoint"
            "pptx" -> "application/vnd.openxmlformats-officedocument.presentationml.presentation"
            "txt" -> "text/plain"
            "csv" -> "text/csv"
            "json" -> "application/json"
            "xml" -> "application/xml"
            "jpg", "jpeg" -> "image/jpeg"
            "png" -> "image/png"
            "gif" -> "image/gif"
            "bmp" -> "image/bmp"
            "svg" -> "image/svg+xml"
            "webp" -> "image/webp"
            "mp4" -> "video/mp4"
            "avi" -> "video/x-msvideo"
            "mov" -> "video/quicktime"
            "wmv" -> "video/x-ms-wmv"
            "mp3" -> "audio/mpeg"
            "wav" -> "audio/wav"
            "ogg" -> "audio/ogg"
            "zip" -> "application/zip"
            "rar" -> "application/x-rar-compressed"
            "7z" -> "application/x-7z-compressed"
            "tar" -> "application/x-tar"
            "gz" -> "application/gzip"
            else -> "application/octet-stream"
        }
    }
}

