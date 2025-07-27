package com.carlos.movilwordkapp.model

data class ChatMessage(
    val text: String,
    val isSentByMe: Boolean // true si lo envié yo, false si lo recibí
)