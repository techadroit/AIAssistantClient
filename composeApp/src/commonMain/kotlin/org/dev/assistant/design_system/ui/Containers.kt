package org.dev.assistant.design_system.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.dev.assistant.design_system.themes.navigationItemSpace

@Composable
fun SideNavigationItem(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    Box(modifier = modifier
        .navigationItemSpace()
        .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier)
    ) {
        content()
    }

}