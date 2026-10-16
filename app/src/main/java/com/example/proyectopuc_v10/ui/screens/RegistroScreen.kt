package com.example.proyectopuc_v10.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.proyectopuc_v10.ui.components.AceptarTerminos
import com.example.proyectopuc_v10.ui.components.CampoText
import com.example.proyectopuc_v10.viewmodel.UsuarioViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroScreen(
    viewModel: UsuarioViewModel = viewModel(),
    onSolicitudExitosa: () -> Unit = {},
    onVolver: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("CIM UC - Solicitud de Contacto") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { innerPading ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPading)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Formulario de Vinculación Institucional",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Text(
                text = "Ingresa tus datos para coordinar oportunidades de colaboración o proyectos I+D con el Centro.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))

            CampoText(
                value = uiState.nombre,
                onValueChange = { viewModel.onNombreCambiado(it) },
                label = "Nombre Completo",
                errorMessage = uiState.errores.nombre
            )

            CampoText(
                value = uiState.correo,
                onValueChange = { viewModel.onCorreoCambiado(it) },
                label = "Correo Institucional",
                errorMessage = uiState.errores.correo
            )

            CampoText(
                value = uiState.organizacion,
                onValueChange = { viewModel.onOrganizacionCambiada(it) },
                label = "Empresa / Universidad / Institución",
                errorMessage = uiState.errores.organizacion
            )

            CampoText(
                value = uiState.areaInteres,
                onValueChange = { viewModel.onAreaInteresCambiada(it) },
                label = "Área I´D de Interés (Ej: Estructura, Normativa)",
                errorMessage = uiState.errores.areaInteres
            )

            AceptarTerminos(
                checked = uiState.aceptaTerminos,
                onCheckedChange = { viewModel.onTerminosCambiados(it) },
                errorMessage = uiState.errores.aceptaTerminos
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (viewModel.validarFormulario()) {
                        onSolicitudExitosa()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Enviar Solicitud")
            }

            OutlinedButton(
                onClick = onVolver,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Volver al Menú Principal")
            }
        }
    }
}