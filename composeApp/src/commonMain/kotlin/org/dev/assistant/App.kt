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
import org.dev.assistant.ui.SideNavigationUI
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MainApp()
}

@Composable
fun MainApp() {
    var chatKey by remember { mutableStateOf(0) }

    HomeTheme {
        Scaffold { padding ->
            SideNavigationUI(
                onNavigateToChat = {
                    // Increment key to create a new chat instance
                    chatKey++
                },
                onNavigateToSettings = {},
                onNavigateToAbout = {}
            ) {
                key(chatKey) {
                    ChatScreen(modifier = Modifier)
                }
            }
        }
    }
}
