package com.example.aifitnessapp.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.aifitnessapp.BuildConfig
import com.example.aifitnessapp.domain.model.ChatMessage
import com.example.aifitnessapp.presentation.viewmodels.AiCoachFactory
import com.example.aifitnessapp.presentation.viewmodels.AiCoachViewModel
import kotlinx.coroutines.launch

@Composable
fun AiCoachScreen() {

    val apiKey = BuildConfig.GROQ_API_KEY
    val context = LocalContext.current

    // ViewModel with Groq support
    val vm: AiCoachViewModel = viewModel(
        factory = AiCoachFactory(
            apiKey = apiKey,
            context = context
        )
    )

    val messages by vm.messages.collectAsState()
    val isStreaming by vm.isStreaming.collectAsState()
    var input by remember { mutableStateOf("") }

    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    // Auto-scroll when new messages come
    LaunchedEffect(messages.size) {
        scope.launch {
            if (messages.isNotEmpty()) {
                listState.animateScrollToItem(0)
            }
        }
    }

    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text("AI Fitness Coach", style = MaterialTheme.typography.headlineMedium)

        Spacer(Modifier.height(16.dp))

        // Chat list
        LazyColumn(
            modifier = Modifier.weight(1f),
            state = listState,
            reverseLayout = true
        ) {

            items(messages.reversed()) { msg ->
                ChatBubble(msg)
            }

            if (isStreaming) {
                item { TypingIndicator() }
            }
        }

        Spacer(Modifier.height(12.dp))

        // Input + Send Button
        Row(verticalAlignment = Alignment.CenterVertically) {

            OutlinedTextField(
                value = input,
                onValueChange = { input = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Ask your fitness coach…") }
            )

            Spacer(Modifier.width(8.dp))

            IconButton(
                onClick = {
                    if (input.isNotBlank()) {
                        vm.sendMessage(input)
                        input = ""
                    }
                }
            ) {
                Icon(Icons.Default.Send, contentDescription = "Send")
            }
        }
    }
}

@Composable
fun ChatBubble(msg: ChatMessage) {
    val isUser = msg.role == "user"

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (isUser) Alignment.End else Alignment.Start
    ) {
        Surface(
            color = if (isUser)
                MaterialTheme.colorScheme.primaryContainer
            else
                MaterialTheme.colorScheme.secondaryContainer,
            shape = MaterialTheme.shapes.medium
        ) {
            Text(
                msg.content,
                modifier = Modifier.padding(12.dp)
            )
        }
        Spacer(Modifier.height(6.dp))
    }
}

@Composable
fun TypingIndicator() {
    Text(
        "Typing...",
        modifier = Modifier.padding(12.dp),
        color = MaterialTheme.colorScheme.primary
    )
}
