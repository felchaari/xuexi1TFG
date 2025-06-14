package com.tuapp.xuexi1tfg.ui.screens
// FlashcardScreen.kt

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.animation.*
import androidx.compose.animation.core.*

// Enums para los modos de estudio
enum class StudyMode {
    CHARACTER_TO_PINYIN,     // Mostrar carácter, responder pinyin
    PINYIN_TO_CHARACTER,     // Mostrar pinyin, dibujar carácter
    MEANING_TO_CHARACTER     // Mostrar significado, dibujar carácter
}

enum class DifficultyLevel {
    AGAIN,      // Rojo - < 1 min
    HARD,       // Amarillo - < 6 min
    GOOD,       // Verde - < 10 min
    EASY        // Azul - 4 días
}

// Clase para representar una línea dibujada
data class DrawnPath(
    val path: Path,
    val color: Color = Color.Black
)

// Datos de ejemplo para flashcards
data class FlashcardData(
    val character: String,
    val pinyin: String,
    val meaning: String,
    val strokeCount: Int,
    val difficulty: String,
    val hskLevel: Int
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlashCardScreen(
    onBackPressed: () -> Unit = {}
) {
    // Estados principales
    var studyMode by remember { mutableStateOf(StudyMode.PINYIN_TO_CHARACTER) }
    var isCardFlipped by remember { mutableStateOf(false) }
    var currentCardIndex by remember { mutableStateOf(0) }
    var showModeSelector by remember { mutableStateOf(false) }

    // Estados para el canvas de dibujo
    var drawnPaths by remember { mutableStateOf(listOf<DrawnPath>()) }
    var currentPath by remember { mutableStateOf(Path()) }
    var isDrawing by remember { mutableStateOf(false) }

    // Estados para estadísticas
    var cardsStudied by remember { mutableStateOf(0) }
    var correctAnswers by remember { mutableStateOf(0) }

    // Datos de ejemplo
    val flashcards = remember {
        listOf(
            FlashcardData("学", "xué", "estudiar, aprender", 8, "Básico", 1),
            FlashcardData("生", "shēng", "nacer, vida", 5, "Básico", 1),
            FlashcardData("中", "zhōng", "centro, medio", 4, "Básico", 1),
            FlashcardData("国", "guó", "país, nación", 8, "Básico", 1),
            FlashcardData("人", "rén", "persona", 2, "Básico", 1)
        )
    }

    val currentCard = flashcards[currentCardIndex % flashcards.size]

    // Función para avanzar a la siguiente tarjeta - ARREGLADA
    fun nextCard(difficulty: DifficultyLevel? = null) {
        // Incrementar contador de tarjetas estudiadas
        cardsStudied++

        // Si se proporcionó una dificultad, registrar la respuesta
        difficulty?.let {
            when (it) {
                DifficultyLevel.AGAIN -> {
                    // Tarjeta muy difícil - mostrar nuevamente pronto (< 1 min)
                    // En una implementación real, esto se programaría para mostrar pronto
                    println("Tarjeta marcada como AGAIN - revisar pronto")
                }
                DifficultyLevel.HARD -> {
                    // Tarjeta difícil - mostrar en unos minutos (< 6 min)
                    println("Tarjeta marcada como HARD - revisar en unos minutos")
                }
                DifficultyLevel.GOOD -> {
                    // Respuesta correcta - mostrar en más tiempo (< 10 min)
                    println("Tarjeta marcada como GOOD - revisar más tarde")
                }
                DifficultyLevel.EASY -> {
                    // Respuesta muy fácil - mostrar en días (4 días)
                    correctAnswers++
                    println("Tarjeta marcada como EASY - revisar en varios días")
                }
            }
        }

        // Avanzar al siguiente índice
        currentCardIndex = (currentCardIndex + 1) % flashcards.size

        // Resetear estados de la tarjeta
        isCardFlipped = false
        drawnPaths = emptyList()
        currentPath = Path()
        isDrawing = false
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // ======= TOP BAR =======
        TopAppBar(
            title = {
                Text(
                    text = "Flashcards",
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
                // Selector de modo
                IconButton(onClick = { showModeSelector = true }) {
                    Icon(
                        Icons.Default.Settings,
                        contentDescription = "Configurar modo"
                    )
                }
                // Estadísticas
                IconButton(onClick = { /* Mostrar estadísticas */ }) {
                    Icon(
                        Icons.Default.Analytics,
                        contentDescription = "Estadísticas"
                    )
                }
            }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // ======= INFORMACIÓN DEL PROGRESO =======
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Progreso",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "${currentCardIndex + 1}/${flashcards.size}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "Modo actual",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = when(studyMode) {
                                StudyMode.CHARACTER_TO_PINYIN -> "Carácter → Pinyin"
                                StudyMode.PINYIN_TO_CHARACTER -> "Pinyin → Carácter"
                                StudyMode.MEANING_TO_CHARACTER -> "Significado → Carácter"
                            },
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                // Barra de progreso
                LinearProgressIndicator(
                    progress = { (currentCardIndex + 1).toFloat() / flashcards.size },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 16.dp),
                )
            }

            // ======= FLASHCARD PRINCIPAL =======
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clickable {
                        if (!isCardFlipped && studyMode != StudyMode.CHARACTER_TO_PINYIN) {
                            // No hacer nada, el usuario debe dibujar
                        } else {
                            isCardFlipped = !isCardFlipped
                        }
                    },
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (!isCardFlipped) {
                        // CARA FRONTAL
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            when (studyMode) {
                                StudyMode.CHARACTER_TO_PINYIN -> {
                                    // Mostrar carácter
                                    Text(
                                        text = currentCard.character,
                                        fontSize = 120.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                    Text(
                                        text = "¿Cuál es el pinyin?",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                }

                                StudyMode.PINYIN_TO_CHARACTER -> {
                                    // Mostrar pinyin
                                    Text(
                                        text = currentCard.pinyin,
                                        fontSize = 48.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Text(
                                        text = currentCard.meaning,
                                        fontSize = 24.sp,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                                        textAlign = TextAlign.Center
                                    )
                                    Text(
                                        text = "Dibuja el carácter",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                }

                                StudyMode.MEANING_TO_CHARACTER -> {
                                    // Mostrar significado
                                    Text(
                                        text = currentCard.meaning,
                                        fontSize = 32.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                                        textAlign = TextAlign.Center
                                    )
                                    Text(
                                        text = "Dibuja el carácter",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                }
                            }
                        }
                    } else {
                        // CARA TRASERA - RESPUESTA
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Text(
                                text = currentCard.character,
                                fontSize = 100.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )

                            Text(
                                text = currentCard.pinyin,
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )

                            Text(
                                text = currentCard.meaning,
                                fontSize = 20.sp,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                textAlign = TextAlign.Center
                            )

                            // Información adicional
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    color = MaterialTheme.colorScheme.secondaryContainer,
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text(
                                        text = "HSK ${currentCard.hskLevel}",
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                        style = MaterialTheme.typography.labelMedium,
                                        color = MaterialTheme.colorScheme.onSecondaryContainer
                                    )
                                }

                                Surface(
                                    color = MaterialTheme.colorScheme.tertiaryContainer,
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text(
                                        text = "${currentCard.strokeCount} trazos",
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                        style = MaterialTheme.typography.labelMedium,
                                        color = MaterialTheme.colorScheme.onTertiaryContainer
                                    )
                                }
                            }
                        }
                    }

                    // Indicador para voltear tarjeta
                    if (!isCardFlipped && studyMode == StudyMode.CHARACTER_TO_PINYIN) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .background(
                                    MaterialTheme.colorScheme.surface.copy(alpha = 0.8f),
                                    RoundedCornerShape(12.dp)
                                )
                                .padding(8.dp)
                        ) {
                            Text(
                                text = "Toca para ver respuesta",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }

            // ======= CANVAS DE DIBUJO =======
            if (studyMode != StudyMode.CHARACTER_TO_PINYIN) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Área de Dibujo",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )

                            // Botón limpiar
                            IconButton(
                                onClick = {
                                    drawnPaths = emptyList()
                                    currentPath = Path()
                                }
                            ) {
                                Icon(
                                    Icons.Default.Clear,
                                    contentDescription = "Limpiar dibujo"
                                )
                            }
                        }

                        // Canvas
                        Canvas(
                            modifier = Modifier
                                .fillMaxSize()
                                .border(
                                    1.dp,
                                    MaterialTheme.colorScheme.outline,
                                    RoundedCornerShape(8.dp)
                                )
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.White)
                                .pointerInput(Unit) {
                                    detectDragGestures(
                                        onDragStart = { offset ->
                                            currentPath = Path().apply { moveTo(offset.x, offset.y) }
                                            isDrawing = true
                                        },
                                        onDrag = { change, dragAmount ->
                                            if (isDrawing) {
                                                currentPath.lineTo(change.position.x, change.position.y)
                                            }
                                        },
                                        onDragEnd = {
                                            if (isDrawing) {
                                                drawnPaths = drawnPaths + DrawnPath(Path().apply {
                                                    addPath(currentPath)
                                                })
                                                currentPath = Path()
                                                isDrawing = false
                                            }
                                        }
                                    )
                                }
                        ) {
                            // Dibujar líneas guardadas
                            drawnPaths.forEach { drawnPath ->
                                drawPath(
                                    path = drawnPath.path,
                                    color = drawnPath.color,
                                    style = Stroke(width = 4.dp.toPx())
                                )
                            }

                            // Dibujar línea actual
                            if (isDrawing) {
                                drawPath(
                                    path = currentPath,
                                    color = Color.Black,
                                    style = Stroke(width = 8.dp.toPx())
                                )
                            }
                        }
                    }
                }
            }

            // ======= BOTÓN MOSTRAR RESPUESTA =======
            if (!isCardFlipped) {
                Button(
                    onClick = { isCardFlipped = true },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary
                    )
                ) {
                    Icon(Icons.Default.Visibility, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Mostrar Respuesta")
                }
            }

            // ======= BOTONES DE DIFICULTAD =======
            if (isCardFlipped) {
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Again (Rojo)
                            DifficultyButton(
                                text = "Again",
                                subtitle = "< 1m",
                                color = Color(0xFFD32F2F),
                                modifier = Modifier.weight(1f)
                            ) {
                                nextCard(DifficultyLevel.AGAIN)
                            }

                            // Hard (Naranja)
                            DifficultyButton(
                                text = "Hard",
                                subtitle = "< 6m",
                                color = Color(0xFFFF9800),
                                modifier = Modifier.weight(1f)
                            ) {
                                nextCard(DifficultyLevel.HARD)
                            }

                            // Good (Verde)
                            DifficultyButton(
                                text = "Good",
                                subtitle = "< 10m",
                                color = Color(0xFF4CAF50),
                                modifier = Modifier.weight(1f)
                            ) {
                                nextCard(DifficultyLevel.GOOD)
                            }

                            // Easy (Azul)
                            DifficultyButton(
                                text = "Easy",
                                subtitle = "4d",
                                color = Color(0xFF2196F3),
                                modifier = Modifier.weight(1f)
                            ) {
                                nextCard(DifficultyLevel.EASY)
                            }
                        }
                    }
                }
            }
        }
    }

    // ======= DIALOG SELECTOR DE MODO =======
    if (showModeSelector) {
        AlertDialog(
            onDismissRequest = { showModeSelector = false },
            title = { Text("Seleccionar Modo de Estudio") },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    StudyMode.values().forEach { mode ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    studyMode = mode
                                    showModeSelector = false
                                    // Resetear estado
                                    isCardFlipped = false
                                    drawnPaths = emptyList()
                                }
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = studyMode == mode,
                                onClick = null
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = when(mode) {
                                        StudyMode.CHARACTER_TO_PINYIN -> "Carácter a Pinyin"
                                        StudyMode.PINYIN_TO_CHARACTER -> "Pinyin a Carácter"
                                        StudyMode.MEANING_TO_CHARACTER -> "Significado a Carácter"
                                    },
                                    style = MaterialTheme.typography.titleSmall
                                )
                                Text(
                                    text = when(mode) {
                                        StudyMode.CHARACTER_TO_PINYIN -> "Ve el carácter y responde con pinyin"
                                        StudyMode.PINYIN_TO_CHARACTER -> "Ve el pinyin y dibuja el carácter"
                                        StudyMode.MEANING_TO_CHARACTER -> "Ve el significado y dibuja el carácter"
                                    },
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showModeSelector = false }) {
                    Text("Cerrar")
                }
            }
        )
    }
}

@Composable
private fun DifficultyButton(
    text: String,
    subtitle: String,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = color,
            contentColor = Color.White
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}