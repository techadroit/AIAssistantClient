package org.dev.assistant

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import org.dev.assistant.themes.HomeTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    HomeTheme {
        ChatScreen()
    }
}

@Composable
fun ChatScreen() {
    Surface {
        ChatContainer()
    }
}

@Composable
fun ChatContainer() {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        ChatFooter(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .wrapContentHeight()
        )
    }
}

@Composable
fun ChatFooter(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ChatInput(
            modifier = Modifier.weight(1f)
        )
        ChatSubmitButton(
            modifier = Modifier.padding(end = 16.dp)
        )
    }
}

@Composable
fun ChatSubmitButton(modifier: Modifier = Modifier) {
//    Button(
//        onClick = { /* Handle send action */ },
//        shape = RoundedCornerShape(50),
//        modifier = modifier.size(48.dp)
//    ) {
//        Icon(Icons.Filled.Done, contentDescription = "Send", tint = Color.White)
//    }
    IconButton(
        onClick = { /* Handle send action */ },
        modifier = modifier.size(48.dp)
    ) {
        Icon(Icons.Filled.Send, contentDescription = "Send",)
    }
}

@Composable
fun ChatInput(
    modifier: Modifier = Modifier,
    textStyle: TextStyle = LocalTextStyle.current,
    colors: TextFieldColors = OutlinedTextFieldDefaults.colors()
) {
    var text by remember { mutableStateOf(TextFieldValue("")) }
    var isFocused by remember { mutableStateOf(false) }

    Surface(
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, Color.Gray),
        modifier = modifier.padding(16.dp)
            .fillMaxWidth()
            .defaultMinSize(
                minHeight = OutlinedTextFieldDefaults.MinHeight
            )
    ) {
        Box(
//            modifier = Modifier.padding(8.dp).fillMaxWidth()

        ) {
            if (text.text.isEmpty() && !isFocused) {
                Text(
                    "Enter text",
                    color = Color.Gray,
                    modifier = Modifier.align(Alignment.CenterStart).padding(start = 16.dp)
                )
            }
            BasicTextField(
                value = text,
                onValueChange = { text = it },
                modifier = Modifier
                    .padding(16.dp)
                    .matchParentSize()
                    .onFocusChanged { focusState ->
                        isFocused = focusState.isFocused
                    }
            )
        }
    }
}