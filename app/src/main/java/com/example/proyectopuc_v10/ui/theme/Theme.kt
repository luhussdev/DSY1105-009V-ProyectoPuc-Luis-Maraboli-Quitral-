package com.example.proyectopuc_v10.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorSchema = lightColorScheme(
    primary = AzulPrimario,
    onPrimary = Blanco,
    primaryContainer = AzulInstitucional,
    onPrimaryContainer = Blanco,
    secondary = AmarilloAcento,
    onSecondary = AzulInstitucional,
    background = GrisFondo,
    onBackground = AzulInstitucional,
    surface = Blanco,
    onSurface = AzulInstitucional
)

@Composable
fun ProyectoPuc_V10Theme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorSchema,
        typography = Typography,
        content = content
    )
}
