package org.dev.assistant.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

/**
 * WebAssembly/JS stub implementation
 * DataStore is not yet supported on WebAssembly target
 *
 * For now, this returns a placeholder. Consider using:
 * - Browser localStorage API
 * - IndexedDB
 * - In-memory storage
 */
actual fun createDataStore(): DataStore<Preferences> {
    throw UnsupportedOperationException(
        "DataStore is not yet supported on WebAssembly. " +
        "Please use browser localStorage or IndexedDB instead."
    )
}

