package org.dev.assistant.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.dev.assistant.data.WebSocketClient
import org.dev.assistant.ui.pojo.ChatMessages
import org.dev.assistant.ui.pojo.Message
import org.dev.assistant.ui.pojo.SentMessage
import org.dev.assistant.util.MessageParser


class ChatViewModel : ViewModel() {
    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages
    val websocketClient = WebSocketClient()
    private val _isConnected = MutableStateFlow(false)
    val isConnected: StateFlow<Boolean> = _isConnected

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
                if (existingMessage.id == message.id){
                    val newMessage = message.copy(msg = existingMessage.msg + message.msg)
                    newMessage
                } else existingMessage
            }
        }
    }

    fun sendMessage(content: String) {
        viewModelScope.launch {
            _messages.value += SentMessage(msg = content, id = "")
            websocketClient.sendMessage(content)
        }
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
}