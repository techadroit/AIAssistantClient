package org.dev.assistant.data

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.http.HttpMethod
import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.consumeAsFlow

data class SocketMessage(val content: String)

class WebSocketClient {
    private val client = HttpClient(CIO) {
        install(WebSockets)
    }

    val messageFlow = MutableSharedFlow<SocketMessage>()
    private var session: DefaultClientWebSocketSession? = null

    suspend fun connect() {
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
                    val sMessage = SocketMessage(message)
                    messageFlow.emit(sMessage)
                    println("Received: $message")
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}