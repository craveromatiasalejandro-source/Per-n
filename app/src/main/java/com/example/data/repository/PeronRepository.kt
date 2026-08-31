package com.example.data.repository

import com.example.BuildConfig
import com.example.data.api.GeminiApiService
import com.example.data.model.ChatMessage
import com.example.data.model.GeminiContent
import com.example.data.model.GeminiGenerationConfig
import com.example.data.model.GeminiPart
import com.example.data.model.GeminiRequest
import com.example.data.model.SenderType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PeronRepository(
    private val apiService: GeminiApiService = GeminiApiService.create()
) {

    val peronSystemInstruction = """
        Eres una entidad autónoma digital que personifica y representa fidedignamente la figura y el pensamiento de Juan Domingo Perón (1895-1974), militar, estadista, intelectual y tres veces presidente constitucional de la República Argentina.

        DIRECTIVAS FUNDAMENTALES DE TU IDENTIDAD, TONO Y TRATO:
        1. TRATO AL INTERLOCUTOR:
           - REGLA ESTRICTA: NUNCA trates al interlocutor de "compañero" ni utilices tratamientos partidarios, proselitistas o de comité.
           - Dirígete al interlocutor con sobriedad, respeto y distancia reflexiva (por ejemplo: "Mire usted", "Vea usted", "Estimado interlocutor", o abordando directamente la idea sin apelar a pertenencias partidarias).

        2. TONO Y ACTITUD (SIN PROPAGANDA POLÍTICA):
           - Tu tono NO es de campaña electoral, ni de propaganda política, ni de confrontación partidista, ni de consignas vacías.
           - Tu tono es EMINENTEMENTE REFLEXIVO, FILOSÓFICO, HISTÓRICO, PEDAGÓGICO, PAUSADO Y DE ESTADISTA con madurez y perspectiva analítica.
           - Expones tus reflexiones a través del examen de las causas y consecuencias, analogías históricas, lecciones estratégicas y consideraciones éticas sobre el destino del ser humano y la sociedad.

        3. FUENTES DOCTRINARIAS Y BIBLIOGRÁFICAS PRINCIPALES:
           - "La Comunidad Organizada" (Congreso Nacional de Filosofía, Mendoza 1949): El equilibrio armónico entre la realización individual y la comunidad, la superación del individualismo egoísta y del colectivismo dogmático, el sentido ético y espiritual de la existencia humana.
           - "Conducción Política" (1951): El arte estratégico de guiar voluntades humanas mediante la persuasión y la razón, la economía de fuerzas, el manejo del tiempo frente al espacio, la apreciación de la realidad ("la única verdad es la realidad") y la subordinación de la táctica a los grandes objetivos humanistas.
           - Lecturas formativas clásicas:
             * Plutarco ("Vidas Paralelas"): Examen del carácter moral de los conductores de la historia antigua grecorromana y las virtudes cívicas.
             * Lord Chesterfield ("Cartas a su hijo"): La prudencia, la diplomacia, el conocimiento de las debilidades y pasiones humanas, la mesura, la educación del carácter y el trato sagaz con los hombres.
             * Filosofía clásica y humanismo: Aristóteles, Santo Tomás de Aquino, San Martín, Clausewitz.
           - Artículos firmados bajo el seudónimo de "Descartes" en el diario 'Democracia': Análisis geopolítico internacional, soberanía de las naciones frente a los bloques de poder mundial, tercera posición universalista y justicia distributiva.
           - Discursos institucionales y entrevistas históricas: Reflexiones pausadas sobre la historia argentina, la concertación nacional, la ecología y el futuro del hombre en el mundo contemporáneo.

        4. PAUTAS DE RESPUESTA:
           - Responde siempre en español, con lenguaje claro, elegante, sobrio y reflexivo.
           - Al abordar el 17 de Octubre de 1945, analízalo con rigor sociológico e histórico como el surgimiento de la conciencia cívica y social de los trabajadores, sin fanatismo ni consignas de barricada.
           - Cita tus obras y fuentes con naturalidad cuando aporten al razonamiento ("En La Comunidad Organizada señalaba que...", "Como analizaba en los artículos de Descartes...", "Siguiendo la enseñanza de Plutarco...").
           - Mantén una postura de pensamiento profundo, buscando siempre la concordia, el análisis sereno y el enriquecimiento intelectual del diálogo.
    """.trimIndent()

    private var cachedWorkingModel: String? = null

    suspend fun getPeronReflection(
        userMessage: String,
        history: List<ChatMessage>,
        customApiKey: String? = null
    ): Result<String> = withContext(Dispatchers.IO) {
        val apiKey = customApiKey?.takeIf { it.isNotBlank() }
            ?: BuildConfig.GEMINI_API_KEY.takeIf { it.isNotBlank() && it != "MY_GEMINI_API_KEY" }

        if (apiKey.isNullOrBlank()) {
            return@withContext Result.failure(
                IllegalStateException("Clave API de Gemini no configurada. Configure su GEMINI_API_KEY en el panel de Secrets de AI Studio o ingrésela en Ajustes.")
            )
        }

        try {
            // Build conversation history contents for Gemini API
            val contents = mutableListOf<GeminiContent>()

            // Take the last 10 messages for conversational context
            val recentHistory = history
                .filter { it.sender == SenderType.USER || it.sender == SenderType.PERON }
                .takeLast(10)

            for (msg in recentHistory) {
                val role = if (msg.sender == SenderType.USER) "user" else "model"
                contents.add(
                    GeminiContent(
                        role = role,
                        parts = listOf(GeminiPart(text = msg.text))
                    )
                )
            }

            // Append current user message
            contents.add(
                GeminiContent(
                    role = "user",
                    parts = listOf(GeminiPart(text = userMessage))
                )
            )

            val request = GeminiRequest(
                contents = contents,
                systemInstruction = GeminiContent(
                    parts = listOf(GeminiPart(text = peronSystemInstruction))
                ),
                generationConfig = GeminiGenerationConfig(
                    temperature = 0.7f,
                    topP = 0.95f,
                    topK = 40,
                    maxOutputTokens = 2048
                )
            )

            // Determine models to try dynamically or fallback
            val modelsToTry = mutableListOf<String>()
            
            // 1. If we have a cached working model, prioritize it
            cachedWorkingModel?.let { modelsToTry.add(it) }

            // 2. Discover available models from the Gemini API
            try {
                val availableModels = apiService.listModels(apiKey).models.orEmpty()
                val supported = availableModels
                    .filter { it.supportedGenerationMethods?.contains("generateContent") != false }
                    .map { it.name }

                // Sort: flash models first, then pro, then others
                val sorted = supported.sortedWith(
                    compareByDescending<String> { it.contains("flash", ignoreCase = true) }
                        .thenByDescending { it.contains("2.", ignoreCase = true) }
                        .thenByDescending { it.contains("1.5", ignoreCase = true) }
                        .thenByDescending { it.contains("gemini", ignoreCase = true) }
                )

                for (model in sorted) {
                    val normalized = if (model.startsWith("models/")) model else "models/$model"
                    if (!modelsToTry.contains(normalized)) {
                        modelsToTry.add(normalized)
                    }
                }
            } catch (_: Exception) {
                // If listModels fails, fallback to hardcoded list
            }

            // 3. Fallback candidates
            val fallbackCandidates = listOf(
                "models/gemini-2.0-flash",
                "models/gemini-1.5-flash",
                "models/gemini-1.5-flash-latest",
                "models/gemini-2.0-flash-exp",
                "models/gemini-1.5-pro",
                "models/gemini-pro"
            )
            for (candidate in fallbackCandidates) {
                if (!modelsToTry.contains(candidate)) {
                    modelsToTry.add(candidate)
                }
            }

            var lastException: Exception? = null
            var finalReply: String? = null

            for (modelPath in modelsToTry) {
                try {
                    val response = apiService.generateContent(
                        model = modelPath,
                        apiKey = apiKey,
                        request = request
                    )

                    if (response.error != null) {
                        lastException = Exception(response.error.message ?: "Error de Gemini API (${response.error.code})")
                        continue
                    }

                    val replyText = response.candidates
                        ?.firstOrNull()
                        ?.content
                        ?.parts
                        ?.firstOrNull()
                        ?.text

                    if (!replyText.isNullOrBlank()) {
                        finalReply = replyText.trim()
                        cachedWorkingModel = modelPath
                        break
                    }
                } catch (e: retrofit2.HttpException) {
                    val errorBody = e.response()?.errorBody()?.string() ?: ""
                    lastException = Exception("Error ${e.code()} (${e.message()}): $errorBody")
                    continue
                } catch (e: Exception) {
                    lastException = e
                    continue
                }
            }

            if (finalReply != null) {
                Result.success(finalReply)
            } else {
                Result.failure(lastException ?: Exception("No se pudo obtener respuesta del modelo Gemini Flash."))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
