package org.dev.assistant.ui.pojo

sealed class Message(open val msg: String, open val id: String)
data class SentMessage(override val msg: String, override val id: String) : Message(msg, id)
data class ReceiveMessage(
    override val msg: String,
    override val id: String,
) : Message(msg, id)