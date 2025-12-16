package org.dev.assistant.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PermanentDrawerSheet
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.dev.assistant.Res
import org.dev.assistant.app_heading
import org.dev.assistant.design_system.themes.dimension
import org.dev.assistant.design_system.themes.navigationItemSpace
import org.dev.assistant.design_system.ui.Heading
import org.dev.assistant.design_system.ui.LabelItem
import org.dev.assistant.design_system.ui.SideNavigationDivider
import org.dev.assistant.design_system.ui.SideNavigationItem
import org.dev.assistant.new_chat
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview


@Composable
fun SideNavigationUI(
    onNavigateToChat: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToAbout: () -> Unit,
    content: @Composable () -> Unit
) {
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
                        SideNavigationItem {
                            Heading(text = stringResource(Res.string.app_heading))
                        }
                        Column {
                            SideNavigationDivider()
                            SideNavigationItem {
                                LabelItem(text = stringResource(Res.string.new_chat))
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

@Preview
@Composable
fun PreviewSideNavigationUI() {
    SideNavigationUI({}, {}, {}, {})
}