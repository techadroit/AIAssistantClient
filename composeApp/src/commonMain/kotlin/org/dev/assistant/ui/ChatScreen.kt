package org.dev.assistant.ui

//import coil3.compose.AsyncImage
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Card
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TopAppBar
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
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.dev.assistant.data.Product
import org.dev.assistant.themes.getChatBackgroundColor
import org.dev.assistant.themes.getPriceColor
import org.dev.assistant.themes.productCartBackground
import org.dev.assistant.ui.pojo.Message
import org.dev.assistant.ui.pojo.ReceiveMessage
import org.dev.assistant.ui.pojo.SentMessage
import org.dev.assistant.util.UrlImage
import org.dev.assistant.util.edgeShadow


@Composable
fun ChatScreen() {
    val viewmodel = ChatViewModel()
    val state = viewmodel.messages.collectAsState()
    val isConnected = viewmodel.isConnected.collectAsState()

    Surface {
        Column {
            ChatToolbar(
                isConnected = isConnected.value,
                onSettingsClick = { viewmodel.showSettingsMenu() },
                onUrlChange = { viewmodel.updateUrl(it) },
                onSwitchChange = {
                    if (it)
                        viewmodel.connect()
                    else
                        viewmodel.disconnect()
                },
                onRefresh = {
                    viewmodel.refresh()
                }
            )
            ChatContainer(state.value) {
                viewmodel.sendMessage(it)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatToolbar(
    isConnected: Boolean,
    onSettingsClick: () -> Unit,
    onUrlChange: (String) -> Unit,
    onSwitchChange: (Boolean) -> Unit,
    onRefresh: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    var url by remember { mutableStateOf("") }

    fun hideDialog() {
        expanded = false
        showDialog = false
    }

    TopAppBar(
        title = { Text("Chat") },
        actions = {
            ConnectionSwitch(
                isConnected = isConnected,
                onCheckedChange = {
                    onSwitchChange(it)
                } // Read-only switch
            )
            IconButton(onClick = { onRefresh() }) {
                Icon(Icons.Default.Refresh, contentDescription = "Refresh")
            }
            IconButton(onClick = { expanded = true }) {
                Icon(Icons.Default.MoreVert, contentDescription = "Settings")
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                DropdownMenuItem(text = { Text("Add URL") }, onClick = {
                    expanded = false
                    showDialog = true
//                    onSettingsClick()
                })
            }
        },
        modifier = Modifier.edgeShadow()
    )

    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                hideDialog()
            },
            title = { Text("Enter WebSocket URL") },
            text = {
                TextField(
                    value = url,
                    onValueChange = { url = it },
                    label = { Text("URL") }
                )
            },
            confirmButton = {
                Button(onClick = {
                    onUrlChange(url)
                    hideDialog()
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                Button(onClick = {
                    hideDialog()
                }) {
                    Text("Cancel")
                }
            }
        )
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

    fun resetText() {
        text = ""
    }

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ChatInput(
            modifier = Modifier.weight(1f),
            text = text,
            onSend = {
                send(text)
                resetText()
            }
        ) {
            text = it
        }
        ChatSubmitButton(
            modifier = Modifier.padding(end = 16.dp)
        ) {
            send(text)
            resetText()
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
    onSend: () -> Unit = {},
    onTextChange: (String) -> Unit,
) {
    var isFocused by remember { mutableStateOf(false) }

    Surface(
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, Color.Gray),
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth()
            .defaultMinSize(minHeight = OutlinedTextFieldDefaults.MinHeight)
    ) {
        Box {
            if (text.isEmpty() && !isFocused) {
                Text(
                    "Enter text",
                    color = Color.Gray,
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 16.dp)
                )
            }
            BasicTextField(
                value = text,
                onValueChange = onTextChange,
                modifier = Modifier
                    .padding(16.dp)
                    .matchParentSize()
                    .onPreviewKeyEvent { keyEvent ->
                        if (keyEvent.type == androidx.compose.ui.input.key.KeyEventType.KeyDown &&
                            (keyEvent.key == androidx.compose.ui.input.key.Key.Enter ||
                                    keyEvent.key == androidx.compose.ui.input.key.Key.NumPadEnter)
                        ) {
                            onSend()
                            true
                        } else {
                            false
                        }
                    }
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
        ChatMessage(message)
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
        ChatMessage(message)
    }
}

@Composable
fun ChatMessage(message: Message, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .background(
                MaterialTheme.colorScheme.getChatBackgroundColor(),
                shape = RoundedCornerShape(8.dp)
            )
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            CommonText(message.msg)
            Divider8()
        }
    }
}

@Composable
fun PreviewChatMessage() {
    val sampleProduct = Product(
        imageUrl = "https://via.placeholder.com/150",
        name = "Sample Product",
        price = 19.99
    )
    val receiveMessage = ReceiveMessage(
        msg = "Here are some products:"
    )
    val sentMessage = SentMessage(
        msg = "Thank you!"
    )
    Column {
        ChatMessage(message = receiveMessage)
        Spacer(modifier = Modifier.size(16.dp))
        ChatMessage(message = sentMessage)
    }
}

@Composable
fun CommonText(message: String, color: Color = Color.Black, fontSize: TextUnit = 18.sp) {
    Text(
        text = message,
        style = MaterialTheme.typography.bodyLarge.copy(color = color, fontSize = fontSize)
    )
}

@Composable
fun CommonTextBold(message: String, color: Color = Color.Black, fontSize: TextUnit = 16.sp) {
    Text(
        text = message,
        style = MaterialTheme.typography.bodyLarge.copy(
            color = color,
            fontWeight = FontWeight.Bold,
            fontSize = fontSize
        )
    )
}

@Composable
fun Divider8(modifier: Modifier = Modifier) {
    Spacer(modifier.size(8.dp))
}

@Composable
fun Divider16(modifier: Modifier = Modifier) {
    Spacer(modifier.size(16.dp))
}

@Composable
fun PaddingBox(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .padding(8.dp)
    ) {
        content()
    }
}

