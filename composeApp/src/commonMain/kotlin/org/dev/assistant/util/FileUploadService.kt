package org.dev.assistant.util

/**
 * Service for handling file uploads using kotlinx-io and Ktor
 * Uses HttpClientFactory for HTTP client configuration
 */
class FileUploadService {
    private val httpClient = HttpClientFactory.createFileUploadClient()
    private val fileUploader = FileUploader(httpClient)

    /**
     * Upload a file with authentication
     */
    suspend fun uploadFileWithAuth(
        fileData: FileData,
        endpoint: String,
        authToken: String,
        filePicker: FilePicker
    ) {
        // Prepare file using kotlinx-io
        val preparedFile = fileUploader.prepareFileForUpload(fileData, filePicker)

        // Upload with progress tracking
        fileUploader.uploadFile(preparedFile, endpoint, authToken).collect { state ->
            when (state) {
                is UploadState.Idle -> println("Ready to upload ${fileData.name}")
                is UploadState.Uploading -> {
                    println("Uploading ${fileData.name}: ${(state.progress * 100).toInt()}%")
                }

                is UploadState.Success -> {
                    println("Successfully uploaded ${fileData.name}")
                    println("Response: ${state.url}")
                }

                is UploadState.Error -> {
                    println("Failed to upload ${fileData.name}: ${state.message}")
                }
            }
        }
    }

    /**
     * Upload a file with progress callback (no authentication)
     * Used by ChatViewModel to upload files and track progress
     */
    suspend fun uploadFileWithProgress(
        sessionId: String,
        fileData: FileData,
        endpoint: String,
        filePicker: FilePicker,
        onProgress: (UploadState) -> Unit
    ) {
        // Prepare file using kotlinx-io
        val preparedFile = fileUploader.prepareFileForUpload(fileData, filePicker)

        // Upload with progress tracking
        fileUploader.uploadFile(preparedFile, endpoint, sessionId = sessionId).collect { state ->
            onProgress(state)
        }
    }

    /**
     * Upload a file with progress callback and authentication
     */
    suspend fun uploadFileWithProgress(
        fileData: FileData,
        endpoint: String,
        authToken: String,
        filePicker: FilePicker,
        onProgress: (UploadState) -> Unit
    ) {
        // Prepare file using kotlinx-io
        val preparedFile = fileUploader.prepareFileForUpload(fileData, filePicker)

        // Upload with progress tracking
        fileUploader.uploadFile(preparedFile, endpoint, authToken).collect { state ->
            onProgress(state)
        }
    }

    /**
     * Upload multiple files concurrently
     */
    suspend fun uploadMultipleFiles(
        files: List<FileData>,
        endpoint: String,
        authToken: String,
        filePicker: FilePicker,
        onProgress: (String, UploadState) -> Unit
    ) {
        files.forEach { file ->
            val preparedFile = fileUploader.prepareFileForUpload(file, filePicker)
            fileUploader.uploadFile(preparedFile, endpoint, authToken).collect { state ->
                onProgress(file.name, state)
            }
        }
    }

    /**
     * Upload directly from file path using kotlinx-io
     */
    suspend fun uploadFromPath(
        path: String,
        endpoint: String,
        authToken: String
    ) {
        fileUploader.uploadFromPath(path, endpoint, authToken).collect { state ->
            when (state) {
                is UploadState.Success -> println("Upload complete!")
                is UploadState.Error -> println("Error: ${state.message}")
                else -> {}
            }
        }
    }

    fun cleanup() {
        httpClient.close()
    }
}

