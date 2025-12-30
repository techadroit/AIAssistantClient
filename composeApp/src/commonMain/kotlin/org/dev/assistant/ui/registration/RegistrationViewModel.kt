package org.dev.assistant.ui.registration

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.dev.assistant.base.BaseViewModel

class RegistrationViewModel : BaseViewModel() {

    private val _state = MutableStateFlow(RegistrationState())
    val state = _state.asStateFlow()

    fun onUsernameChange(username: String) {
        _state.value = _state.value.copy(
            username = username,
            usernameError = null
        )
    }

    fun onPasswordChange(password: String) {
        _state.value = _state.value.copy(
            password = password,
            passwordError = null
        )
    }

    fun onConfirmPasswordChange(confirmPassword: String) {
        _state.value = _state.value.copy(
            confirmPassword = confirmPassword,
            confirmPasswordError = null
        )
    }

    fun onRegister() {
        if (!validateInputs()) {
            return
        }

        _state.value = _state.value.copy(isLoading = true)

        scope.launch {
            try {
                // TODO: Implement actual registration logic with your repository
                // Example: userRepository.register(username, password)

                // Simulate registration
                kotlinx.coroutines.delay(1000)

                _state.value = _state.value.copy(
                    isLoading = false,
                    registrationSuccess = true,
                    errorMessage = null
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    registrationSuccess = false,
                    errorMessage = e.message ?: "Registration failed"
                )
            }
        }
    }

    private fun validateInputs(): Boolean {
        val currentState = _state.value
        var isValid = true

        if (currentState.username.isBlank()) {
            _state.value = _state.value.copy(usernameError = "Username is required")
            isValid = false
        } else if (currentState.username.length < 3) {
            _state.value = _state.value.copy(usernameError = "Username must be at least 3 characters")
            isValid = false
        }

        if (currentState.password.isBlank()) {
            _state.value = _state.value.copy(passwordError = "Password is required")
            isValid = false
        } else if (currentState.password.length < 6) {
            _state.value = _state.value.copy(passwordError = "Password must be at least 6 characters")
            isValid = false
        }

        if (currentState.confirmPassword.isBlank()) {
            _state.value = _state.value.copy(confirmPasswordError = "Please confirm your password")
            isValid = false
        } else if (currentState.password != currentState.confirmPassword) {
            _state.value = _state.value.copy(confirmPasswordError = "Passwords do not match")
            isValid = false
        }

        return isValid
    }

    fun clearError() {
        _state.value = _state.value.copy(errorMessage = null)
    }
}

data class RegistrationState(
    val username: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val usernameError: String? = null,
    val passwordError: String? = null,
    val confirmPasswordError: String? = null,
    val isLoading: Boolean = false,
    val registrationSuccess: Boolean = false,
    val errorMessage: String? = null
)

