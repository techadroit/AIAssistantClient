package org.dev.assistant

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "AIAssistant",
    ) {
        App()
    }
}