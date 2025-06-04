package org.dev.assistant.ui.pojo

import org.dev.assistant.data.Product

sealed class Message(open val msg: String)
data class SentMessage(override val msg: String) : Message(msg)
data class ReceiveMessage(
    override val msg: String,
    val products: List<Product> = emptyList(),
    val messageType: String
) : Message(msg)