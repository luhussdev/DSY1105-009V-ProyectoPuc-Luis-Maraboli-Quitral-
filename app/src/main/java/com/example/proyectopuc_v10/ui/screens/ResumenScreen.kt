package com.example.proyectopuc_v10.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.proyectopuc_v10.viewmodel.UsuarioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResumenScreen(
    viewModel: UsuarioViewModel = viewModel(),
    onVolverInicio: () -> Unit = {},
    onIrAPerfil: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("CIM UC - Confirmación") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "¡Solicitud Registrada con Éxito!",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Text(
                text = "Hemos recibido tus datos para coordinar iniciativas I+D con el Centro UC de Innovación en Madera.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Detalles de la Solicitud",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    HorizontalDivider()

                    Text(
                        text = "Solicitante: ${uiState.nombre}",
                        style = MaterialTheme.typography.bodyLarge
                    )

                    Text(
                        text = "Correo: ${uiState.correo}",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Text(
                        text = "Organización: ${uiState.organizacion}",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Text(
                        text = "Área I+D: ${uiState.areaInteres}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = onVolverInicio,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Volver al Inicio")
            }

            OutlinedButton(
                onClick = onIrAPerfil,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Ver Mi Perfil")
            }
        }
    }
}