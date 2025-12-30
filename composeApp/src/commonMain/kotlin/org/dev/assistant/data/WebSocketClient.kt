package org.dev.assistant.data

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.websocket.CloseReason
import io.ktor.websocket.Frame
import io.ktor.websocket.close
import io.ktor.websocket.readText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import org.dev.assistant.domain.UserService
import org.dev.assistant.platform.getUrlProvider
import org.dev.assistant.ui.pojo.ChatMessages

//data class SocketMessage(val content: String)

class WebSocketClient(val userService: UserService) {


    private val json = Json { ignoreUnknownKeys = true }
    val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    private val client = HttpClient(CIO) {
        install(WebSockets) {
//            pingInterval = 50000
        }
    }

    val messageFlow = MutableSharedFlow<ChatMessages>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
        extraBufferCapacity = 1
    )
    private val _isConnected = MutableStateFlow(false)
    val isConnected: StateFlow<Boolean> get() = _isConnected
    private var session: DefaultClientWebSocketSession? = null

    fun connect() {
        scope.launch {

            withContext(Dispatchers.Default) {
                try {
                    userService.getUserId().getOrNull()?.let {
                        var url = getUrlProvider().wsUrl + "/" + it
                        println(" Connecting to $url")
                        tryConnect(url)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    _isConnected.value = false
                }
            }
        }
    }

    fun disconnect() {
        scope.launch {
            session?.close(
                reason = CloseReason(
                    code = CloseReason.Codes.NORMAL,
                    message = "Updating URL"
                )
            )
        }
    }

    private suspend fun tryConnect(url: String) {
        coroutineScope {
            client.webSocket(
                urlString = url
            ) {
                println(" client connected ")
                _isConnected.value = true
                session = this
                receiveMessages(this)
                observeClose(this)
                // this: DefaultClientWebSocketSession
            }
        }
    }

    suspend fun sendMessage(content: String) {
        try {
            // URL encode the content (useful for JSON strings)
            val sendingText = Frame.Text(content)
            println(" sending message $content")
            session?.send(sendingText)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun observeClose(session: DefaultClientWebSocketSession) {
        scope.launch {
            try {
                val reason = session.closeReason.await()
                println("Connection lost $reason")
                _isConnected.value = false
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    suspend fun receiveMessages(session: DefaultClientWebSocketSession) {
        try {
            session.incoming.consumeAsFlow().collect { frame ->
                if (frame is Frame.Text) {
                    val message = frame.readText()
                    println("Received message: $message")
                    val response = json.decodeFromString<ChatMessages>(message)
                    messageFlow.tryEmit(response)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            _isConnected.value = false
        }
    }

    fun updateUrl(newUrl: String) {
        scope.launch {
            disconnect()
//            url = newUrl
        }
    }
}