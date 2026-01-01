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
import org.dev.assistant.util.logger.AppLogger

//data class SocketMessage(val content: String)

class WebSocketClient(val userService: UserService) {

    private val logger = AppLogger("WebSocketClient")
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
        logger.info("Initiating WebSocket connection...")
        scope.launch {
            withContext(Dispatchers.Default) {
                try {
                    userService.getUserId().getOrNull()?.let { userId ->
                        var url = getUrlProvider().wsUrl + "/" + userId
                        logger.debug("WebSocket URL constructed: $url")
                        logger.debug("User ID: $userId")
                        println(" Connecting to $url")
                        tryConnect(url)
                    } ?: run {
                        logger.error("Failed to connect: User ID is null")
                    }
                } catch (e: Exception) {
                    logger.error("WebSocket connection failed", e)
                    e.printStackTrace()
                    _isConnected.value = false
                }
            }
        }
    }

    fun disconnect() {
        logger.info("Disconnecting WebSocket...")
        scope.launch {
            try {
                session?.close(
                    reason = CloseReason(
                        code = CloseReason.Codes.NORMAL,
                        message = "Updating URL"
                    )
                )
                logger.info("WebSocket disconnected successfully")
            } catch (e: Exception) {
                logger.error("Error during WebSocket disconnect", e)
            }
        }
    }

    private suspend fun tryConnect(url: String) {
        logger.debug("Attempting to establish WebSocket connection to: $url")
        coroutineScope {
            try {
                client.webSocket(
                    urlString = url
                ) {
                    println(" client connected ")
                    logger.info("✅ WebSocket connected successfully")
                    _isConnected.value = true
                    session = this
                    receiveMessages(this)
                    observeClose(this)
                    // this: DefaultClientWebSocketSession
                }
            } catch (e: Exception) {
                logger.error("Failed to establish WebSocket connection", e)
                throw e
            }
        }
    }

    suspend fun sendMessage(content: String) {
        try {
            logger.debug("Preparing to send WebSocket message")
            // URL encode the content (useful for JSON strings)
            val sendingText = Frame.Text(content)
            println(" sending message $content")
            session?.send(sendingText)
            logger.debug("✅ Message sent successfully")
        } catch (e: Exception) {
            logger.error("Failed to send WebSocket message", e)
            e.printStackTrace()
        }
    }

    private fun observeClose(session: DefaultClientWebSocketSession) {
        scope.launch {
            try {
                val reason = session.closeReason.await()
                logger.warn("WebSocket connection closed. Reason: $reason")
                println("Connection lost $reason")
                _isConnected.value = false
            } catch (e: Exception) {
                logger.error("Error while observing close reason", e)
                e.printStackTrace()
            }
        }
    }

    suspend fun receiveMessages(session: DefaultClientWebSocketSession) {
        logger.debug("Starting to listen for WebSocket messages...")
        try {
            session.incoming.consumeAsFlow().collect { frame ->
                if (frame is Frame.Text) {
                    val message = frame.readText()
                    logger.debug("Received WebSocket message: ${message.take(100)}${if (message.length > 100) "..." else ""}")
                    println("Received message: $message")
                    try {
                        val response = json.decodeFromString<ChatMessages>(message)
                        messageFlow.tryEmit(response)
                        logger.debug("Message parsed and emitted successfully")
                    } catch (e: Exception) {
                        logger.error("Failed to parse WebSocket message", e)
                    }
                }
            }
        } catch (e: Exception) {
            logger.error("Error while receiving WebSocket messages", e)
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