package org.dev.assistant

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
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
    HomeTheme {
        Scaffold { padding ->
            Row(modifier = Modifier.fillMaxSize().padding(padding)) {
                SideNavigationUI({}, {}, {})
                ChatScreen(modifier = Modifier.weight(1f))
            }
        }
    }
}
