package org.dev.assistant.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import okio.Path.Companion.toPath
import java.io.File

/**
 * Desktop (JVM) implementation of DataStore
 */
actual fun createDataStore(): DataStore<Preferences> {
    return PreferenceDataStoreFactory.createWithPath(
        produceFile = {
            val userHome = System.getProperty("user.home")
            val appDataDir = File(userHome, ".ai_client")
            if (!appDataDir.exists()) {
                appDataDir.mkdirs()
            }
            File(appDataDir, "preferences.preferences_pb").absolutePath.toPath()
        }
    )
}

