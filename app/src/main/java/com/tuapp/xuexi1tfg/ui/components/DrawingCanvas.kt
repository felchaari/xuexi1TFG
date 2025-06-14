// DrawingCanvas.kt – versión limpia y CORREGIDA (sin referencias @Composable fuera de contexto)
package com.tuapp.xuexi1tfg.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Undo
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// =====================================
//  Modelo de datos
// =====================================

data class DrawingStroke(
    val points: List<Offset>,
    val strokeWidth: Float,
    val color: Color
)

// =====================================
//  Composable principal
// =====================================

@Composable
fun DrawingCanvas(
    modifier: Modifier = Modifier,
    enabled: Boolean
) {
    /** ESTADOS */
    var currentStroke by remember { mutableStateOf<DrawingStroke?>(null) }
    val strokes = remember { mutableStateListOf<DrawingStroke>() }
    var isDrawing by remember { mutableStateOf(false) }

    /** Config del trazo (¡SE OBTIENE EN CONTEXTO COMPOSABLE!) */
    val strokeWidth = 6f
    val strokeColor = MaterialTheme.colorScheme.onSurface

    Column(modifier = modifier.fillMaxSize()) {

        // ====== Área de dibujo ======
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(16.dp)
                .background(Color.White, RoundedCornerShape(8.dp))
                .border(2.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(8.dp))
                .clipToBounds()
        ) {
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(4.dp)
                    .pointerInput(Unit) {
                        detectDragGestures(
                            onDragStart = { offset ->
                                isDrawing = enabled
                                currentStroke = DrawingStroke(
                                    points = listOf(offset),
                                    strokeWidth = strokeWidth,
                                    color = strokeColor // ✅ ya no invoca @Composable aquí
                                )
                            },
                            onDrag = { change, _ ->
                                currentStroke = currentStroke?.copy(
                                    points = currentStroke!!.points + change.position
                                )
                            },
                            onDragEnd = {
                                currentStroke?.let { stroke ->
                                    if (stroke.points.size > 1) {
                                        strokes.add(stroke)
                                    }
                                }
                                currentStroke = null
                                isDrawing = false
                            }
                        )
                    }
            ) {
                drawMinimalGuides()
                strokes.forEach { drawStroke(it) }
                currentStroke?.let { drawStroke(it) }
            }

            // Indicador ✍
            if (isDrawing) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(12.dp)
                        .background(
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                            CircleShape
                        )
                        .padding(6.dp)
                ) {
                    Text("✍️", fontSize = 14.sp)
                }
            }
        }

        // ====== Botones inferiores ======
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ElevatedButton(
                onClick = {
                    if (strokes.isNotEmpty()) {
                        strokes.removeAt(strokes.lastIndex)
                    }
                },
                enabled = strokes.isNotEmpty()
            ) {
                Icon(Icons.Default.Undo, contentDescription = "Deshacer")
                Spacer(Modifier.width(8.dp))
                Text("Deshacer")
            }

            ElevatedButton(
                onClick = {
                    strokes.clear()
                    currentStroke = null
                }
            ) {
                Icon(Icons.Default.Delete, contentDescription = "Limpiar")
                Spacer(Modifier.width(8.dp))
                Text("Limpiar")
            }
        }

        // Info de número de trazos
        if (strokes.isNotEmpty()) {
            Text(
                text = "${strokes.size} trazo${if (strokes.size == 1) "" else "s"}",
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 8.dp),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
            )
        }
    }
}

// =====================================
//  Funciones de dibujo auxiliares
// =====================================

private fun DrawScope.drawMinimalGuides() {
    val guideColor = Color.Gray.copy(alpha = 0.15f)
    val strokeW = 0.5.dp.toPx()
    drawLine(guideColor, Offset(0f, size.height / 2), Offset(size.width, size.height / 2), strokeW)
    drawLine(guideColor, Offset(size.width / 2, 0f), Offset(size.width / 2, size.height), strokeW)
}

private fun DrawScope.drawStroke(stroke: DrawingStroke) {
    if (stroke.points.size < 2) return

    val path = Path().apply {
        moveTo(stroke.points.first().x, stroke.points.first().y)
        for (i in 1 until stroke.points.size) {
            val prev = stroke.points[i - 1]
            val curr = stroke.points[i]
            val midX = (prev.x + curr.x) / 2
            val midY = (prev.y + curr.y) / 2
            if (i == 1) lineTo(midX, midY) else quadraticBezierTo(prev.x, prev.y, midX, midY)
        }
        lineTo(stroke.points.last().x, stroke.points.last().y)
    }

    drawPath(
        path,
        stroke.color,
        style = Stroke(
            width = stroke.strokeWidth,
            cap = StrokeCap.Round,
            join = StrokeJoin.Round
        )
    )
}

// =====================================
//  Pantalla wrapper reutilizable
// =====================================

@Composable
fun DrawingScreen() {
    DrawingCanvas(modifier = Modifier.fillMaxSize(), enabled = true)
}
