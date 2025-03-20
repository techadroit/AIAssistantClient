package org.dev.assistant.platform

interface UrlProvider {
    val wsUrl: String
}

expect fun getUrlProvider(): UrlProvider