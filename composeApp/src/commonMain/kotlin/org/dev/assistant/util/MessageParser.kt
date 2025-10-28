package org.dev.assistant.util

import org.dev.assistant.ui.pojo.ChatMessages
import org.dev.assistant.ui.pojo.ReceiveMessage
import org.dev.assistant.ui.pojo.SentMessage

object MessageParser {

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

    fun toChatMessage(message: SentMessage): ChatMessages{
        return ChatMessages(
            messageId = message.id,
            utcTime = "",
            sender = "user",
            receiver = "assistant",
            message = org.dev.assistant.ui.pojo.ChatMessageBody(messages = message.msg),
            chat_mode = message.agentMode
        )
    }
}