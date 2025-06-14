package com.tuapp.xuexi1tfg.ui.components

// Imports necesarios para el SearchBar
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp

/**
 * Composable que crea una barra de búsqueda personalizada para buscar caracteres hanzi.
 * Incluye funcionalidades como placeholder, iconos, limpieza de texto y acción de búsqueda.
 *
 * @param query El texto actual de búsqueda
 * @param onQueryChange Función que se llama cuando cambia el texto de búsqueda
 * @param onSearch Función que se ejecuta cuando se presiona el botón de búsqueda
 */
@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit
) {
    TextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier
            .fillMaxWidth() // Ocupa todo el ancho disponible
            .padding(bottom = 16.dp), // Espaciado inferior de 16dp
        placeholder = {
            Text("Buscar hanzi (ej. 你好, nǐ hǎo)") // Texto de ayuda que se muestra cuando está vacío
        },
        leadingIcon = {
            // Icono de búsqueda al inicio del campo
            Icon(Icons.Default.Search, contentDescription = "Buscar")
        },
        trailingIcon = {
            // Icono de limpiar al final del campo (solo si hay texto)
            if (query.isNotEmpty()) {
                IconButton(onClick = { onQueryChange("") }) {
                    Icon(Icons.Default.Close, contentDescription = "Limpiar")
                }
            }
        },
        // Configuración del teclado para mostrar botón de búsqueda
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        // Acción que se ejecuta cuando se presiona el botón de búsqueda del teclado
        keyboardActions = KeyboardActions(onSearch = { onSearch() }),
        // Forma redondeada para el campo de texto
        shape = MaterialTheme.shapes.extraLarge,
        // Colores personalizados para el campo de texto
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant, // Color cuando está enfocado
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant // Color cuando no está enfocado
        )
    )
}