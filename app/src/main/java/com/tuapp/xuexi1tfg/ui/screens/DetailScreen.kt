// DetailScreen.kt
package com.tuapp.xuexi1tfg.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.animation.*
import androidx.compose.animation.core.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    hanzi: String = "学", // Carácter por defecto para demostración
    onBackPressed: () -> Unit = {}
) {
    // Estados para controlar las secciones expandibles
    var isStrokesExpanded by remember { mutableStateOf(false) }
    var isRelatedWordsExpanded by remember { mutableStateOf(true) }
    var isExamplesExpanded by remember { mutableStateOf(false) }

    // Datos de ejemplo para demostración
    val hanziData = HanziData(
        character = hanzi,
        pinyin = "xué",
        meaning = "estudiar, aprender",
        strokes = 8,
        strokeOrder = listOf("一", "丶", "丶", "㇙", "一", "丿", "乀", "亅"),
        relatedWords = listOf(
            RelatedWord("学生", "xuéshēng", "estudiante"),
            RelatedWord("学校", "xuéxiào", "escuela"),
            RelatedWord("学习", "xuéxí", "estudiar"),
            RelatedWord("大学", "dàxué", "universidad"),
            RelatedWord("学问", "xuéwèn", "conocimiento")
        ),
        examples = listOf(
            Example("我在学中文。", "Wǒ zài xué zhōngwén.", "Estoy estudiando chino."),
            Example("他是学生。", "Tā shì xuéshēng.", "Él es estudiante."),
            Example("学校很大。", "Xuéxiào hěn dà.", "La escuela es muy grande.")
        ),
        radicals = listOf("子", "冖"),
        difficulty = "初级", // Básico, Intermedio, Avanzado
        frequency = "Alta",
        hskLevel = 1 // Nivel HSK añadido
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // ======= TOP BAR =======
        TopAppBar(
            title = {
                Text(
                    text = "Detalles del Hanzi",
                    style = MaterialTheme.typography.headlineSmall
                )
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
                // Botón de favoritos
                IconButton(onClick = { /* Agregar a favoritos */ }) {
                    Icon(
                        Icons.Default.FavoriteBorder,
                        contentDescription = "Agregar a favoritos",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
                // Botón de compartir
                IconButton(onClick = { /* Compartir */ }) {
                    Icon(
                        Icons.Default.Share,
                        contentDescription = "Compartir"
                    )
                }
            }
        )

        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // ======= CARÁCTER PRINCIPAL =======
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Carácter grande
                    Text(
                        text = hanziData.character,
                        fontSize = 120.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        textAlign = TextAlign.Center
                    )

                    // Pinyin
                    Text(
                        text = hanziData.pinyin,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.primary
                    )

                    // Significado
                    Text(
                        text = hanziData.meaning,
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        textAlign = TextAlign.Center
                    )
                }
            }

            // ======= INFORMACIÓN BÁSICA =======
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Información Básica",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        InfoItem("Trazos", hanziData.strokes.toString())
                        InfoItem("HSK", "Nivel ${hanziData.hskLevel}")
                        InfoItem("Dificultad", hanziData.difficulty)
                        InfoItem("Frecuencia", hanziData.frequency)
                    }

                    // Radicales
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Radicales:",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium
                        )
                        hanziData.radicals.forEach { radical ->
                            Surface(
                                color = MaterialTheme.colorScheme.secondaryContainer,
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = radical,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    fontSize = 16.sp,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                            }
                        }
                    }
                }
            }

            // ======= ORDEN DE TRAZOS =======
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { isStrokesExpanded = !isStrokesExpanded }
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Orden de Trazos",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Icon(
                            imageVector = if (isStrokesExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                            contentDescription = if (isStrokesExpanded) "Colapsar" else "Expandir"
                        )
                    }

                    AnimatedVisibility(
                        visible = isStrokesExpanded,
                        enter = expandVertically() + fadeIn(),
                        exit = shrinkVertically() + fadeOut()
                    ) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            // Botón de animación
                            Button(
                                onClick = { /* Iniciar animación de trazos */ },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(Icons.Default.PlayArrow, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Ver Animación de Trazos")
                            }

                            // Secuencia de trazos
                            Text(
                                text = "Secuencia de trazos:",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium
                            )

                            LazyStrokeGrid(strokes = hanziData.strokeOrder)
                        }
                    }
                }
            }

            // ======= PALABRAS RELACIONADAS =======
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { isRelatedWordsExpanded = !isRelatedWordsExpanded }
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Palabras Relacionadas",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Icon(
                            imageVector = if (isRelatedWordsExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                            contentDescription = if (isRelatedWordsExpanded) "Colapsar" else "Expandir"
                        )
                    }

                    AnimatedVisibility(
                        visible = isRelatedWordsExpanded,
                        enter = expandVertically() + fadeIn(),
                        exit = shrinkVertically() + fadeOut()
                    ) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            hanziData.relatedWords.forEach { word ->
                                RelatedWordItem(word = word)
                            }
                        }
                    }
                }
            }

            // ======= EJEMPLOS DE USO =======
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { isExamplesExpanded = !isExamplesExpanded }
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Ejemplos de Uso",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Icon(
                            imageVector = if (isExamplesExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                            contentDescription = if (isExamplesExpanded) "Colapsar" else "Expandir"
                        )
                    }

                    AnimatedVisibility(
                        visible = isExamplesExpanded,
                        enter = expandVertically() + fadeIn(),
                        exit = shrinkVertically() + fadeOut()
                    ) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            hanziData.examples.forEach { example ->
                                ExampleItem(example = example)
                            }
                        }
                    }
                }
            }
        }
    }
}

// ======= COMPONENTES AUXILIARES =======

@Composable
fun InfoItem(label: String, value: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}



@Composable
fun LazyStrokeGrid(strokes: List<String>) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        strokes.chunked(4).forEach { rowStrokes ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                rowStrokes.forEach { stroke ->
                    Surface(
                        modifier = Modifier
                            .size(50.dp)
                            .border(
                                1.dp,
                                MaterialTheme.colorScheme.outline,
                                RoundedCornerShape(8.dp)
                            ),
                        color = Color.Transparent,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Box(
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = stroke,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
                // Rellenar espacios vacíos
                repeat(4 - rowStrokes.size) {
                    Spacer(modifier = Modifier.size(50.dp))
                }
            }
        }
    }
}

@Composable
fun RelatedWordItem(word: RelatedWord) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* Navegar a detalles de la palabra */ },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = word.characters,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Text(
                    text = word.pinyin,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = word.meaning,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Icon(
                Icons.Default.ChevronRight,
                contentDescription = "Ver detalles",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun ExampleItem(example: Example) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = example.chinese,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = example.pinyin,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = example.translation,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onTertiaryContainer
            )
        }
    }
}

// ======= CLASES DE DATOS =======

data class HanziData(
    val character: String,
    val pinyin: String,
    val meaning: String,
    val strokes: Int,
    val strokeOrder: List<String>,
    val relatedWords: List<RelatedWord>,
    val examples: List<Example>,
    val radicals: List<String>,
    val difficulty: String,
    val frequency: String,
    val hskLevel: Int // Campo HSK añadido
)

data class RelatedWord(
    val characters: String,
    val pinyin: String,
    val meaning: String
)

data class Example(
    val chinese: String,
    val pinyin: String,
    val translation: String
)