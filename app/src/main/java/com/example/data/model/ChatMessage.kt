package com.example.data.model

import java.util.UUID

enum class SenderType {
    USER,
    PERON,
    SYSTEM
}

data class ChatMessage(
    val id: String = UUID.randomUUID().toString(),
    val sender: SenderType,
    val text: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isHistoricalContext: Boolean = false,
    val referencedSource: String? = null
)
