package org.dev.assistant

class JVMPlatform: Platform {
    override val name: String = "Java ${System.getProperty("java.version")}"
    override val type: PlatformType = PlatformType.DESKTOP
}

actual fun getPlatform(): Platform = JVMPlatform()