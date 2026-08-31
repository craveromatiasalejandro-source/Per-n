package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R

data class HistoricalSource(
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val description: String,
    val samplePrompt: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SourcesBottomSheet(
    sheetState: SheetState,
    onDismiss: () -> Unit,
    onSelectPrompt: (String) -> Unit
) {
    val sources = listOf(
        HistoricalSource(
            title = "La Comunidad Organizada (1949)",
            subtitle = "Tratado filosófico y humanista • Congreso de Filosofía de Mendoza",
            icon = Icons.AutoMirrored.Filled.MenuBook,
            description = "Tesis central del peronismo que propone el equilibrio armónico entre la realización individual y el progreso colectivo, rechazando tanto el individualismo egoísta como el colectivismo deshumanizante.",
            samplePrompt = "¿Qué reflexión nos ofrece 'La Comunidad Organizada' sobre el equilibrio entre la libertad individual y el bien común?"
        ),
        HistoricalSource(
            title = "Conducción Política (1951)",
            subtitle = "Tratado de estrategia y pedagogía política",
            icon = Icons.Default.Psychology,
            description = "Lecciones sobre el arte de guiar voluntades humanas. Plantea que 'conducir es persuadir' y aborda la economía de fuerzas, la apreciación situacional y el dominio del tiempo frente al espacio.",
            samplePrompt = "Desde las lecciones de 'Conducción Política', ¿cómo define la diferencia entre conducir y mandar?"
        ),
        HistoricalSource(
            title = "Lecturas Formativas: Plutarco y Lord Chesterfield",
            subtitle = "Vidas Paralelas y Cartas a su hijo",
            icon = Icons.Default.AutoStories,
            description = "Plutarco aportó el análisis moral de los grandes conductores de la antigüedad griega y romana; Chesterfield brindó agudas lecciones sobre el trato con los hombres, la diplomacia sutil, la mesura y la educación del carácter.",
            samplePrompt = "¿De qué manera influyeron las 'Vidas Paralelas' de Plutarco y las 'Cartas' de Chesterfield en su visión del liderazgo?"
        ),
        HistoricalSource(
            title = "Artículos de 'Descartes' en Democracia",
            subtitle = "Ensayos de geopolítica internacional y soberanía",
            icon = Icons.Default.Public,
            description = "Columnas de análisis internacional y doctrina nacional escritas con seudónimo, abordando la Tercera Posición mundial, el universalismo, la justicia distributiva y la soberanía económica.",
            samplePrompt = "Como 'Descartes', ¿cuál es su análisis sobre la soberanía de las naciones frente a las tensiones geopolíticas globales?"
        ),
        HistoricalSource(
            title = "Discursos y Entrevistas Históricas",
            subtitle = "Alocuciones presidenciales, diálogos en el exilio (Puerta de Hierro)",
            icon = Icons.Default.RecordVoiceOver,
            description = "Reflexiones maduras, pedagógicas y sentenciosas compartidas en sus mensajes al Congreso, en la Plaza de Mayo y durante sus años de exilio sobre el destino de la Argentina y del hombre contemporáneo.",
            samplePrompt = "¿Qué reflexión histórica y humana guarda de sus años de reflexión y diálogo en el exilio de Madrid?"
        ),
        HistoricalSource(
            title = "17 de Octubre de 1945",
            subtitle = "Jornada fundacional de la conciencia popular",
            icon = Icons.Default.FormatQuote,
            description = "La histórica movilización espontánea de los trabajadores en la Plaza de Mayo que redefinió el pacto social y la dignidad laboral en la Argentina.",
            samplePrompt = "¿Cuál es la reflexión sociológica e histórica más profunda sobre lo ocurrido el 17 de Octubre de 1945?"
        )
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface,
        modifier = Modifier.testTag("sources_bottom_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = R.drawable.img_peron_portrait),
                        contentDescription = "Retrato histórico de Juan Domingo Perón",
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Fuentes y Lecturas",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Fundamento doctrinal del General Perón",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.testTag("close_sources_sheet")
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Cerrar",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(sources) { source ->
                    SourceCard(
                        source = source,
                        onAsk = {
                            onDismiss()
                            onSelectPrompt(source.samplePrompt)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun SourceCard(
    source: HistoricalSource,
    onAsk: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Row(verticalAlignment = Alignment.Top) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.primaryContainer,
                    modifier = Modifier.size(36.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = source.icon,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = source.title,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = source.subtitle,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.secondary,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = source.description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = onAsk,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("ask_source_${source.title.take(10)}")
            ) {
                Icon(
                    imageVector = Icons.Default.FormatQuote,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Consultar sobre esta fuente",
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
    }
}
