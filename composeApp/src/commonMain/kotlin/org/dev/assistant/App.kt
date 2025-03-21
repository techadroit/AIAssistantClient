package org.dev.assistant

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import org.dev.assistant.themes.HomeTheme
import org.dev.assistant.ui.ChatViewModel
import org.dev.assistant.ui.Message
import org.dev.assistant.ui.ReceiveMessage
import org.dev.assistant.ui.SentMessage
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
    val viewmodel = ChatViewModel()
    val state = viewmodel.messages.collectAsState()
    Surface {
        ChatContainer(state.value) {
            viewmodel.sendMessage(it)
        }
    }
}

@Composable
fun ChatContainer(list: List<Message>, send: (String) -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        ChatList(
            messages = list,
            modifier = Modifier.weight(1f)
        )
        ChatFooter(
            modifier = Modifier.wrapContentHeight()
        ) {
            if (it.isNotBlank() || it.isNotEmpty())
                send(it)
        }
    }
}

@Composable
fun ChatFooter(modifier: Modifier = Modifier, send: (String) -> Unit) {
    var text by remember { mutableStateOf("") }
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ChatInput(
            modifier = Modifier.weight(1f),
            text = text
        ) {
            text = it
        }
        ChatSubmitButton(
            modifier = Modifier.padding(end = 16.dp)
        ) {
            send(text)
            text = ""
        }
    }
}

@Composable
fun ChatSubmitButton(modifier: Modifier = Modifier, onClick: () -> Unit) {
    IconButton(
        onClick = { onClick() },
        modifier = modifier.size(48.dp)
    ) {
        Icon(Icons.Filled.Send, contentDescription = "Send")
    }
}

@Composable
fun ChatInput(
    modifier: Modifier = Modifier,
    textStyle: TextStyle = LocalTextStyle.current,
    colors: TextFieldColors = OutlinedTextFieldDefaults.colors(),
    text: String,
    onTextChange: (String) -> Unit
) {
//    var text by remember { mutableStateOf(TextFieldValue("")) }
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
        Box {
            if (text.isEmpty() && !isFocused) {
                Text(
                    "Enter text",
                    color = Color.Gray,
                    modifier = Modifier.align(Alignment.CenterStart).padding(start = 16.dp)
                )
            }
            BasicTextField(
                value = text,
                onValueChange = {
//                    text = it
                    onTextChange(it)
                },
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

@Composable
fun ChatList(messages: List<Message>, modifier: Modifier) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(messages) { it ->
            when (it) {
                is SentMessage -> SentMessageItem(it)
                is ReceiveMessage -> ReceiveMessageItem(it)
            }

        }
    }
}

@Composable
fun SentMessageItem(message: SentMessage) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.End
    ) {
        ChatMessage(message.msg)
    }
}

@Composable
fun ReceiveMessageItem(message: ReceiveMessage) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.Start
    ) {
        ChatMessage(message.msg)
    }
}

@Composable
fun ChatMessage(message: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .background(
                MaterialTheme.colorScheme.onSurface,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(8.dp)
    ) {
        Text(
            text = message,
            color = Color.Black
        )
    }
}