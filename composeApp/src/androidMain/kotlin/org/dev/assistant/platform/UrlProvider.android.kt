package org.dev.assistant.platform

actual fun getUrlProvider(): UrlProvider {
    return AndroidUrlProvider()
}

class AndroidUrlProvider : UrlProvider {
    override val wsUrl: String
        get() = "ws://10.0.2.2:8000/ws"
}