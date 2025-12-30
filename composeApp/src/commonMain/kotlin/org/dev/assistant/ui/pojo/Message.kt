package org.dev.assistant.ui.pojo

sealed class Message(
    open val msg: String,
    open val id: String,
    open val agentMode: ChatModeType
)

data class SentMessage(
    override val msg: String,
    override val id: String,
    override val agentMode: ChatModeType
) : Message(msg, id, agentMode)

data class ReceiveMessage(
    override val msg: String,
    override val id: String,
    override val agentMode: ChatModeType
) : Message(msg, id, agentMode)