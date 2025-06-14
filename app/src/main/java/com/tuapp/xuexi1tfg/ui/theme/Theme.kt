package com.tuapp.xuexi1tfg.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF81C784),
    secondary = Color(0xFF2E7D32),
    surface = Color(0xFF121212)
    /*
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80*/
)

private val XuexiLightColorScheme = lightColorScheme(
    primary = Color(0xFF2E7D32), // Verde principal
    secondary = Color(0xFF81C784), // Verde claro
    tertiary = Color(0xFF4CAF50), // Verde intermedio
    surface = Color(0xFFF8F9FA), // Fondo suave
    onPrimary = Color.White // Texto sobre botones verdes
    /*
    primary = Color(0xFF5D9CEC),  // Azul principal suave
    secondary = Color(0xFFA0C4FF),  // Azul claro
    tertiary = Color(0xFFDFF6FF),   // Azul muy claro
    background = Color(0xFFF5F9FF), // Fondo azul blanquecino
    surface = Color(0xFFFFFFFF),    // Superficies blancas
    onPrimary = Color(0xFFFFFFFF),
    onSecondary = Color(0xFF000000),
    onTertiary = Color(0xFF000000),
    onBackground = Color(0xFF333333), // Texto oscuro pero suave
    onSurface = Color(0xFF333333),
    */
    )

@Composable
fun Xuexi1TFGTheme(
    // Parámetro que determina si el tema es oscuro (por defecto sigue el sistema)
    darkTheme: Boolean = isSystemInDarkTheme(),
    //Contenido al que se le aplicara el tema
    content: @Composable () -> Unit
) {
    val colorScheme =  if (darkTheme) DarkColorScheme else XuexiLightColorScheme

    MaterialTheme(
        colorScheme = colorScheme, //este es el esquema de colores seleccionado
        typography = Typography, //Esto es Tipografia definida, que aun no esta definida
        content = content //Contenido que heredará todos estos chupiestilos ;)
    )
}