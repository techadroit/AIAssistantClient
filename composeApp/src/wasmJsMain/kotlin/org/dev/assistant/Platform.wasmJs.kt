package org.dev.assistant

import org.dev.assistant.platform.Platform
import org.dev.assistant.platform.PlatformType

class WasmPlatform: Platform {
    override val name: String = "Web with Kotlin/Wasm"
    override val type: PlatformType = PlatformType.BROWSER
}

actual fun getPlatform(): Platform = WasmPlatform()