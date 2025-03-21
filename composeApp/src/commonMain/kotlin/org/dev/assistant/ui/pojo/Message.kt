package org.dev.assistant.ui.pojo

sealed class Message(val content: String)
data class SentMessage(val msg: String) : Message(msg)
data class ReceiveMessage(val msg: String) : Message(msg)