package org.dev.assistant.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PermanentDrawerSheet
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import org.dev.assistant.Res
import org.dev.assistant.app_heading
import org.dev.assistant.data.model.ChatSessionResponse
import org.dev.assistant.design_system.themes.dimension
import org.dev.assistant.design_system.themes.navigationItemSpace
import org.dev.assistant.design_system.ui.Heading
import org.dev.assistant.design_system.ui.LabelItem
import org.dev.assistant.design_system.ui.SideNavigationDivider
import org.dev.assistant.design_system.ui.SideNavigationItem
import org.dev.assistant.home
import org.dev.assistant.new_chat
import org.dev.assistant.ui.main.MainViewModel
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel


@Composable
fun SideNavigationUI(
    onNavigateToHome: () -> Unit,
    onNavigateToChat: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToAbout: () -> Unit,
    onChatSessionClick: (String) -> Unit,
    content: @Composable () -> Unit
) {
    val mainViewModel: MainViewModel = koinViewModel()
    val chatSessions by mainViewModel.chatSessions.collectAsState()

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Open)
    val drawerMinWidth = MaterialTheme.dimension().drawerMinWidth
    val dividerColor = MaterialTheme.colorScheme.outlineVariant
    val bgColor = MaterialTheme.colorScheme.surfaceVariant

    PermanentNavigationDrawer(
        drawerContent = {
            Box(
                modifier = Modifier.sizeIn(maxWidth = drawerMinWidth).background(bgColor)
            ) {
                PermanentDrawerSheet(
                    modifier = Modifier.fillMaxSize().padding(16.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxHeight(),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            SideNavigationItem {
                                Heading(text = stringResource(Res.string.app_heading))
                            }
                            SideNavigationItem(onClick = onNavigateToHome) {
                                LabelItem(text = stringResource(Res.string.home))
                            }

                            Column {
                                SideNavigationDivider()

                                // Chat Sessions List
                                LazyColumn(
                                    modifier = Modifier.fillMaxWidth().weight(1f, fill = false)
                                ) {
                                    items(chatSessions) { session ->
                                        ChatSessionItem(
                                            session = session,
                                            onClick = { onChatSessionClick(session.chatSessionId) }
                                        )
                                    }
                                }

                                // New Chat Button
                                SideNavigationItem(onClick = onNavigateToChat) {
                                    LabelItem(text = stringResource(Res.string.new_chat))
                                }
                            }
                        }
                        Spacer(modifier = Modifier.navigationItemSpace())
                    }
                }
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(1.dp)
                        .align(Alignment.CenterEnd)
                        .background(dividerColor)
                )
            }
        }
    ) {
        content()
    }
}

@Composable
fun ChatSessionItem(
    session: ChatSessionResponse,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Text(
            text = session.title ?: "Untitled Chat",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Preview
@Composable
fun PreviewSideNavigationUI() {
    SideNavigationUI({}, {}, {}, {}, {}, {})
}

