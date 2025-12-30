package org.dev.assistant.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.engine.cio.endpoint
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class NetworkClient(val baseUrl: String) {

    val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
            })
        }
        install(Logging)
        engine {
            endpoint {
                connectTimeout = 30_000
                socketTimeout = 30_000
            }
        }
    }

    suspend inline fun <reified T> get(
        path: String,
        queryParams: Map<String, String> = emptyMap()
    ): Result<T> {
        return runCatching {
            client.get("$baseUrl$path") {
                queryParams.forEach { (key, value) ->
                    parameter(key, value)
                }
            }.body<T>()
        }
    }

    suspend inline fun <reified T> post(
        path: String,
        body: Any? = null,
        queryParams: Map<String, String> = emptyMap()
    ): Result<T> {
        return runCatching {
            client.post("$baseUrl$path") {
                queryParams.forEach { (key, value) ->
                    parameter(key, value)
                }
                body?.let {
                    contentType(ContentType.Application.Json)
                    setBody(it)
                }
            }.body<T>()
        }
    }

    suspend inline fun <reified T> put(
        path: String,
        body: Any? = null,
        queryParams: Map<String, String> = emptyMap()
    ): Result<T> {
        return runCatching {
            client.put("$baseUrl$path") {
                queryParams.forEach { (key, value) ->
                    parameter(key, value)
                }
                body?.let {
                    contentType(ContentType.Application.Json)
                    setBody(it)
                }
            }.body<T>()
        }
    }

    suspend inline fun <reified T> delete(
        path: String,
        queryParams: Map<String, String> = emptyMap()
    ): Result<T> {
        return runCatching {
            client.delete("$baseUrl$path") {
                queryParams.forEach { (key, value) ->
                    parameter(key, value)
                }
            }.body<T>()
        }
    }
}
