package org.dev.assistant.util

import io.ktor.client.*
import io.ktor.client.plugins.onUpload
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.utils.io.core.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Represents the upload state of a file
 */
sealed class UploadState {
    object Idle : UploadState()
    data class Uploading(val progress: Float) : UploadState()
    data class Success(val url: String? = null) : UploadState()
    data class Error(val message: String) : UploadState()
}

/**
 * Service for uploading files using Ktor and kotlinx-io
 */
class FileUploader(private val httpClient: HttpClient? = null) {

    /**
     * Upload a single file
     * @param fileData The file to upload
     * @param endpoint The server endpoint URL
     * @param authToken Optional authorization token
     * @return Flow of UploadState representing the upload progress
     */
    fun uploadFile(
        fileData: FileData,
        endpoint: String,
        authToken: String? = null,
        sessionId: String = ""
    ): Flow<UploadState> = flow {
        emit(UploadState.Idle)

        try {
            emit(UploadState.Uploading(0.1f))

            // Get the file bytes if not already loaded
            val bytes = fileData.bytes ?: throw IllegalStateException(
                "File bytes not loaded. Call prepareFileForUpload first."
            )

//            emit(UploadState.Uploading(0.3f))

//            if (httpClient == null) {
//                // Simulate upload if no client provided (for testing/preview)
//                emit(UploadState.Uploading(0.6f))
//                emit(UploadState.Uploading(0.9f))
//                emit(UploadState.Success("Simulated upload - no HTTP client provided"))
//                return@flow
//            }
            val uploadUrl = "$endpoint?session_id=$sessionId"
            // Perform actual upload using Ktor
            val response = httpClient?.post(uploadUrl) {
//                if (authToken != null) {
//                    header(HttpHeaders.Authorization, "Bearer $authToken")
//                }

                setBody(MultiPartFormDataContent(
                    formData {
                        append("file", bytes, Headers.build {
                            append(HttpHeaders.ContentType, fileData.mimeType)
                            append(
                                HttpHeaders.ContentDisposition,
                                "filename=\"${fileData.name}\""
                            )
                        })
                        // Add additional metadata
                        append("filename", fileData.name)
                        append("filesize", fileData.size.toString())
                        append("mimetype", fileData.mimeType)
                        append("session_Id", sessionId)
                    }
                ))

                onUpload { bytesSentTotal, contentLength ->
                    // Calculate and emit progress
                    val progress = if (contentLength!! > 0) {
                        (bytesSentTotal.toFloat() / contentLength.toFloat()).coerceIn(0f, 1f)
                    } else {
                        0.5f
                    }
                    // Note: This won't emit in the flow due to coroutine context
                    // For real progress tracking, you'd need to use a Channel
                }
            }

            emit(UploadState.Uploading(1.0f))

            if (response?.status?.isSuccess() == true) {
                val responseText = response.bodyAsText()
                emit(UploadState.Success(responseText))
            } else {
                emit(UploadState.Error("Upload failed with status: ${response?.status}"))
            }

        } catch (e: Exception) {
            emit(UploadState.Error(e.message ?: "Unknown error occurred"))
        }
    }

    /**
     * Upload multiple files
     * @param files List of files to upload
     * @param endpoint The server endpoint URL
     * @param authToken Optional authorization token
     * @return Map of file names to their upload state flows
     */
    fun uploadMultipleFiles(
        files: List<FileData>,
        endpoint: String,
        authToken: String? = null
    ): Map<String, Flow<UploadState>> {
        return files.associate { fileData ->
            fileData.name to uploadFile(fileData, endpoint, authToken)
        }
    }

    /**
     * Prepare file for upload by reading its bytes using kotlinx-io
     * @param fileData The file to prepare
     * @param filePicker The file picker to use for reading bytes
     * @return FileData with bytes loaded
     */
    suspend fun prepareFileForUpload(fileData: FileData, filePicker: FilePicker): FileData {
        val bytes = filePicker.readFileBytes(fileData)
        return fileData.copy(bytes = bytes)
    }

    /**
     * Upload file directly from path using kotlinx-io
     * @param path The file path
     * @param endpoint The server endpoint URL
     * @param authToken Optional authorization token
     * @return Flow of UploadState
     */
    suspend fun uploadFromPath(
        path: String,
        endpoint: String,
        authToken: String? = null
    ): Flow<UploadState> {
        return flow {
            try {
                emit(UploadState.Uploading(0.1f))

                // Read file using kotlinx-io
                val ioPath = kotlinx.io.files.Path(path)
                val bytes = FileSystemUtil.readFileBytes(ioPath)
                val fileName = ioPath.name
                val size = bytes.size.toLong()
                val mimeType = FileSystemUtil.getMimeType(fileName)

                val fileData = FileData(
                    name = fileName,
                    path = path,
                    size = size,
                    mimeType = mimeType,
                    bytes = bytes
                )

                // Use existing upload flow
                uploadFile(fileData, endpoint, authToken).collect { state ->
                    emit(state)
                }
            } catch (e: Exception) {
                emit(UploadState.Error(e.message ?: "Failed to read file"))
            }
        }
    }
}

