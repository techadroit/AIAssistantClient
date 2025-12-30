package org.dev.assistant.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.dev.assistant.domain.UserService

class SettingsViewModel(
    private val userService: UserService
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _clearCacheSuccess = MutableStateFlow(false)
    val clearCacheSuccess: StateFlow<Boolean> = _clearCacheSuccess.asStateFlow()

    fun clearUserData() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                _clearCacheSuccess.value = false

                // Clear local user data
                userService.clearUserData()

                _clearCacheSuccess.value = true

            } catch (e: Exception) {
                _error.value = "Failed to clear cache: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun resetSuccessState() {
        _clearCacheSuccess.value = false
    }
}

