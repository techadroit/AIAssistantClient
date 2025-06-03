package org.dev.assistant.util

import org.dev.assistant.data.SocketMessage
import org.dev.assistant.ui.pojo.ReceiveMessage

object MessageParser {

    fun parseMessage(message: SocketMessage): ReceiveMessage {

        println(" the message is $message")

        return ReceiveMessage(
            msg = message.responseMessage ?: "",
            imageUrl = emptyList(),
            messageType = "text"
        )
    }
}