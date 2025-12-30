package org.dev.assistant.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import okio.Path.Companion.toPath

/**
 * Android implementation of DataStore
 * Requires Context to be provided
 */
private lateinit var appContext: Context

fun initDataStore(context: Context) {
    appContext = context.applicationContext
}

actual fun createDataStore(): DataStore<Preferences> {
    return PreferenceDataStoreFactory.createWithPath(
        produceFile = {
            appContext.filesDir.resolve("preferences.preferences_pb").absolutePath.toPath()
        }
    )
}

