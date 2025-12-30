package org.dev.assistant.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

/**
 * Platform-specific DataStore creator
 * Each platform will provide its own implementation
 */
expect fun createDataStore(): DataStore<Preferences>

