package org.dev.assistant.util

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.io.files.Path
import java.awt.FileDialog
import java.awt.Frame
import java.io.File

const val SELECT_FILE = "Select File"

actual fun getFilePicker(): FilePicker = DesktopFilePicker()

class DesktopFilePicker : FilePicker {

    override suspend fun pickFile(allowedExtensions: List<String>?): FileData? {
        return withContext(Dispatchers.IO) {
            val fileDialog = FileDialog(null as Frame?, SELECT_FILE, FileDialog.LOAD)

            if (!allowedExtensions.isNullOrEmpty()) {
                fileDialog.setFilenameFilter { _, name ->
                    allowedExtensions.any { ext ->
                        name.lowercase().endsWith(".$ext")
                    }
                }
            }

            fileDialog.isVisible = true

            val directory = fileDialog.directory
            val fileName = fileDialog.file

            if (directory != null && fileName != null) {
                val file = File(directory, fileName)
                if (file.exists()) {
                    createFileData(file)
                } else {
                    null
                }
            } else {
                null
            }
        }
    }

    override suspend fun pickMultipleFiles(allowedExtensions: List<String>?): List<FileData> {
        return withContext(Dispatchers.IO) {
            val fileDialog = FileDialog(null as Frame?, SELECT_FILE, FileDialog.LOAD)
            fileDialog.isMultipleMode = true

            if (!allowedExtensions.isNullOrEmpty()) {
                fileDialog.setFilenameFilter { _, name ->
                    allowedExtensions.any { ext ->
                        name.lowercase().endsWith(".$ext")
                    }
                }
            }

            fileDialog.isVisible = true

            val files = fileDialog.files
            files?.mapNotNull { file ->
                if (file.exists()) {
                    createFileData(file)
                } else {
                    null
                }
            } ?: emptyList()
        }
    }

    override suspend fun readFileBytes(fileData: FileData): ByteArray {
        return withContext(Dispatchers.IO) {
            // Use kotlinx-io for cross-platform file reading
            val path = Path(fileData.path)
            FileSystemUtil.readFileBytes(path)
        }
    }

    private fun createFileData(file: File): FileData {
        val mimeType = FileSystemUtil.getMimeType(file.name)

        return FileData(
            name = file.name,
            path = file.absolutePath,
            size = file.length(),
            mimeType = mimeType
        )
    }
}

