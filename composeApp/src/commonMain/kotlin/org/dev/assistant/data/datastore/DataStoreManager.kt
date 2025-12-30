package org.dev.assistant.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * DataStore manager for storing app preferences
 *
 * Usage example:
 * ```
 * val dataStoreManager = DataStoreManager(createDataStore())
 *
 * // Save data
 * dataStoreManager.saveString("user_id", "12345")
 *
 * // Read data
 * val userId: Flow<String?> = dataStoreManager.getString("user_id")
 * ```
 */
class DataStoreManager(private val dataStore: DataStore<Preferences>) {

    companion object {
        // Predefined keys for common app preferences
        val USER_ID = "user_id"
        val USERNAME = stringPreferencesKey("username")
        val AUTH_TOKEN = stringPreferencesKey("auth_token")
        val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
        val THEME_MODE = stringPreferencesKey("theme_mode")
        val API_BASE_URL = stringPreferencesKey("api_base_url")
        val WEBSOCKET_URL = stringPreferencesKey("websocket_url")
        val LAST_SESSION_ID = stringPreferencesKey("last_session_id")
    }

    // String operations
    suspend fun saveString(key: String, value: String) {
        val prefKey = stringPreferencesKey(key)
        dataStore.edit { preferences ->
            preferences[prefKey] = value
        }
    }

    fun getString(key: String, defaultValue: String? = null): Flow<String?> {
        val prefKey = stringPreferencesKey(key)
        return dataStore.data.map { preferences ->
            preferences[prefKey] ?: defaultValue
        }
    }

    // Boolean operations
    suspend fun saveBoolean(key: String, value: Boolean) {
        val prefKey = booleanPreferencesKey(key)
        dataStore.edit { preferences ->
            preferences[prefKey] = value
        }
    }

    fun getBoolean(key: String, defaultValue: Boolean = false): Flow<Boolean> {
        val prefKey = booleanPreferencesKey(key)
        return dataStore.data.map { preferences ->
            preferences[prefKey] ?: defaultValue
        }
    }

    // Int operations
    suspend fun saveInt(key: String, value: Int) {
        val prefKey = intPreferencesKey(key)
        dataStore.edit { preferences ->
            preferences[prefKey] = value
        }
    }

    fun getInt(key: String, defaultValue: Int = 0): Flow<Int> {
        val prefKey = intPreferencesKey(key)
        return dataStore.data.map { preferences ->
            preferences[prefKey] ?: defaultValue
        }
    }

    // Generic operations with Preferences.Key
    suspend fun <T> save(key: Preferences.Key<T>, value: T) {
        dataStore.edit { preferences ->
            preferences[key] = value
        }
    }

    fun <T> get(key: Preferences.Key<T>, defaultValue: T): Flow<T> {
        return dataStore.data.map { preferences ->
            preferences[key] ?: defaultValue
        }
    }

    fun <T> get(key: Preferences.Key<T>): Flow<T?> {
        return dataStore.data.map { preferences ->
            preferences[key]
        }
    }

    // Remove a specific key
    suspend fun remove(key: String) {
        val stringKey = stringPreferencesKey(key)
        dataStore.edit { preferences ->
            preferences.remove(stringKey)
        }
    }

    suspend fun <T> remove(key: Preferences.Key<T>) {
        dataStore.edit { preferences ->
            preferences.remove(key)
        }
    }

    // Clear all preferences
    suspend fun clear() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

}

