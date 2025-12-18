package org.dev.assistant.di

import org.koin.dsl.module
import org.koin.core.module.dsl.viewModelOf
import org.dev.assistant.ui.ChatViewModel

val appModule = module {
    // Add your dependencies here
    // Example: single { SomeRepository() }
}

val viewmodelModule = module {
    viewModelOf(::ChatViewModel)
}


