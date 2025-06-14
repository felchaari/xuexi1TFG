
/*
package com.tuapp.xuexi1tfg.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultsScreen(
    searchQuery: String = "学",
    searchResults: List<HanziSearchResult> = getSampleResults(),
    onBackPressed: () -> Unit = {},
    onCharacterClick: (String) -> Unit = {},
    onNewSearch: () -> Unit = {}
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // ======= TOP BAR =======
        TopAppBar(
            title = {
                Column {
                    Text(
                        text = "Resultados de búsqueda",
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Text(
                        text = "${searchResults.size} resultados para \"$searchQuery\"",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            },
            navigationIcon = {
                IconButton(onClick = onBackPressed) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Volver"
                    )
                }
            },
            actions = {
                // Botón de nueva búsqueda
                IconButton(onClick = onNewSearch) {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = "Nueva búsqueda"
                    )
                }
                // Botón de filtros
                IconButton(onClick = { /* Abrir filtros */ }) {
                    Icon(
                        Icons.Default.FilterList,
                        contentDescription = "Filtros"
                    )
                }
            }
        )

        // ======= CONTENIDO =======
        if (searchResults.isEmpty()) {
            // Estado vacío
            EmptyResultsContent(
                searchQuery = searchQuery,
                onNewSearch = onNewSearch
            )
        } else {
            // Lista de resultados
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(searchResults) { result ->
                    HanziResultCard(
                        result = result,
                        onClick = { onCharacterClick(result.character) }
                    )
                }

                // Espaciado final
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

// ======= COMPONENTES AUXILIARES =======

@Composable
fun HanziResultCard(
    result: HanziSearchResult,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Carácter principal
            Surface(
                modifier = Modifier.size(80.dp),
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(12.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = result.character,
                        fontSize = 40.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            // Información del carácter
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // Pinyin y significado
                Text(
                    text = result.pinyin,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = result.meaning,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )

                // Información adicional
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    InfoChip(
                        label = "HSK ${result.hskLevel}",
                        color = getHSKColor(result.hskLevel)
                    )
                    InfoChip(
                        label = "${result.strokes} trazos",
                        color = MaterialTheme.colorScheme.outline
                    )
                    InfoChip(
                        label = result.frequency,
                        color = when (result.frequency) {
                            "Alta" -> MaterialTheme.colorScheme.primary
                            "Media" -> MaterialTheme.colorScheme.secondary
                            else -> MaterialTheme.colorScheme.outline
                        }
                    )
                }
            }

            // Icono de navegación
            Icon(
                Icons.Default.ChevronRight,
                contentDescription = "Ver detalles",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun InfoChip(
    label: String,
    color: androidx.compose.ui.graphics.Color
) {
    Surface(
        color = color.copy(alpha = 0.1f),
        shape = RoundedCornerShape(6.dp)
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
            style = MaterialTheme.typography.bodySmall,
            color = color,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun EmptyResultsContent(
    searchQuery: String,
    onNewSearch: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            Icons.Default.SearchOff,
            contentDescription = null,
            modifier = Modifier.size(80.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Sin resultados",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "No se encontraron caracteres para \"$searchQuery\"",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Intenta con:",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(8.dp))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text("• Un carácter diferente", style = MaterialTheme.typography.bodyMedium)
            Text("• Pinyin con tonos (ej: xuě)", style = MaterialTheme.typography.bodyMedium)
            Text("• Significado en español", style = MaterialTheme.typography.bodyMedium)
            Text("• Términos más generales", style = MaterialTheme.typography.bodyMedium)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onNewSearch,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.Search, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Nueva búsqueda")
        }
    }
}

// ======= FUNCIONES AUXILIARES =======

fun getHSKColor(hskLevel: Int): androidx.compose.ui.graphics.Color {
    return when (hskLevel) {
        1 -> androidx.compose.ui.graphics.Color(0xFF4CAF50) // Verde
        2 -> androidx.compose.ui.graphics.Color(0xFF8BC34A) // Verde claro
        3 -> androidx.compose.ui.graphics.Color(0xFFFFEB3B) // Amarillo
        4 -> androidx.compose.ui.graphics.Color(0xFFFF9800) // Naranja
        5 -> androidx.compose.ui.graphics.Color(0xFFFF5722) // Naranja oscuro
        6 -> androidx.compose.ui.graphics.Color(0xFFF44336) // Rojo
        else -> androidx.compose.ui.graphics.Color(0xFF757575) // Gris
    }
}

fun getSampleResults(): List<HanziSearchResult> {
    return listOf(
        HanziSearchResult(
            character = "学",
            pinyin = "xué",
            meaning = "estudiar, aprender",
            strokes = 8,
            hskLevel = 1,
            frequency = "Alta"
        ),
        HanziSearchResult(
            character = "雪",
            pinyin = "xuě",
            meaning = "nieve",
            strokes = 11,
            hskLevel = 2,
            frequency = "Media"
        ),
        HanziSearchResult(
            character = "血",
            pinyin = "xuè",
            meaning = "sangre",
            strokes = 6,
            hskLevel = 3,
            frequency = "Media"
        ),
        HanziSearchResult(
            character = "学生",
            pinyin = "xuéshēng",
            meaning = "estudiante",
            strokes = 16,
            hskLevel = 1,
            frequency = "Alta"
        ),
        HanziSearchResult(
            character = "学校",
            pinyin = "xuéxiào",
            meaning = "escuela",
            strokes = 18,
            hskLevel = 1,
            frequency = "Alta"
        )
    )
}

// ======= CLASES DE DATOS =======

data class HanziSearchResult(
    val character: String,
    val pinyin: String,
    val meaning: String,
    val strokes: Int,
    val hskLevel: Int,
    val frequency: String
) */