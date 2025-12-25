package org.dev.assistant.ui.registration

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * Example usage of RegistrationScreen
 *
 * You can integrate this in your navigation flow.
 *
 * Add Registration to your Screen sealed class:
 * sealed class Screen {
 *     object Home : Screen()
 *     object Chat : Screen()
 *     object Registration : Screen()
 * }
 *
 * Then in your navigation when clause:
 * when (currentScreen) {
 *     Screen.Registration -> {
 *         RegistrationScreen(
 *             onRegistrationSuccess = {
 *                 currentScreen = Screen.Home
 *             },
 *             onNavigateToLogin = {
 *                 currentScreen = Screen.Login
 *             }
 *         )
 *     }
 *     // ... other screens
 * }
 */
@Composable
fun RegistrationScreenExample() {
    RegistrationScreen(
        modifier = Modifier,
        onRegistrationSuccess = {
            // Handle successful registration
            println("Registration successful!")
        },
        onNavigateToLogin = {
            // Navigate to login
            println("Navigate to login")
        }
    )
}

