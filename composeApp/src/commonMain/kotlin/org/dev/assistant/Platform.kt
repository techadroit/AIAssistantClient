package org.dev.assistant

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform