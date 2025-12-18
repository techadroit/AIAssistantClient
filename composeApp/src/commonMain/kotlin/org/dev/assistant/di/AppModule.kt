package org.dev.assistant.di

import org.dev.assistant.data.ChatSessionRepository
import org.dev.assistant.data.UserRepository
import org.dev.assistant.domain.ChatSessionService
import org.dev.assistant.domain.UserService
import org.dev.assistant.network.NetworkClient
import org.dev.assistant.ui.chat.ChatViewModel
import org.dev.assistant.ui.home.HomeViewModel
import org.dev.assistant.ui.main.MainViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

private var apiBaseUrl = "127.0.0.1:8001"

val appModule = module {
    // Add your dependencies here
    // Example: single { SomeRepository() }
    single { NetworkClient(baseUrl = apiBaseUrl) }
}

val repositoryModule = module {
    single { UserRepository(get()) }
    single { ChatSessionRepository(get()) }
}

val serviceModule = module {
    single { UserService() }
    single { ChatSessionService(get(), get()) }
}

val viewmodelModule = module {
    viewModelOf(::ChatViewModel)
    viewModelOf(::HomeViewModel)
    viewModelOf(::MainViewModel)
}


