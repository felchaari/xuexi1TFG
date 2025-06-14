package com.tuapp.xuexi1tfg.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import com.tuapp.xuexi1tfg.ui.components.DrawingCanvas // Asegúrate de que esta ruta sea correcta
import kotlinx.coroutines.launch

// ======================================================================================
// CLASES DE DATOS
// ======================================================================================

data class HanziSearchResult(
    val character: String,
    val pinyin: String,
    val meaning: String,
    val strokes: Int,
    val hskLevel: Int,
    val frequency: String
)

// ======================================================================================
// FUNCIONES AUXILIARES
// ======================================================================================

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
        ),
        HanziSearchResult(
            character = "中文",
            pinyin = "zhōngwén",
            meaning = "idioma chino",
            strokes = 8,
            hskLevel = 1,
            frequency = "Alta"
        ),
        HanziSearchResult(
            character = "汉字",
            pinyin = "hànzì",
            meaning = "carácter chino",
            strokes = 10,
            hskLevel = 2,
            frequency = "Media"
        ),
        HanziSearchResult(
            character = "你好",
            pinyin = "nǐhǎo",
            meaning = "hola",
            strokes = 7,
            hskLevel = 1,
            frequency = "Alta"
        )
    )
}

// ======================================================================================
// PANTALLA PRINCIPAL: SearchScreen
// ======================================================================================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    // Parámetros para la navegación o acciones de alto nivel, si los necesitas
    onNavigateToDetails: (String) -> Unit = {}, // Ejemplo: para cuando se haga clic en un hanzi
) {
    // Estados para controlar la interfaz de búsqueda
    var isDrawingMode by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf("") }
    var isSearchBarActive by remember { mutableStateOf(false) } // Controla el estado "activo" del SearchBar
    val focusManager = LocalFocusManager.current

    // Estado para controlar los resultados de la búsqueda
    var searchResults by remember { mutableStateOf<List<HanziSearchResult>>(emptyList()) }
    var isShowingResults by remember { mutableStateOf(false) } // Nuevo estado para alternar vistas

    // *** Estados y scope para el Navigation Drawer ***
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope() // Crea un CoroutineScope para abrir/cerrar el drawer

    // Función para manejar la búsqueda real
    val performSearch: (String) -> Unit = { query ->
        // Aquí iría tu lógica de búsqueda real, por ahora usamos los ejemplos
        searchResults = if (query.isNotBlank()) {
            getSampleResults().filter {
                it.character.contains(query, ignoreCase = true) ||
                        it.pinyin.contains(query, ignoreCase = true) ||
                        it.meaning.contains(query, ignoreCase = true)
            }
        } else {
            emptyList()
        }
        isShowingResults = true // Mostrar resultados
        isSearchBarActive = false // Desactivar SearchBar
        focusManager.clearFocus() // Limpiar foco
    }

    // *** ModalNavigationDrawer envuelve todo el contenido de la pantalla ***
    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet {
                Text(
                    "Mazos de estudio",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(16.dp)
                )
                Divider()
                Spacer(modifier = Modifier.height(8.dp))

                NavigationDrawerItem(
                    label = { Text("Mazo HSK 1") },
                    selected = false,
                    onClick = { scope.launch { drawerState.close() } /* Lógica HSK 1 */ },
                    icon = { Icon(Icons.Default.List, contentDescription = "HSK 1") }
                )
                NavigationDrawerItem(
                    label = { Text("Mazo HSK 2") },
                    selected = false,
                    onClick = { scope.launch { drawerState.close() } /* Lógica HSK 2 */ },
                    icon = { Icon(Icons.Default.List, contentDescription = "HSK 2") }
                )
                // Puedes añadir más niveles HSK aquí...

                Spacer(modifier = Modifier.height(16.dp))
                Divider()
                Spacer(modifier = Modifier.height(8.dp))

                NavigationDrawerItem(
                    label = { Text("Crear Nuevo Mazo") },
                    selected = false,
                    onClick = { scope.launch { drawerState.close() } /* Lógica para abrir la pantalla de creación de mazo */ },
                    icon = { Icon(Icons.Default.AddCircle, contentDescription = "Crear Mazo") }
                )

                Spacer(modifier = Modifier.height(8.dp))

                NavigationDrawerItem(
                    label = { Text("Mis Caracteres Guardados") },
                    selected = false,
                    onClick = { scope.launch { drawerState.close() } /* Lógica para abrir el mazo de caracteres guardados */ },
                    icon = { Icon(Icons.Default.Favorite, contentDescription = "Caracteres Guardados") }
                )
            }
        },
        drawerState = drawerState,
        gesturesEnabled = drawerState.isOpen // Permite cerrar el drawer con gestos solo si está abierto
    ) {
        // *** Contenido principal de tu SearchScreen ***
        Scaffold( // Usamos Scaffold para tener un TopAppBar consistente
            topBar = {
                if (!isShowingResults) { // Mostrar esta TopAppBar si no estamos en resultados
                    TopAppBar(
                        title = { Text("Buscar") },
                        navigationIcon = {
                            IconButton(onClick = {
                                scope.launch {
                                    drawerState.apply { if (isClosed) open() else close() }
                                }
                            }) {
                                Icon(Icons.Default.Menu, contentDescription = "Menú")
                            }
                        }
                    )
                } else { // Mostrar esta TopAppBar si estamos mostrando resultados
                    TopAppBar(
                        title = {
                            Column {
                                Text(
                                    text = "Search Results",
                                    style = MaterialTheme.typography.headlineSmall
                                )
                                Text(
                                    text = "${searchResults.size} resultados para \"$searchText\"",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        },
                        navigationIcon = {
                            IconButton(onClick = {
                                isShowingResults = false // Volver a la interfaz de búsqueda
                                searchText = "" // Limpiar búsqueda
                                searchResults = emptyList() // Limpiar resultados
                            }) {
                                Icon(
                                    imageVector = Icons.Default.ArrowBack,
                                    contentDescription = "Volver a búsqueda"
                                )
                            }
                        },
                        actions = {
                            IconButton(onClick = {
                                isShowingResults = false // Volver a la interfaz de búsqueda
                                searchText = "" // Limpiar búsqueda
                                searchResults = emptyList() // Limpiar resultados
                                isSearchBarActive = true // Activar SearchBar para nueva búsqueda
                            }) {
                                Icon(
                                    Icons.Default.Search,
                                    contentDescription = "Nueva búsqueda"
                                )
                            }
                            IconButton(onClick = { /* Abrir filtros */ }) {
                                Icon(
                                    Icons.Default.FilterList,
                                    contentDescription = "Filtros"
                                )
                            }
                        }
                    )
                }
            }
        ) { paddingValues -> // paddingValues es importante para que el contenido no se solape con el TopAppBar
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues) // Aplica el padding de la TopAppBar
                    .padding(horizontal = 16.dp, vertical = 8.dp) // Padding adicional para el contenido
            ) {
                // Alternar entre la interfaz de búsqueda y la de resultados
                AnimatedVisibility(
                    visible = !isShowingResults,
                    enter = slideInHorizontally(initialOffsetX = { -it }) + fadeIn(),
                    exit = slideOutHorizontally(targetOffsetX = { -it }) + fadeOut(),
                    modifier = Modifier.fillMaxSize() // Asegura que ocupe todo el espacio
                ) {
                    // Contenido de la interfaz de búsqueda
                    Column(modifier = Modifier.fillMaxSize()) { // Necesario para que AnimatedVisibility funcione bien con Column
                        SearchBar(
                            query = searchText,
                            onQueryChange = { searchText = it },
                            onSearch = { performSearch(it) }, // Llama a la función de búsqueda
                            active = isSearchBarActive,
                            onActiveChange = {
                                isSearchBarActive = it
                                if (it) isDrawingMode = false // Desactivar modo dibujo si se activa el SearchBar
                            },
                            placeholder = { Text("Buscar Hanzi, Pinyin o significado") },
                            leadingIcon = {
                                Icon(Icons.Default.Search, contentDescription = null)
                            },
                            trailingIcon = {
                                if (searchText.isNotEmpty()) {
                                    IconButton(onClick = { searchText = "" }) {
                                        Icon(Icons.Default.Clear, contentDescription = "Borrar búsqueda")
                                    }
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            ) {
                                Text("Introduce tu búsqueda...")
                                // Aquí puedes añadir sugerencias de búsqueda o historial
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "Selecciona un método de entrada",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceAround,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    OutlinedIconButton(
                                        onClick = {
                                            isDrawingMode = !isDrawingMode
                                            if (isDrawingMode) {
                                                isSearchBarActive = false
                                                focusManager.clearFocus()
                                            }
                                        },
                                        colors = IconButtonDefaults.outlinedIconButtonColors(
                                            containerColor = if (isDrawingMode) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface,
                                            contentColor = if (isDrawingMode) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
                                        )
                                    ) {
                                        Icon(Icons.Default.Edit, contentDescription = "Dibujar")
                                    }

                                    OutlinedIconButton(
                                        onClick = {
                                            isSearchBarActive = true
                                            isDrawingMode = false
                                            focusManager.clearFocus()
                                        },
                                        colors = IconButtonDefaults.outlinedIconButtonColors(
                                            containerColor = if (isSearchBarActive) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface,
                                            contentColor = if (isSearchBarActive) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
                                        )
                                    ) {
                                        Icon(Icons.Default.Keyboard, contentDescription = "Teclado")
                                    }

                                    OutlinedIconButton(onClick = {
                                        isDrawingMode = false
                                        isSearchBarActive = false
                                        focusManager.clearFocus()
                                    }) {
                                        Icon(Icons.Default.Mic, contentDescription = "Audio")
                                    }

                                    OutlinedIconButton(onClick = {
                                        isDrawingMode = false
                                        isSearchBarActive = false
                                        focusManager.clearFocus()
                                    }) {
                                        Icon(Icons.Default.CameraAlt, contentDescription = "Cámara")
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Card(
                            modifier = Modifier
                                .fillMaxSize()
                                .weight(1f),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                            )
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                this@Card.AnimatedVisibility(
                                    visible = isDrawingMode,
                                    enter = fadeIn() + expandVertically(expandFrom = Alignment.Top),
                                    exit = fadeOut() + shrinkVertically(shrinkTowards = Alignment.Top)
                                ) {
                                    DrawingCanvas(
                                        modifier = Modifier.fillMaxSize(),
                                        enabled = true
                                    )
                                }

                                this@Card.AnimatedVisibility(
                                    visible = !isDrawingMode,
                                    enter = fadeIn() + expandVertically(expandFrom = Alignment.Top),
                                    exit = fadeOut() + shrinkVertically(shrinkTowards = Alignment.Top)
                                ) {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center
                                    ) {
                                        if (isSearchBarActive) {
                                            Text(
                                                text = "El teclado está listo para tu búsqueda.",
                                                style = MaterialTheme.typography.bodyMedium,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        } else if (!isDrawingMode && !isSearchBarActive) {
                                            Text(
                                                text = "Usa la barra de búsqueda o selecciona una herramienta.",
                                                style = MaterialTheme.typography.bodyMedium,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // Contenido de la interfaz de resultados
                AnimatedVisibility(
                    visible = isShowingResults,
                    enter = slideInHorizontally(initialOffsetX = { it }) + fadeIn(),
                    exit = slideOutHorizontally(targetOffsetX = { it }) + fadeOut(),
                    modifier = Modifier.fillMaxSize() // Asegura que ocupe todo el espacio
                ) {
                    SearchResultsContent(
                        searchQuery = searchText,
                        searchResults = searchResults,
                        onCharacterClick = onNavigateToDetails, // Pasa la acción de navegación
                        onNewSearch = {
                            isShowingResults = false // Volver a la interfaz de búsqueda
                            searchText = "" // Limpiar búsqueda
                            searchResults = emptyList() // Limpiar resultados
                            isSearchBarActive = true // Activar SearchBar para nueva búsqueda
                        }
                    )
                }
            }
        }
    }
}

// ======================================================================================
// COMPONENTE: SearchResultsContent (anteriormente ResultsScreen)
// ======================================================================================

@Composable
fun SearchResultsContent(
    searchQuery: String,
    searchResults: List<HanziSearchResult>,
    onCharacterClick: (String) -> Unit,
    onNewSearch: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // No necesitamos TopAppBar aquí, ya que la maneja el Scaffold de SearchScreen
        // El padding de la TopAppBar ya se aplica automáticamente por el Scaffold.

        // ======= CONTENIDO =======
        if (searchResults.isEmpty()) {
            EmptyResultsContent(
                searchQuery = searchQuery,
                onNewSearch = onNewSearch
            )
        } else {
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

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}


// ======================================================================================
// COMPONENTES AUXILIARES
// ======================================================================================

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
            containerColor = MaterialTheme.colorScheme.surfaceContainer // Cambiado a surfaceContainer para un color ligeramente diferente
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

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
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