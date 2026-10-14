package com.example.proyectopuc_v10.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.proyectopuc_v10.viewmodel.UsuarioViewModel
import androidx.compose.runtime.getValue

@Composable
fun ResumenScreen(
    viewModel: UsuarioViewModel,
    onVolver: () -> Unit,
    onIrAAnimaciones: () -> Unit = {},
    onIrAPerfil: () -> Unit = {}
) {
    val estado by viewModel.estado.collectAsState()

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Resumen del Registro",
                style = MaterialTheme.typography.headlineMedium
            )

            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(text = "Nombre: ${estado.nombre}", style = MaterialTheme.typography.bodyLarge)
                    Text(text = "Correo: ${estado.correo}", style = MaterialTheme.typography.bodyLarge)
                    Text(text = "Dirección: ${estado.direccion}", style = MaterialTheme.typography.bodyLarge)
                    Text(
                        text = "Términos aceptados: ${if (estado.aceptaTerminos) "Si" else "No"}",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            OutlinedButton(
                onClick = {
                    viewModel.limpiarFormulario()
                    onVolver()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Volver al Inicio")
            }
            Button(
                onClick = onIrAAnimaciones,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Ver Estado y Animaciones")
            }

            Button(
                onClick = onIrAPerfil,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Ir a Mi Perfil (Cámara / Galería)")
            }
        }
    }
}