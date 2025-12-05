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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import kotlinx.coroutines.launch
import org.dev.assistant.data.Product
import org.dev.assistant.themes.getChatBackgroundColor
import org.dev.assistant.ui.pojo.ChatModeType
import org.dev.assistant.ui.pojo.Message
import org.dev.assistant.ui.pojo.ReceiveMessage
import org.dev.assistant.ui.pojo.SentMessage
import org.dev.assistant.util.UploadState
import org.dev.assistant.util.edgeShadow
import org.dev.assistant.util.getFilePicker

// String Constants
const val CHAT = "chat"
const val ADD_URL = "Add URL"
const val ENTER_WEBSOCKET_URL = "Enter WebSocket URL"
const val URL_LABEL = "URL"
const val OK_BUTTON = "OK"
const val CANCEL_BUTTON = "Cancel"
const val REFRESH_CONTENT_DESC = "Refresh"
const val SETTINGS_CONTENT_DESC = "Settings"
const val SEND_CONTENT_DESC = "Send"
const val ATTACH_FILE_CONTENT_DESC = "Attach file"
const val ENTER_TEXT_PLACEHOLDER = "Enter text"
const val UPLOADING_TEXT = "Uploading... "
const val UPLOADED_FILE_PREFIX = "📎 Uploaded file: "
const val SAMPLE_PRODUCT_NAME = "Sample Product"
const val SAMPLE_PRODUCTS_MESSAGE = "Here are some products:"
const val THANK_YOU_MESSAGE = "Thank you!"
const val SELECTED_FILE_LOG = "Selected file: "
const val SIZE_BYTES_SUFFIX = " bytes, Type: "
const val AGENT_MODE_LABEL = "Agent Mode"
const val CHAT_MODE_LABEL = "Chat Mode"
const val SELECT_CHAT_MODE = "Select mode"

@Composable
fun ChatScreen() {
    val viewmodel = ChatViewModel()
    val state = viewmodel.messages.collectAsState()
    val isConnected = viewmodel.isConnected.collectAsState()
    val uploadState = viewmodel.uploadState.collectAsState()
    val chatMode = viewmodel.chatMode.collectAsState()

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
            ChatContainer(
                viewModel = viewmodel,
                messages = state.value,
                uploadState = uploadState.value,
                chatMode = chatMode.value
            ) {
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
        title = { Text(CHAT) },
        actions = {
            ConnectionSwitch(
                isConnected = isConnected,
                onCheckedChange = {
                    onSwitchChange(it)
                } // Read-only switch
            )
            IconButton(onClick = { onRefresh() }) {
                Icon(Icons.Default.Refresh, contentDescription = REFRESH_CONTENT_DESC)
            }
            IconButton(onClick = { expanded = true }) {
                Icon(Icons.Default.MoreVert, contentDescription = SETTINGS_CONTENT_DESC)
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                DropdownMenuItem(text = { Text(ADD_URL) }, onClick = {
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
            title = { Text(ENTER_WEBSOCKET_URL) },
            text = {
                TextField(
                    value = url,
                    onValueChange = { url = it },
                    label = { Text(URL_LABEL) }
                )
            },
            confirmButton = {
                Button(onClick = {
                    onUrlChange(url)
                    hideDialog()
                }) {
                    Text(OK_BUTTON)
                }
            },
            dismissButton = {
                Button(onClick = {
                    hideDialog()
                }) {
                    Text(CANCEL_BUTTON)
                }
            }
        )
    }
}

@Composable
fun ChatContainer(
    viewModel: ChatViewModel,
    messages: List<Message>,
    uploadState: UploadState,
    chatMode: ChatModeType,
    send: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        ChatList(
            messages = messages,
            modifier = Modifier.weight(1f)
        )

        // Show upload progress indicator if uploading
        if (uploadState is UploadState.Uploading) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "$UPLOADING_TEXT${(uploadState.progress * 100).toInt()}%",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        // Chat Mode Dropdown
        ChatModeDropdown(
            selectedMode = chatMode,
            onModeSelected = { viewModel.setChatMode(it) },
            modifier = Modifier.fillMaxWidth()
        )

        ChatFooter(
            viewModel = viewModel,
            modifier = Modifier.wrapContentHeight()
        ) {
            if (it.isNotBlank() || it.isNotEmpty())
                send(it)
        }
    }
}

@Composable
fun ChatFooter(viewModel: ChatViewModel, modifier: Modifier = Modifier, send: (String) -> Unit) {
    var text by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val filePicker = getFilePicker()

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
            },
            onAttachClick = {
                scope.launch {
                    val file = filePicker.pickFile()
                    if (file != null) {
                        println("$SELECTED_FILE_LOG${file.name}, Size: ${file.size}$SIZE_BYTES_SUFFIX${file.mimeType}")
                        // Upload file using viewModel
                        viewModel.uploadFile(file, filePicker)
                    }
                }
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
        Icon(Icons.Filled.Send, contentDescription = SEND_CONTENT_DESC)
    }
}

@Composable
fun ChatInput(
    modifier: Modifier = Modifier,
    textStyle: TextStyle = LocalTextStyle.current,
    colors: TextFieldColors = OutlinedTextFieldDefaults.colors(),
    text: String,
    onSend: () -> Unit = {},
    onAttachClick: () -> Unit = {},
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
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 8.dp)
        ) {
            // Attach button
            IconButton(
                onClick = onAttachClick,
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = ATTACH_FILE_CONTENT_DESC,
                    tint = Color.Gray
                )
            }

            // Text input
            Box(
                modifier = Modifier.weight(1f)
            ) {
                if (text.isEmpty() && !isFocused) {
                    Text(
                        ENTER_TEXT_PLACEHOLDER,
                        color = Color.Gray,
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .padding(start = 8.dp)
                    )
                }
                BasicTextField(
                    value = text,
                    onValueChange = onTextChange,
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
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
        name = SAMPLE_PRODUCT_NAME,
        price = 19.99
    )
    val receiveMessage = ReceiveMessage(
        msg = SAMPLE_PRODUCTS_MESSAGE,
        id = ""
    )
    val sentMessage = SentMessage(
        msg = THANK_YOU_MESSAGE,
        id = ""
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

@Composable
fun ChatModeDropdown(
    selectedMode: ChatModeType,
    onModeSelected: (ChatModeType) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    val options = remember { ChatModeType.values() }

    Column(
        modifier = modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = CHAT_MODE_LABEL,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Surface(
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(1.dp, Color.Gray),
            modifier = Modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = 48.dp)
                .background(Color.Transparent)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 12.dp)
                    .background(Color.Transparent)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(selectedMode.value.ifBlank { SELECT_CHAT_MODE })
                    IconButton(onClick = { expanded = !expanded }) {
                        Icon(Icons.Default.MoreVert, contentDescription = null)
                    }
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    options.forEach { mode ->
                        DropdownMenuItem(
                            text = { Text(mode.value) },
                            onClick = {
                                onModeSelected(mode)
                                expanded = false
                            }
                        )
                    }
                }
            }
        }
    }
}
