package org.dev.assistant.data

import kotlinx.coroutines.flow.firstOrNull
import org.dev.assistant.data.datastore.DataStoreManager

class UserLocalRepository(val dataStoreManager: DataStoreManager) {

    suspend fun getUserId(): Result<String?> {
        return runCatching {
            dataStoreManager.getString(DataStoreManager.USER_ID).firstOrNull()
        }
    }

    suspend fun saveUserId(userId: String): Result<Unit> {
        return runCatching {
            dataStoreManager.saveString(DataStoreManager.USER_ID, userId)
        }
    }
}