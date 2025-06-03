package org.dev.assistant.ui.pojo

sealed class Message(open val msg: String)
data class SentMessage(override val msg: String) : Message(msg)
data class ReceiveMessage(override val msg: String, val imageUrl:List<String> = emptyList(), val messageType:String) : Message(msg)