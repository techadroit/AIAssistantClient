package org.dev.assistant.platform

actual fun getUrlProvider(): UrlProvider {
    return DesktopUrlProvider()
}

class DesktopUrlProvider : UrlProvider {
    override val wsUrl: String
        get() = "ws://127.0.0.1:8000/ws"
}