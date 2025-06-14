package com.tuapp.xuexi1tfg.ui.components

// Imports necesarios para este componente
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Composable que muestra una sección con los resultados de búsqueda recientes.
 * Incluye un título y una lista de caracteres hanzi en formato de chips.
 */
@OptIn(ExperimentalLayoutApi::class) // FlowRow es experimental
@Composable
fun SearchResultsSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth() // Ocupa todo el ancho disponible
            .padding(16.dp) // Padding de 16dp en todos los lados
    ) {
        // Título de la sección
        Text(
            text = "Resultados recientes",
            style = MaterialTheme.typography.titleMedium, // Estilo de título mediano
            color = MaterialTheme.colorScheme.primary, // Color primario del tema
            modifier = Modifier.padding(bottom = 8.dp) // Espaciado inferior de 8dp
        )

        // Lista de caracteres hanzi en formato de flujo (se adapta al ancho)
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp) // Espacio de 8dp entre elementos
        ) {
            // Lista de ejemplo de caracteres hanzi (se conectará a Room más adelante)
            listOf("视", "看", "示", "盾").forEach { hanzi ->
                HanziChip(
                    hanzi = hanzi,
                    onClick = { /* TODO: Implementar navegación a detalles del hanzi */ }
                )
            }
        }
    }
}

/**
 * Composable que crea un chip circular con un carácter hanzi.
 * Se usa para mostrar caracteres chinos de forma visual y clickeable.
 *
 * @param hanzi El carácter chino que se mostrará
 * @param onClick Función que se ejecuta cuando se pulsa el chip
 */
@Composable
fun HanziChip(hanzi: String, onClick: () -> Unit) {
    Surface(
        shape = CircleShape, // Forma circular
        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f), // Fondo con color primario al 10%
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary), // Borde de 1dp con color primario
        modifier = Modifier.clickable { onClick() } // Hace el chip clickeable
    ) {
        Text(
            text = hanzi,
            style = MaterialTheme.typography.headlineMedium, // Estilo de texto grande para el hanzi
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp), // Padding interno del chip
            color = MaterialTheme.colorScheme.onSurface // Color del texto según el tema
        )
    }
}