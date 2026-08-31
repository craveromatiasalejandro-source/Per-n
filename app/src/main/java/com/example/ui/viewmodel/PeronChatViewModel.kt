package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.ChatMessage
import com.example.data.model.SenderType
import com.example.data.repository.PeronRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PeronChatViewModel(
    private val repository: PeronRepository = PeronRepository()
) : ViewModel() {

    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _customApiKey = MutableStateFlow("")
    val customApiKey: StateFlow<String> = _customApiKey.asStateFlow()

    val promptSuggestions = listOf(
        "¿Cuál es el valor del 17 de Octubre de 1945?",
        "Sobre 'La Comunidad Organizada' y la armonía social",
        "El arte de la persuasión en 'Conducción Política'",
        "¿Qué lecciones extrajo de Plutarco y Chesterfield?",
        "Su visión como 'Descartes' sobre la soberanía mundial"
    )

    init {
        // Initial welcome message from General Perón in a reflective historical tone
        val welcomeMessage = ChatMessage(
            sender = SenderType.PERON,
            text = "Celebro este espacio de diálogo y reflexión. Como siempre he considerado, el hombre no puede realizarse plenamente sino en el seno de una comunidad armónica. A lo largo de los años he dedicado muchas horas al estudio y al pensamiento: desde las lecciones éticas de Plutarco y la sagacidad de Chesterfield, hasta los principios de 'La Comunidad Organizada' y 'Conducción Política', pasando por el análisis geopolítico que escribí bajo la firma de Descartes y el significado histórico del 17 de Octubre de 1945. Dígame, ¿sobre qué aspecto de la historia, la sociedad o la condición humana desea que reflexionemos hoy?"
        )
        _messages.value = listOf(welcomeMessage)
    }

    fun sendMessage(text: String) {
        val trimmed = text.trim()
        if (trimmed.isBlank() || _isLoading.value) return

        val userMessage = ChatMessage(
            sender = SenderType.USER,
            text = trimmed
        )

        _messages.update { it + userMessage }
        _errorMessage.value = null
        _isLoading.value = true

        viewModelScope.launch {
            val result = repository.getPeronReflection(
                userMessage = trimmed,
                history = _messages.value,
                customApiKey = _customApiKey.value
            )

            result.onSuccess { reply ->
                val peronReply = ChatMessage(
                    sender = SenderType.PERON,
                    text = reply
                )
                _messages.update { it + peronReply }
            }.onFailure { error ->
                _errorMessage.value = error.message ?: "Ocurrió un error al consultar a la entidad de Juan Domingo Perón."
            }

            _isLoading.value = false
        }
    }

    fun setCustomApiKey(key: String) {
        _customApiKey.value = key
    }

    fun clearChat() {
        val resetMessage = ChatMessage(
            sender = SenderType.PERON,
            text = "Comencemos nuevamente nuestro diálogo. En la conducción y en el pensamiento, el tiempo es el mejor aliado de la verdad. ¿Qué tema le gustaría que analicemos?"
        )
        _messages.value = listOf(resetMessage)
        _errorMessage.value = null
    }

    fun dismissError() {
        _errorMessage.value = null
    }
}
