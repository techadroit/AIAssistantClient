package org.dev.assistant

import androidx.compose.runtime.Composable
import org.dev.assistant.themes.HomeTheme
import org.dev.assistant.ui.ChatScreen
import org.dev.assistant.ui.PreviewChatMessage
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    HomeTheme {
        ChatScreen()
//        PreviewChatMessage()
    }
}
