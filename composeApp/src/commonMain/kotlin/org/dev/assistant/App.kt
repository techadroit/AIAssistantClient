package org.dev.assistant

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import org.dev.assistant.design_system.themes.HomeTheme
import org.dev.assistant.navigation.AppNavHost
import org.dev.assistant.navigation.Screen
import org.dev.assistant.ui.home.SideNavigationUI
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MainApp()
}

@Composable
fun MainApp() {
    val navController = rememberNavController()

    HomeTheme {
        Scaffold { padding ->
            SideNavigationUI(
                onNavigateToHome = {
                    navController.navigate(Screen.Home) {
                        popUpTo(Screen.Home) { inclusive = true }
                    }
                },
                onNavigateToChat = {
                    navController.navigate(Screen.Chat())
                },
                onNavigateToSettings = {
                    navController.navigate(Screen.Settings) {
                        launchSingleTop = true
                    }
                },
                onNavigateToAbout = {},
                onChatSessionClick = { sessionId ->
                    navController.navigate(Screen.Chat(sessionId = sessionId))
                }
            ) {
                AppNavHost(
                    navController = navController,
                    modifier = Modifier
                )
            }
        }
    }
}

