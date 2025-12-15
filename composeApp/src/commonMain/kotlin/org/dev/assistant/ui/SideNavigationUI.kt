package org.dev.assistant.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.PermanentDrawerSheet
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.dev.assistant.Res
import org.dev.assistant.app_heading
import org.dev.assistant.design_system.ui.Heading
import org.dev.assistant.design_system.ui.LabelItem
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

    PermanentNavigationDrawer(
        drawerContent = {
            PermanentDrawerSheet {
                Heading(text = stringResource(Res.string.app_heading))
                LabelItem(text = stringResource(Res.string.new_chat))
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