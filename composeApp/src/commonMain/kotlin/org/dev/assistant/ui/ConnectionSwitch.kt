package org.dev.assistant.ui

import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color

@Composable
fun ConnectionSwitch(
    isConnected: Boolean,
    onCheckedChange: ((Boolean) -> Unit)
) {
    var isChecked by remember { mutableStateOf(false) }
    Switch(
        checked = isChecked,
        onCheckedChange = {
            isChecked = it
            onCheckedChange(it)
        },
        colors = SwitchDefaults.colors(
            checkedThumbColor = if (isConnected) Color.Green else Color.Red,
            uncheckedThumbColor = Color.White
        )
    )
}