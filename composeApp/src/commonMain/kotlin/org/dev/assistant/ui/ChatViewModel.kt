package org.dev.assistant.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.dev.assistant.data.WebSocketClient

//data class Message(val content: String, val isSent: Boolean)

sealed class Message(val content: String)
data class SentMessage(val msg: String) : Message(msg)
data class ReceiveMessage(val msg: String) : Message(msg)

class ChatViewModel : ViewModel() {
    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages
    val websocketClient = WebSocketClient()

    init {
        viewModelScope.launch {
            websocketClient.connect()
        }
    }

    private suspend fun receiveMessages() {
        websocketClient.messageFlow.collectLatest {
            _messages.value += ReceiveMessage(it.content)
        }
    }

    fun sendMessage(content: String) {
        viewModelScope.launch {
            _messages.value += SentMessage(content)
            websocketClient.sendMessage(content)
        }
    }
}