package org.dev.assistant.design_system.ui

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun Heading(text: String) {
    Text(text)
}

@Composable
fun LabelItem(text: String, modifier: Modifier = Modifier) {
    Text(text, modifier = modifier)
}