package org.dev.assistant.util

/**
 * Interface for picking files from the file system
 * Platform-specific implementations will be provided for each target platform
 */
interface FilePicker {
    /**
     * Pick a single file from the file system
     * @param allowedExtensions List of allowed file extensions (e.g., ["jpg", "png", "pdf"])
     * @return FileData if a file was selected, null otherwise
     */
    suspend fun pickFile(allowedExtensions: List<String>? = null): FileData?

    /**
     * Pick multiple files from the file system
     * @param allowedExtensions List of allowed file extensions (e.g., ["jpg", "png", "pdf"])
     * @return List of FileData for all selected files
     */
    suspend fun pickMultipleFiles(allowedExtensions: List<String>? = null): List<FileData>

    /**
     * Read file content as bytes
     * @param fileData The file data to read
     * @return ByteArray containing file content
     */
    suspend fun readFileBytes(fileData: FileData): ByteArray
}

/**
 * Expect function to get platform-specific file picker implementation
 */
expect fun getFilePicker(): FilePicker

