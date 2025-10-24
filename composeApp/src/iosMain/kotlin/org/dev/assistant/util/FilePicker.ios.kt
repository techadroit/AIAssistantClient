package org.dev.assistant.util

import kotlinx.cinterop.*
import platform.Foundation.*
import platform.UIKit.*
import platform.UniformTypeIdentifiers.*
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import platform.darwin.*

@OptIn(ExperimentalForeignApi::class)
actual fun getFilePicker(): FilePicker = IOSFilePicker()

@OptIn(ExperimentalForeignApi::class)
class IOSFilePicker : FilePicker {

    private var viewController: UIViewController? = null

    fun setViewController(controller: UIViewController) {
        this.viewController = controller
    }

    override suspend fun pickFile(allowedExtensions: List<String>?): FileData? {
        val controller = viewController ?: throw IllegalStateException("ViewController not set")

        return suspendCancellableCoroutine { continuation ->
            val documentPicker = UIDocumentPickerViewController(
                forOpeningContentTypes = listOf(UTTypeItem),
                asCopy = true
            )

            documentPicker.allowsMultipleSelection = false

            // Use completion handler instead of delegate for simpler implementation
            controller.presentViewController(documentPicker, animated = true) {
                // On completion, check if files were selected
                val urls = documentPicker.URLs() as? List<NSURL>
                if (!urls.isNullOrEmpty()) {
                    val fileData = createFileDataFromURL(urls[0])
                    continuation.resume(fileData)
                } else {
                    continuation.resume(null)
                }
            }
        }
    }

    override suspend fun pickMultipleFiles(allowedExtensions: List<String>?): List<FileData> {
        val controller = viewController ?: throw IllegalStateException("ViewController not set")

        return suspendCancellableCoroutine { continuation ->
            val documentPicker = UIDocumentPickerViewController(
                forOpeningContentTypes = listOf(UTTypeItem),
                asCopy = true
            )

            documentPicker.allowsMultipleSelection = true

            // Use completion handler instead of delegate
            controller.presentViewController(documentPicker, animated = true) {
                val urls = documentPicker.URLs() as? List<NSURL>
                val fileDataList = urls?.mapNotNull { url ->
                    createFileDataFromURL(url)
                } ?: emptyList()
                continuation.resume(fileDataList)
            }
        }
    }

    override suspend fun readFileBytes(fileData: FileData): ByteArray {
        return withContext(Dispatchers.Default) {
            val url = NSURL.URLWithString(fileData.path)
                ?: throw IllegalStateException("Invalid file URL: ${fileData.path}")

            val data = NSData.dataWithContentsOfURL(url)
                ?: throw IllegalStateException("Could not read file: ${fileData.name}")

            // Convert NSData to ByteArray
            val length = data.length.toInt()
            ByteArray(length).apply {
                if (length > 0) {
                    data.getBytes(this.refTo(0).getPointer(MemScope()), length.toULong())
                }
            }
        }
    }

    private fun createFileDataFromURL(url: NSURL): FileData? {
        return try {
            // Get file size by reading the data
            val data = NSData.dataWithContentsOfURL(url)
            val size = data?.length?.toLong() ?: 0L
            val name = url.lastPathComponent ?: "unknown"

            // Use kotlinx-io for consistent MIME type detection
            val mimeType = FileSystemUtil.getMimeType(name)

            FileData(
                name = name,
                path = url.absoluteString ?: "",
                size = size,
                mimeType = mimeType
            )
        } catch (e: Exception) {
            null
        }
    }
}

