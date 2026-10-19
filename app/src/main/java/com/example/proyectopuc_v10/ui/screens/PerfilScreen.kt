package com.example.proyectopuc_v10.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.proyectopuc_v10.ui.components.ImagenInteligente
import com.example.proyectopuc_v10.viewmodel.PerfilViewModel

@Composable
fun PerfilScreen(
    viewModel: PerfilViewModel = viewModel(),
    onVolver: () -> Unit = {}
) {
    val context = LocalContext.current
    val imagenUri by viewModel.imagenUri.collectAsState()

    // Variable para mantener el Uri de la foto que tomará la cámara
    var tempCameraUri by remember { mutableStateOf<Uri?>(null) }

    // Launcher para Galeria (GetContent)
    val galeriaLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            viewModel.actualizarImagen(uri)
        }
    }

    // Launcher para Cámara (TakePicture)
    val camaraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { exito: Boolean ->
        if (exito && tempCameraUri != null) {
            viewModel.actualizarImagen(tempCameraUri)
        }
    }

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Perfil de Usuario",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Componentes Reutilizable ImagenInteligente
            ImagenInteligente(imagenUri = imagenUri)

            Spacer(modifier = Modifier.height(32.dp))

            // Botón para Galeria
            Button(
                onClick = { galeriaLauncher.launch("image/*") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Seleccionar desde Galeria")
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Botón para Cámara
            OutlinedButton(
                onClick = {
                    val uri = viewModel.crearUriTemporal(context)
                    tempCameraUri = uri
                    camaraLauncher.launch(uri)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Tomar Foto con Cámara")
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Botón para volver a la pantalla anterior
            TextButton(onClick = onVolver) {
                Text("Volver")
            }
        }
    }
}