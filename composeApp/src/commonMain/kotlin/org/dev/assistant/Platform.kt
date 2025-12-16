package org.dev.assistant

enum class PlatformType {
    ANDROID,
    DESKTOP,
    IOS,
    BROWSER
}

interface Platform {
    val name: String
    val type: PlatformType
}

expect fun getPlatform(): Platform

fun currentPlatformType(): PlatformType = getPlatform().type
