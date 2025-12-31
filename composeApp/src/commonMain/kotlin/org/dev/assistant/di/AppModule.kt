package org.dev.assistant.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import org.dev.assistant.data.ChatMessageRepository
import org.dev.assistant.data.ChatSessionRepository
import org.dev.assistant.data.SessionManager
import org.dev.assistant.data.UserLocalRepository
import org.dev.assistant.data.UserRepository
import org.dev.assistant.data.WebSocketClient
import org.dev.assistant.data.datastore.DataStoreManager
import org.dev.assistant.data.datastore.createDataStore
import org.dev.assistant.domain.ChatMessageService
import org.dev.assistant.domain.ChatSessionService
import org.dev.assistant.domain.UserService
import org.dev.assistant.network.NetworkClient
import org.dev.assistant.ui.chat.ChatSessionHandler
import org.dev.assistant.ui.chat.ChatViewModel
import org.dev.assistant.ui.home.HomeViewModel
import org.dev.assistant.ui.main.MainViewModel
import org.dev.assistant.ui.registration.RegistrationViewModel
import org.dev.assistant.ui.settings.SettingsViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

private var apiBaseUrl = "http://127.0.0.1:8001"

val appModule = module {
    // Add your dependencies here
    // Example: single { SomeRepository() }
    single { NetworkClient(baseUrl = apiBaseUrl) }
    // DataStore - with explicit type parameters
    single<DataStore<Preferences>> { createDataStore() }
    single<DataStoreManager> { DataStoreManager(get()) }
    single { ChatSessionHandler() }
    single { SessionManager(get()) }
    single { WebSocketClient(get()) }
}

val repositoryModule = module {
    single { UserLocalRepository(get()) }
    single { UserRepository(get()) }
    single { ChatSessionRepository(get()) }
    single { ChatMessageRepository(get()) }
}

val serviceModule = module {
    single { UserService(get(), get()) }
    single { ChatSessionService(get(), get(), get()) }
    single { ChatMessageService(get()) }
}

val viewmodelModule = module {
    viewModelOf(::ChatViewModel)
    viewModelOf(::HomeViewModel)
    viewModelOf(::MainViewModel)
    viewModelOf(::RegistrationViewModel)
    viewModelOf(::SettingsViewModel)
}


