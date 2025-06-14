package com.tuapp.xuexi1tfg.ui.components

// Imports para las funciones de layout y composición
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape

// Imports para los iconos de Material Design
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material.icons.filled.Mic

// Imports para los componentes de Material 3
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text

// Imports para Compose runtime y UI
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

/**
 * Composable que muestra una fila con tres botones de métodos de búsqueda
 * para la aplicación de estudio de chino: dibujo, voz e imagen.
 *
 * @param onDrawClick Función que se ejecuta al pulsar el botón de dibujo
 * @param onVoiceClick Función que se ejecuta al pulsar el botón de voz
 * @param onCameraClick Función que se ejecuta al pulsar el botón de imagen
 */
@Composable
fun SearchMethodButtons(
    onDrawClick: () -> Unit,
    onVoiceClick: () -> Unit,
    onCameraClick: () -> Unit
) {
    // Contenedor horizontal que distribuye los botones uniformemente
    Row(
        modifier = Modifier
            .fillMaxWidth() // Ocupa todo el ancho disponible
            .padding(vertical = 16.dp), // Espaciado vertical de 16dp arriba y abajo
        horizontalArrangement = Arrangement.SpaceEvenly // Distribuye los elementos con espacio igual entre ellos
    ) {
        // Botón de dibujo - permite buscar caracteres dibujándolos
        MethodButton(
            icon = Icons.Default.Brush, // Icono de pincel
            label = "Dibujo",
            onClick = onDrawClick,
            color = MaterialTheme.colorScheme.primary // Color primario del tema
        )
        MethodButton(
            icon = Icons.Default.Keyboard, // Icono de pincel
            label = "Texto",
            onClick = onDrawClick,
            color = MaterialTheme.colorScheme.primary // Color primario del tema
        )

        // Botón de imagen - permite buscar caracteres desde una foto
        MethodButton(
            icon = Icons.Default.CameraAlt, // Icono de cámara
            label = "Imagen",
            onClick = onCameraClick,
            color = MaterialTheme.colorScheme.primary
        )

        // Botón de voz - permite buscar mediante reconocimiento de voz
        MethodButton(
            icon = Icons.Default.Mic, // Icono de micrófono
            label = "Voz",
            onClick = onVoiceClick,
            color = MaterialTheme.colorScheme.primary
        )


    }
}

/**
 * Composable que crea un botón individual con icono circular y etiqueta.
 * Usado para los diferentes métodos de búsqueda de caracteres chinos.
 *
 * @param icon El icono vectorial que se mostrará en el botón
 * @param label El texto que aparecerá debajo del icono
 * @param onClick Función que se ejecuta cuando se pulsa el botón
 * @param color Color del icono y del borde del botón
 */
@Composable
fun MethodButton(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
    color: Color
) {
    // Contenedor vertical que apila el icono y la etiqueta
    Column(
        horizontalAlignment = Alignment.CenterHorizontally, // Centra los elementos horizontalmente
        modifier = Modifier.clickable { onClick() } // Hace que toda la columna sea clickeable
    ) {
        // Contenedor circular para el icono
        Box(
            contentAlignment = Alignment.Center, // Centra el icono dentro del círculo
            modifier = Modifier
                .size(64.dp) // Tamaño del círculo: 64x64 dp
                .background(
                    color.copy(alpha = 0.1f), // Fondo con el color primario al 10% de opacidad
                    CircleShape // Forma circular
                )
                .border(1.dp, color, CircleShape) // Borde de 1dp con el color primario
        ) {
            // Icono dentro del círculo
            Icon(
                imageVector = icon,
                contentDescription = label, // Descripción para accesibilidad
                tint = color, // Color del icono
                modifier = Modifier.size(24.dp) // Tamaño del icono: 24x24 dp
            )
        }

        // Separador entre el icono y la etiqueta
        Spacer(modifier = Modifier.height(4.dp)) // Espacio de 4dp

        // Texto de la etiqueta debajo del icono
        Text(
            text = label,
            color = MaterialTheme.colorScheme.onSurface, // Color del texto según el tema
            style = MaterialTheme.typography.labelMedium // Estilo de tipografía para etiquetas
        )
    }
}