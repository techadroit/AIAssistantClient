package org.dev.assistant.ui.pojo

sealed class Message(
    open val msg: String,
    open val id: String,
    open val agentMode: ChatMode
)

data class SentMessage(
    override val msg: String,
    override val id: String,
    override val agentMode: ChatMode = ChatMode()
) : Message(msg, id, agentMode)

data class ReceiveMessage(
    override val msg: String,
    override val id: String,
    override val agentMode: ChatMode = ChatMode()
) : Message(msg, id, agentMode)