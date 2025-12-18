package org.dev.assistant.util

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.dev.assistant.ui.pojo.ChatMessages
import org.dev.assistant.ui.pojo.ReceiveMessage
import org.dev.assistant.ui.pojo.SentMessage

object MessageParser {

    // JSON instance for serialization
    private val json = Json {
        ignoreUnknownKeys = true
        prettyPrint = false
        isLenient = true
    }

//    fun parseMessage(message: SocketMessage): ReceiveMessage {
//
//        println(" the message is $message")
//
//        return ReceiveMessage(
//            msg = message.responseMessage ?: "",
//            id = message.messageId ?: ""
//        )
//    }

    fun parseMessage(message: ChatMessages): ReceiveMessage {

        println(" the message is $message")

        return ReceiveMessage(
            msg = message.message.messages,
            id = message.messageId
        )
    }

    fun toChatMessage(message: SentMessage, chatSessionId: String): String {
        val chatMessages = ChatMessages(
            messageId = message.id,
            chatSessionId = chatSessionId,
            utcTime = "",
            sender = "user",
            receiver = "assistant",
            message = org.dev.assistant.ui.pojo.ChatMessageBody(messages = message.msg),
            chat_mode = message.agentMode
        )
        return json.encodeToString(chatMessages)
    }
}