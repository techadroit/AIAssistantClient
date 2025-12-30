package org.dev.assistant.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import org.dev.assistant.ui.ChatScreen
import org.dev.assistant.ui.home.HomeScreen
import org.dev.assistant.ui.settings.SettingsScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home,
        modifier = modifier
    ) {
        composable<Screen.Home> {
            HomeScreen(modifier = Modifier)
        }

        composable<Screen.Chat> { backStackEntry ->
            val chat: Screen.Chat = backStackEntry.toRoute()
            ChatScreen(
                modifier = Modifier,
                chatSessionId = chat.sessionId
            )
        }

        composable<Screen.Settings> {
            SettingsScreen(modifier = Modifier)
        }
    }
}

