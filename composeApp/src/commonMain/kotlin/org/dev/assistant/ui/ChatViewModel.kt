package org.dev.assistant.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.dev.assistant.data.SessionManager
import org.dev.assistant.data.WebSocketClient
import org.dev.assistant.ui.pojo.ChatMessages
import org.dev.assistant.ui.pojo.Message
import org.dev.assistant.ui.pojo.SentMessage
import org.dev.assistant.util.FileData
import org.dev.assistant.util.FilePicker
import org.dev.assistant.util.FileUploadService
import org.dev.assistant.util.MessageParser
import org.dev.assistant.util.UploadState


class ChatViewModel : ViewModel() {
    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    private val sessionManager = SessionManager()
    val messages: StateFlow<List<Message>> = _messages
    val websocketClient = WebSocketClient()
    private val _isConnected = MutableStateFlow(false)
    val isConnected: StateFlow<Boolean> = _isConnected

    // File upload service instance
    private val fileUploadService = FileUploadService()

    // Upload state
    private val _uploadState = MutableStateFlow<UploadState>(UploadState.Idle)
    val uploadState: StateFlow<UploadState> = _uploadState

    // Agent mode state
    private val _isAgentMode = MutableStateFlow(false)
    val isAgentMode: StateFlow<Boolean> = _isAgentMode

    // Base URL for your FastAPI server
    private var apiBaseUrl = "http://localhost:8001/api" // Update this with your server URL

    init {
        viewModelScope.launch {
            observeWebConnection()
            receiveMessages()
        }
    }

    private suspend fun receiveMessages() {
        try {
            websocketClient.messageFlow
                .collect { socketMessage ->
                    processMessage(socketMessage)
                }
        } catch (e: Exception) {
            println("Error collecting messages: ${e.message}")
        }
    }

    fun processMessage(chatMessages: ChatMessages) {
        val message = MessageParser.parseMessage(chatMessages)
        val count = _messages.value.count { it.id == message.id }
        if (count == 0) {
            _messages.value += message
        } else {
            _messages.value = _messages.value.map { existingMessage ->
                if (existingMessage.id == message.id) {
                    val newMessage = message.copy(msg = existingMessage.msg + message.msg)
                    newMessage
                } else existingMessage
            }
        }
    }

    fun sendMessage(content: String) {
        viewModelScope.launch {
            val message = SentMessage(
                msg = content,
                id = "",
                agentMode = org.dev.assistant.ui.pojo.ChatMode(mode = if (_isAgentMode.value) 1 else 0)
            )
            _messages.value += message
            websocketClient.sendMessage(MessageParser.toChatMessage(message))
        }
    }

    /**
     * Toggle agent mode on/off
     */
    fun setAgentMode(enabled: Boolean) {
        _isAgentMode.value = enabled
    }

    fun refresh() {
        disconnect()
        clearChatHistory()
        connect()
    }

    fun clearChatHistory() {
        _messages.value = emptyList()
    }

    fun connect() {
        viewModelScope.launch {
            websocketClient.connect()
        }
    }

    fun disconnect() {
        viewModelScope.launch {
            websocketClient.disconnect()
        }
    }

    fun showSettingsMenu() {
        // Logic to show settings menu
    }

    fun observeWebConnection() {
        viewModelScope.launch {
            websocketClient.isConnected.collect {
                _isConnected.value = it
            }
        }
    }

    fun updateUrl(url: String) {
        viewModelScope.launch {
            websocketClient.updateUrl(url)
            connect()
        }
    }

    /**
     * Upload a file to the FastAPI server using FileUploadService
     * @param fileData The file to upload
     * @param filePicker FilePicker instance to read file bytes
     */
    fun uploadFile(fileData: FileData, filePicker: FilePicker) {
        viewModelScope.launch {
            try {
                _uploadState.value = UploadState.Uploading(0.1f)

                // Upload to FastAPI endpoint using FileUploadService
                val endpoint = "$apiBaseUrl/uploadfile"

                // Use FileUploadService with progress callback
                fileUploadService.uploadFileWithProgress(
                    sessionId = sessionManager.getSessionId(),
                    fileData = fileData,
                    endpoint = endpoint,
                    filePicker = filePicker,
                    onProgress = { state ->
                        _uploadState.value = state

                        when (state) {
                            is UploadState.Success -> {
                                // Add a message to chat indicating file was uploaded
                                _messages.value += SentMessage(
                                    msg = "📎 Uploaded file: ${fileData.name}",
                                    id = ""
                                )
                                println("File uploaded successfully: ${fileData.name}")
                            }
                            is UploadState.Error -> {
                                println("Upload error: ${state.message}")
                            }
                            else -> {}
                        }
                    }
                )
            } catch (e: Exception) {
                _uploadState.value = UploadState.Error(e.message ?: "Unknown error")
                println("Upload exception: ${e.message}")
            }
        }
    }

    /**
     * Update the API base URL
     */
    fun updateApiBaseUrl(url: String) {
        apiBaseUrl = url
    }

    /**
     * Clean up resources
     */
    override fun onCleared() {
        super.onCleared()
        fileUploadService.cleanup()
    }
}