package org.dev.assistant.util

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun Modifier.edgeShadow(elevation: Dp = 8.dp) = this.then(
    Modifier
        .shadow(
            elevation = elevation,
            shape = RoundedCornerShape(0.dp),
            clip = false
        )
)