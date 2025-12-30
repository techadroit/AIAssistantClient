package org.dev.assistant

import androidx.compose.ui.window.ComposeUIViewController
import org.koin.core.context.startKoin
import org.dev.assistant.di.appModule
import org.dev.assistant.di.viewmodelModule

fun MainViewController() = ComposeUIViewController {
    startKoin {
        modules(appModule, viewmodelModule)
    }
    App()
}

