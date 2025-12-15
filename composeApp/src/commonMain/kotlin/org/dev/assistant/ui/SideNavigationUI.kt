package org.dev.assistant.ui

import androidx.compose.material3.NavigationRail
import androidx.compose.runtime.Composable
import org.dev.assistant.design_system.ui.Heading

@Composable
fun SideNavigationUI(
    onNavigateToChat: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToAbout: () -> Unit
) {
    NavigationRail {
        NavigationRail {
            Heading(text = "Assistant")
        }
    }
}