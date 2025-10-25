package org.dev.assistant.util

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

/**
 * Factory for creating configured HTTP clients
 * Provides reusable HTTP client configurations for different use cases
 */
object HttpClientFactory {

    /**
     * Create a configured HTTP client for file uploads
     * Features:
     * - JSON content negotiation
     * - Extended timeout (5 minutes) for large file uploads
     * - CIO engine for multiplatform support
     */
    fun createFileUploadClient(): HttpClient {
        return HttpClient(CIO) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    prettyPrint = true
                    isLenient = true
                })
            }

            // Configure timeout settings for large files
            engine {
                requestTimeout = 300_000 // 5 minutes
            }
        }
    }

    /**
     * Create a standard HTTP client for general API requests
     * Features:
     * - JSON content negotiation
     * - Standard timeout (60 seconds)
     */
    fun createStandardClient(): HttpClient {
        return HttpClient(CIO) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    prettyPrint = true
                    isLenient = true
                })
            }

            engine {
                requestTimeout = 60_000 // 1 minute
            }
        }
    }

    /**
     * Create a custom HTTP client with specific timeout
     * @param timeoutMillis Request timeout in milliseconds
     */
    fun createClientWithTimeout(timeoutMillis: Long): HttpClient {
        return HttpClient(CIO) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    prettyPrint = true
                    isLenient = true
                })
            }

            engine {
                requestTimeout = timeoutMillis
            }
        }
    }
}

