package com.example.proyectopuc_v10.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.proyectopuc_v10.ui.components.AceptarTerminos
import com.example.proyectopuc_v10.ui.components.CampoTexto
import com.example.proyectopuc_v10.viewmodel.UsuarioViewModel
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue

@Composable
fun RegistroScreen(
    viewModel: UsuarioViewModel,
    onNavegarResumen: () -> Unit
) {
    val estado by viewModel.estado.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope ()

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Registro de Usuario",
                style = MaterialTheme.typography.headlineMedium
            )

            // Campos reutilizados conectados al ViweModel
            CampoTexto(
                valor = estado.nombre,
                onValueChange = viewModel::onNombreChange,
                label = "Nombre de usuario",
                error = estado.errores.nombre
            )

            CampoTexto(
                valor =  estado.correo,
                onValueChange = viewModel::onCorreoChange,
                label = "Correo electrónico",
                error = estado.errores.correo
            )

            CampoTexto(
                valor = estado.clave,
                onValueChange = viewModel::onClaveChange,
                label = "Contraseña",
                esPassword = true,
                error = estado.errores.clave
            )

            CampoTexto(
                valor = estado.direccion,
                onValueChange = viewModel::onDireccionChange,
                label = "Dirección",
                error = estado.errores.direccion
            )

            AceptarTerminos(
                aceptado = estado.aceptaTerminos,
                onCheckedChange = viewModel::onAceptaTerminosChange,
                error = estado.errores.aceptaTerminos
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Botón de Envio con validación y Snackbar
            Button(
                onClick = {
                    if (viewModel.validarFormulario()) {
                        scope.launch {
                            snackbarHostState.showSnackbar("¡Formulario válido! Redirigiendo...")
                        }
                        onNavegarResumen()
                    } else {
                        scope.launch {
                            snackbarHostState.showSnackbar("Por favor, corrige los errores señalados.")
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Registrar")
            }
        }
    }
}