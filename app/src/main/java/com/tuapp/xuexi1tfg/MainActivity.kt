package com.tuapp.xuexi1tfg

import android.app.appsearch.SearchResult
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.tuapp.xuexi1tfg.ui.screens.DetailScreen
import com.tuapp.xuexi1tfg.ui.screens.FlashCardScreen
import com.tuapp.xuexi1tfg.ui.screens.WelcomeScreen
import com.tuapp.xuexi1tfg.ui.screens.SearchScreen
import com.tuapp.xuexi1tfg.ui.theme.Xuexi1TFGTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * Actividad principal de la aplicación de estudio de chino.
 * Configura el tema, la estructura básica y habilita el modo edge-to-edge.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // Habilita el modo edge-to-edge (barras de sistema transparentes)

        setContent {
            Xuexi1TFGTheme {
                // Scaffold proporciona una estructura básica de Material Design
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // Surface es un contenedor que sigue el esquema de color del tema
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        HanziFlowApp()
                    }
                }
            }
        }
    }
}

/**
 * Composable principal de la aplicación que configura la navegación
 * y los callbacks principales para las diferentes funcionalidades.
 */
@Composable
fun HanziFlowApp() {
    WelcomeScreen()
}

@Composable
fun WelcomeScreen() {
    TODO("Not yet implemented")
}

/**
 * Previews para ver la aplicación en modo claro y oscuro
 * durante el desarrollo sin necesidad de compilar.
 */
@Preview(showBackground = true, name = "Modo Claro")
@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    name = "Modo Oscuro"
)
@Composable
fun HanziFlowAppPreview() {
    Xuexi1TFGTheme {
        HanziFlowApp()
    }
}