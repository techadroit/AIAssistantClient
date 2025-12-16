package org.dev.assistant.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.requiredWidthIn
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PermanentDrawerSheet
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
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

    PermanentNavigationDrawer(
        modifier = Modifier.defaultMinSize(minWidth = drawerMinWidth),
        drawerContent = {
            PermanentDrawerSheet {
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