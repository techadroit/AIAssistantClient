package org.dev.assistant

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.dev.assistant.di.appModule
import org.dev.assistant.di.repositoryModule
import org.dev.assistant.di.serviceModule
import org.dev.assistant.di.viewmodelModule
import org.koin.core.context.startKoin

fun main() = application {
    // Initialize Koin
    startKoin {
        modules(
            appModule,
            repositoryModule,
            serviceModule,
            viewmodelModule
        )
    }

    Window(
        onCloseRequest = ::exitApplication,
        title = "AIAssistant",
    ) {
        App()
    }
}

