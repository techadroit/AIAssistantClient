package org.dev.assistant.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.URLProtocol
import io.ktor.http.appendPathSegments
import io.ktor.http.contentType
import io.ktor.http.path
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
    }

    suspend inline fun <reified T> get(
        path: String? = null,
        pathSegment: Map<String, String> = emptyMap(),
        queryParams: Map<String, String> = emptyMap()
    ): Result<T> {
        return runCatching {
            client.get {
                url {
                    protocol = URLProtocol.HTTPS
                    host = baseUrl
                    path?.let {
                        path(it)
                    }
                    pathSegment.forEach { (key, value) ->
                        appendPathSegments(key, value)
                    }
                    queryParams.forEach { (key, value) ->
                        parameters.append(key, value)
                    }
                }
            }.body<T>()
        }
    }

    suspend inline fun <reified T> post(
        path: String? = null,
        pathSegment: Map<String, String> = emptyMap(),
        queryParams: Map<String, String> = emptyMap(),
        body: Any? = null
    ): Result<T> {
        return runCatching {
            client.post {
                url {
                    protocol = URLProtocol.HTTPS
                    host = baseUrl
                    path?.let {
                        path(it)
                    }
                    pathSegment.forEach { (key, value) ->
                        appendPathSegments(key, value)
                    }
                    queryParams.forEach { (key, value) ->
                        parameters.append(key, value)
                    }
                }
                body?.let {
                    contentType(ContentType.Application.Json)
                    setBody(it)
                }
            }.body<T>()
        }
    }

    suspend inline fun <reified T> put(
        path: String? = null,
        pathSegment: Map<String, String> = emptyMap(),
        queryParams: Map<String, String> = emptyMap(),
        body: Any? = null
    ): Result<T> {
        return runCatching {
            client.put {
                url {
                    protocol = URLProtocol.HTTPS
                    host = baseUrl
                    path?.let {
                        path(it)
                    }
                    pathSegment.forEach { (key, value) ->
                        appendPathSegments(key, value)
                    }
                    queryParams.forEach { (key, value) ->
                        parameters.append(key, value)
                    }
                }
                body?.let {
                    contentType(ContentType.Application.Json)
                    setBody(it)
                }
            }.body<T>()
        }
    }

    suspend inline fun <reified T> delete(
        path: String? = null,
        pathSegment: Map<String, String> = emptyMap(),
        queryParams: Map<String, String> = emptyMap()
    ): Result<T> {
        return runCatching {
            client.delete {
                url {
                    protocol = URLProtocol.HTTPS
                    host = baseUrl
                    path?.let {
                        path(it)
                    }
                    pathSegment.forEach { (key, value) ->
                        appendPathSegments(key, value)
                    }
                    queryParams.forEach { (key, value) ->
                        parameters.append(key, value)
                    }
                }
            }.body<T>()
        }
    }

}