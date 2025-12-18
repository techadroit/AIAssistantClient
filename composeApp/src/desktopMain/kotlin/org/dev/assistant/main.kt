package org.dev.assistant

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.koin.core.context.startKoin
import org.dev.assistant.di.appModule
import org.dev.assistant.di.viewmodelModule

fun main() = application {
    // Initialize Koin
    startKoin {
        modules(appModule, viewmodelModule)
    }

    Window(
        onCloseRequest = ::exitApplication,
        title = "AIAssistant",
    ) {
        App()
    }
}

