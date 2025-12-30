package org.dev.assistant.util

import io.ktor.util.date.getTimeMillis
import kotlinx.datetime.Clock
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
        encodeDefaults = true
    }

    fun parseMessage(message: ChatMessages): ReceiveMessage {

        println(" the message is $message")

        return ReceiveMessage(
            msg = message.message.messages,
            id = message.messageId,
            agentMode = message.chat_mode
        )
    }

    fun toChatMessage(message: SentMessage, chatSessionId: String): String {
        val chatMessages = ChatMessages(
            messageId = getTimeMillis().toString(),
            chatSessionId = chatSessionId,
            utcTime = currentUtcTimestamp(),
            sender = "user",
            receiver = "assistant",
            message = org.dev.assistant.ui.pojo.ChatMessageBody(messages = message.msg),
            chat_mode = message.agentMode
        )
        val res = json.encodeToString(chatMessages)
        return res
    }
}
