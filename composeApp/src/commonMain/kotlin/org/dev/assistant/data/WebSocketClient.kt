package org.dev.assistant.data

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.http.HttpMethod
import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

data class SocketMessage(val content: String)

class WebSocketClient {
    val scope = CoroutineScope(Dispatchers.IO + Job())
    private val client = HttpClient(CIO) {
        install(WebSockets)
    }

    val messageFlow = MutableSharedFlow<SocketMessage>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
        extraBufferCapacity = 1
    )
    private var session: DefaultClientWebSocketSession? = null

    fun connect() {
        scope.launch {
            tryConnect()
        }
    }

    private suspend fun tryConnect() {
        try {
            client.webSocket(
                method = HttpMethod.Get,
                host = "10.0.2.2",
                port = 8000,
                path = "/ws"
            ) {
                println(" client connected ")
                session = this
                receiveMessages(this)
                // this: DefaultClientWebSocketSession
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun sendMessage(content: String) {
        try {
            session?.send(Frame.Text(content))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun receiveMessages(session: DefaultClientWebSocketSession) {
        try {
            session.incoming.consumeAsFlow().collect { frame ->
                if (frame is Frame.Text) {
                    val message = frame.readText()
                    println("Received message: $message")
                    val sMessage = SocketMessage(message)
                    messageFlow.tryEmit(sMessage)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}