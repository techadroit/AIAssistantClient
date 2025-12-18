package org.dev.assistant

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import org.dev.assistant.design_system.themes.HomeTheme
import org.dev.assistant.ui.ChatScreen
import org.dev.assistant.ui.home.HomeScreen
import org.dev.assistant.ui.home.SideNavigationUI
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MainApp()
}

@Composable
fun MainApp() {
    var currentScreen by remember { mutableStateOf<Screen>(Screen.Home) }
    var chatKey by remember { mutableStateOf(0) }

    HomeTheme {
        Scaffold { padding ->
            SideNavigationUI(
                onNavigateToHome = {
                    currentScreen = Screen.Home
                },
                onNavigateToChat = {
                    // Increment key to create a new chat instance
                    chatKey++
                    currentScreen = Screen.Chat
                },
                onNavigateToSettings = {},
                onNavigateToAbout = {}
            ) {
                when (currentScreen) {
                    Screen.Home -> HomeScreen(modifier = Modifier)
                    Screen.Chat -> {
                        key(chatKey) {
                            ChatScreen(modifier = Modifier)
                        }
                    }
                }
            }
        }
    }
}

sealed class Screen {
    object Home : Screen()
    object Chat : Screen()
}
