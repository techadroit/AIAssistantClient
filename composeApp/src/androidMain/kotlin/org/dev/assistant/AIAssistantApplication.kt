package org.dev.assistant

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.dev.assistant.di.appModule
import org.dev.assistant.di.viewmodelModule

class AIAssistantApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@AIAssistantApplication)
            modules(appModule, viewmodelModule)
        }
    }
}

