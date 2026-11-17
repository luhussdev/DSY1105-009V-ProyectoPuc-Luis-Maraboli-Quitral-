package com.example.proyectopuc_v10.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorSchema = lightColorScheme(
    primary = AzulPrimario,
    onPrimary = Blanco,
    primaryContainer = AzulClaroUC,
    onPrimaryContainer = AzulInstitucional,
    secondary = AmarilloAcento,
    onSecondary = GrisTituloUC,
    background = GrisFondo,
    onBackground = GrisTituloUC,
    surface = Blanco,
    onSurface = GrisTituloUC,
    surfaceVariant = TarjetaWebUC,
    onSurfaceVariant = GrisTextoUC,
    outline = BordeWebUC
)

@Composable
fun ProyectoPuc_V10Theme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorSchema,
        typography = Typography,
        content = content
    )
}