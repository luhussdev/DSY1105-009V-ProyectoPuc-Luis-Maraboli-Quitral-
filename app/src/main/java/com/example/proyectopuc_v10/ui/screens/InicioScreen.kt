package com.example.proyectopuc_v10.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Article
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.ManageAccounts
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.proyectopuc_v10.ui.theme.AzulInstitucional
import com.example.proyectopuc_v10.ui.theme.Blanco
import com.example.proyectopuc_v10.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InicioScreen(
    authViewModel: AuthViewModel,
    onIrANoticias: () -> Unit,
    onIrAForo: () -> Unit,
    onIrAEditarPerfil: () -> Unit,
    onCerrarSesion: () -> Unit
) {
    val usuarioActual by authViewModel.usuarioActual.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("CIM UC - Panel Principal") },
                actions = {
                    IconButton(onClick = {
                        authViewModel.cerrarSesion()
                        onCerrarSesion()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = "Cerrar sesión",
                            tint = Blanco
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AzulInstitucional,
                    titleContentColor = Blanco
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Tarjeta de bienvenida de sesión activa
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Hola, ${usuarioActual?.nombre ?: "Investigador"}",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = usuarioActual?.especialidad ?: "Sesión activa en CIM UC",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            Text(
                text = "Secciones Disponibles",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground
            )

            // Acceso a Pantalla 4: Noticias y Proyectos I+D
            ModuloCard(
                titulo = "Las Noticias y Proyectos I+D",
                descripcion = "Consulta novedades, capacitaciones CLT y normativas vigentes.",
                icono = Icons.Default.Article,
                onClick = onIrANoticias
            )

            // Acceso a Pantalla 5: Foro Técnico (API REST)
            ModuloCard(
                titulo = "Foro Técnico",
                descripcion = "Publica, edita y comenta aportes técnicos de la comunidad en línea.",
                icono = Icons.Default.Forum,
                onClick = onIrAForo
            )

            // Acceso a Pantalla 6: Editar Perfil
            ModuloCard(
                titulo = "Editar Perfil",
                descripcion = "Administra tu información personal y preferencias de usuario.",
                icono = Icons.Default.ManageAccounts,
                onClick = onIrAEditarPerfil
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Botón Cerrar Sesión -> Regresa a Pantalla 1 (Login)
            OutlinedButton(
                onClick = {
                    authViewModel.cerrarSesion()
                    onCerrarSesion()
                },
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.error
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Cerrar Sesión")
            }
        }
    }
}

@Composable
private fun ModuloCard(
    titulo: String,
    descripcion: String,
    icono: ImageVector,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icono,
                contentDescription = titulo,
                tint = AzulInstitucional,
                modifier = Modifier.size(36.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = titulo,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = descripcion,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}