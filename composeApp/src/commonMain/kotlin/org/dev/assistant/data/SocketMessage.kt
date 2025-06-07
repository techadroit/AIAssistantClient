package org.dev.assistant.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SocketMessage(
    @SerialName("message_type")
    val messageType: String,
    @SerialName("response_message")
    val responseMessage: String?,
    @SerialName("error_message")
    val errorMessage: String?,
    @SerialName("error_code")
    val errorCode: Int?,
    @SerialName("content")
    val content: Content = Content(),
    @SerialName("content_type")
    val contentType: String
)

@Serializable
data class Content(
    val products: List<Product> = emptyList()
)

@Serializable
data class Product(
    val name: String,
    val price: Double,
    @SerialName("image_url")
    val imageUrl: String
)