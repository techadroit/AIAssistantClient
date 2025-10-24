package org.dev.assistant.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.dev.assistant.util.*

// String Constants
private const val FILE_UPLOADER_TITLE = "File Uploader"
private const val PICK_FILE_BUTTON = "Pick File"
private const val PICK_MULTIPLE_BUTTON = "Pick Multiple"
private const val UPLOAD_FILES_PREFIX = "Upload "
private const val UPLOAD_FILES_SUFFIX = " File(s)"
private const val CLEAR_ALL_BUTTON = "Clear All"
private const val SELECTED_FILES_PREFIX = "Selected Files ("
private const val SELECTED_FILES_SUFFIX = ")"
private const val NO_FILES_SELECTED = "No files selected"
private const val REMOVE_FILE_DESC = "Remove file"
private const val UPLOADING_PREFIX = "Uploading... "
private const val UPLOADING_SUFFIX = "%"
private const val UPLOAD_SUCCESS = "✓ Uploaded successfully"
private const val UPLOAD_ERROR_PREFIX = "✗ Error: "
private const val BYTES_SUFFIX = " B"
private const val KB_SUFFIX = " KB"
private const val MB_SUFFIX = " MB"
private const val GB_SUFFIX = " GB"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FileUploaderScreen(
    filePicker: FilePicker = getFilePicker(),
    fileUploader: FileUploader = FileUploader()
) {
    var selectedFiles by remember { mutableStateOf<List<FileData>>(emptyList()) }
    var isUploading by remember { mutableStateOf(false) }
    var uploadStatus by remember { mutableStateOf<Map<String, UploadState>>(emptyMap()) }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(FILE_UPLOADER_TITLE) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Action buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = {
                        scope.launch {
                            val file = filePicker.pickFile()
                            if (file != null) {
                                selectedFiles = selectedFiles + file
                            }
                        }
                    },
                    modifier = Modifier.weight(1f),
                    enabled = !isUploading
                ) {
                    Icon(Icons.Default.Add, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text(PICK_FILE_BUTTON)
                }

                Button(
                    onClick = {
                        scope.launch {
                            val files = filePicker.pickMultipleFiles()
                            selectedFiles = selectedFiles + files
                        }
                    },
                    modifier = Modifier.weight(1f),
                    enabled = !isUploading
                ) {
                    Icon(Icons.Default.Add, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text(PICK_MULTIPLE_BUTTON)
                }
            }

            Spacer(Modifier.height(16.dp))

            // Upload button
            if (selectedFiles.isNotEmpty()) {
                Button(
                    onClick = {
                        scope.launch {
                            isUploading = true
                            // Placeholder endpoint - replace with your actual endpoint
                            val endpoint = "https://your-api.com/upload"

                            selectedFiles.forEach { file ->
                                // Prepare file by reading bytes first
                                val preparedFile = fileUploader.prepareFileForUpload(file, filePicker)
                                fileUploader.uploadFile(preparedFile, endpoint).collect { state ->
                                    uploadStatus = uploadStatus + (file.name to state)
                                }
                            }
                            isUploading = false
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isUploading
                ) {
                    Icon(Icons.Default.Add, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("$UPLOAD_FILES_PREFIX${selectedFiles.size}$UPLOAD_FILES_SUFFIX")
                }

                Spacer(Modifier.height(8.dp))

                TextButton(
                    onClick = {
                        selectedFiles = emptyList()
                        uploadStatus = emptyMap()
                    },
                    enabled = !isUploading
                ) {
                    Text(CLEAR_ALL_BUTTON)
                }
            }

            Spacer(Modifier.height(16.dp))

            // Selected files list
            if (selectedFiles.isNotEmpty()) {
                Text(
                    "$SELECTED_FILES_PREFIX${selectedFiles.size}$SELECTED_FILES_SUFFIX",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(8.dp))

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(selectedFiles) { file ->
                        FileItemCard(
                            fileData = file,
                            uploadState = uploadStatus[file.name],
                            onRemove = {
                                if (!isUploading) {
                                    selectedFiles = selectedFiles.filter { it.path != file.path }
                                }
                            }
                        )
                    }
                }
            } else {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        NO_FILES_SELECTED,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
fun FileItemCard(
    fileData: FileData,
    uploadState: UploadState?,
    onRemove: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = fileData.name,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = formatFileSize(fileData.size),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = fileData.mimeType,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                IconButton(onClick = onRemove) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = REMOVE_FILE_DESC,
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }

            // Upload status
            when (uploadState) {
                is UploadState.Uploading -> {
                    Spacer(Modifier.height(8.dp))
                    LinearProgressIndicator(
                        progress = { uploadState.progress },
                        modifier = Modifier.fillMaxWidth(),
                    )
                    Text(
                        text = "$UPLOADING_PREFIX${(uploadState.progress * 100).toInt()}$UPLOADING_SUFFIX",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                is UploadState.Success -> {
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = UPLOAD_SUCCESS,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                is UploadState.Error -> {
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = "$UPLOAD_ERROR_PREFIX${uploadState.message}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error
                    )
                }
                else -> {}
            }
        }
    }
}

private fun formatFileSize(bytes: Long): String {
    return when {
        bytes < 1024 -> "$bytes$BYTES_SUFFIX"
        bytes < 1024 * 1024 -> "${bytes / 1024}$KB_SUFFIX"
        bytes < 1024 * 1024 * 1024 -> "${bytes / (1024 * 1024)}$MB_SUFFIX"
        else -> "${bytes / (1024 * 1024 * 1024)}$GB_SUFFIX"
    }
}

