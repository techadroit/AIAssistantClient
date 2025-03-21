package org.dev.assistant.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
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
                    println("Collected from flow: ${socketMessage.content}")
                    _messages.value += ReceiveMessage(socketMessage.content)
                }
        } catch (e: Exception) {
            println("Error collecting messages: ${e.message}")
        }
    }

    fun sendMessage(content: String) {
        viewModelScope.launch {
            _messages.value += SentMessage(content)
            websocketClient.sendMessage(content)
        }
    }

    fun connect(){
        viewModelScope.launch {
            websocketClient.connect()
        }
    }

    fun disconnect(){
        viewModelScope.launch {
            websocketClient.disconnect()
        }
    }

    fun showSettingsMenu() {
        // Logic to show settings menu
    }

    fun observeWebConnection(){
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