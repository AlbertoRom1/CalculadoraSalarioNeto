package com.alberto.calculadorasalarioneto.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Esquema de colores verde y negro para toda la app
private val EsquemaVerdeNegro = darkColorScheme(
    primary = VerdePrincipal,          // botones rellenos, slider, campo activo
    onPrimary = Color.Black,           // texto encima de los botones verdes
    secondary = VerdeOscuro,
    onSecondary = Color.Black,
    background = Negro,                // fondo de la pantalla
    onBackground = Color.White,        // texto normal
    surface = Negro,
    onSurface = Color.White,
    surfaceVariant = GrisTarjeta,      // fondo de las tarjetas (vista 2)
    onSurfaceVariant = Color(0xFFB0B0B0),
    outline = VerdePrincipal,          // borde de OutlinedButton y del TextField
    error = Color(0xFFFF5252)          // mensaje de error
)

@Composable
fun CalculadoraSalarioNetoTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = EsquemaVerdeNegro,
        typography = Typography,
        content = content
    )
}