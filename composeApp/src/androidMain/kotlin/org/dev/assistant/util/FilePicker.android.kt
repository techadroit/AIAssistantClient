package org.dev.assistant.util

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume

actual fun getFilePicker(): FilePicker = AndroidFilePicker()

class AndroidFilePicker : FilePicker {

    private var activity: ComponentActivity? = null

    fun setActivity(activity: ComponentActivity) {
        this.activity = activity
    }

    override suspend fun pickFile(allowedExtensions: List<String>?): FileData? {
        val currentActivity = activity ?: throw IllegalStateException("Activity not set")

        return suspendCancellableCoroutine { continuation ->
            val launcher = currentActivity.activityResultRegistry.register(
                "file_picker_${System.currentTimeMillis()}",
                ActivityResultContracts.GetContent()
            ) { uri: Uri? ->
                if (uri != null) {
                    val fileData = createFileDataFromUri(currentActivity, uri)
                    continuation.resume(fileData)
                } else {
                    continuation.resume(null)
                }
            }

            continuation.invokeOnCancellation {
                launcher.unregister()
            }

            val mimeType = if (!allowedExtensions.isNullOrEmpty()) {
                "*/*" // We'll handle filtering in the intent
            } else {
                "*/*"
            }

            launcher.launch(mimeType)
        }
    }

    override suspend fun pickMultipleFiles(allowedExtensions: List<String>?): List<FileData> {
        val currentActivity = activity ?: throw IllegalStateException("Activity not set")

        return suspendCancellableCoroutine { continuation ->
            val launcher = currentActivity.activityResultRegistry.register(
                "file_picker_multiple_${System.currentTimeMillis()}",
                ActivityResultContracts.GetMultipleContents()
            ) { uris: List<Uri> ->
                val fileDataList = uris.mapNotNull { uri ->
                    createFileDataFromUri(currentActivity, uri)
                }
                continuation.resume(fileDataList)
            }

            continuation.invokeOnCancellation {
                launcher.unregister()
            }

            val mimeType = if (!allowedExtensions.isNullOrEmpty()) {
                "*/*"
            } else {
                "*/*"
            }

            launcher.launch(mimeType)
        }
    }

    override suspend fun readFileBytes(fileData: FileData): ByteArray {
        val currentActivity = activity ?: throw IllegalStateException("Activity not set")

        return withContext(Dispatchers.IO) {
            currentActivity.contentResolver.openInputStream(Uri.parse(fileData.path))?.use { inputStream ->
                inputStream.readBytes()
            } ?: throw IllegalStateException("Could not read file: ${fileData.name}")
        }
    }

    private fun createFileDataFromUri(context: Context, uri: Uri): FileData? {
        return try {
            val cursor = context.contentResolver.query(uri, null, null, null, null)
            var name = ""
            var size = 0L

            cursor?.use {
                if (it.moveToFirst()) {
                    val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    val sizeIndex = it.getColumnIndex(OpenableColumns.SIZE)

                    if (nameIndex >= 0) {
                        name = it.getString(nameIndex)
                    }
                    if (sizeIndex >= 0) {
                        size = it.getLong(sizeIndex)
                    }
                }
            }

            // Use kotlinx-io for consistent MIME type detection
            val mimeType = context.contentResolver.getType(uri)
                ?: FileSystemUtil.getMimeType(name)

            FileData(
                name = name,
                path = uri.toString(),
                size = size,
                mimeType = mimeType
            )
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}

