package com.carlos.movilwordkapp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.carlos.movilwordkapp.model.ChatMessage
import com.carlos.movilwordkapp.model.SampleData
import com.carlos.movilwordkapp.ui.theme.AccentPink
import com.carlos.movilwordkapp.ui.theme.PrimaryVariant

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(navController: NavController) {
    var messageText by remember { mutableStateOf("") }
    val conversation = SampleData.sampleConversation

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Javier", fontWeight = FontWeight.Bold) }, // Nombre del match
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PrimaryVariant,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        bottomBar = {
            MessageInput(
                text = messageText,
                onTextChange = { messageText = it },
                onSendClick = { /* Lógica de envío en el futuro */ }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 8.dp),
            reverseLayout = true // Para que empiece desde abajo
        ) {
            items(conversation.reversed()) { message ->
                MessageBubble(message = message)
            }
        }
    }
}

@Composable
fun MessageBubble(message: ChatMessage) {
    val horizontalArrangement = if (message.isSentByMe) Arrangement.End else Arrangement.Start
    val bubbleColor = if (message.isSentByMe) AccentPink else Color.DarkGray

    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = horizontalArrangement
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(bubbleColor)
                .padding(12.dp)
        ) {
            Text(text = message.text, color = Color.White)
        }
    }
}

@Composable
fun MessageInput(text: String, onTextChange: (String) -> Unit, onSendClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            value = text,
            onValueChange = onTextChange,
            modifier = Modifier.weight(1f),
            placeholder = { Text("Escribe un mensaje...") },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Gray.copy(alpha = 0.2f),
                unfocusedContainerColor = Color.Gray.copy(alpha = 0.2f),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            shape = RoundedCornerShape(24.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        IconButton(onClick = onSendClick, enabled = text.isNotBlank()) {
            Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Enviar", tint = AccentPink)
        }
    }
}