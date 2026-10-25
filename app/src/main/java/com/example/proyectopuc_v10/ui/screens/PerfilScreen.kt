package com.example.proyectopuc_v10.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
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

    // Recolectamos el usuario desde la base de datos de Room
    val usuario by viewModel.usuarioState.collectAsState()

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
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Perfil de Usuario",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Componentes de la foto de perfil
            ImagenInteligente(imagenUri = imagenUri)

            Spacer(modifier = Modifier.height(16.dp))

            // Botón de imagen
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { galeriaLauncher.launch("image/*") },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Galeria")
                }

                OutlinedButton(
                    onClick = {
                        val uri = viewModel.crearUriTemporal(context)
                        tempCameraUri = uri
                        camaraLauncher.launch(uri)
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Cámara")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Datos persistidos cargados desde Room
            usuario?.let { u ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Correo: ${u.correo}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = "Organización: ${u.organizacion}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = "Área de Interés: ${u.areaInteres}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            } ?: run {
                CircularProgressIndicator()
                Spacer(modifier = Modifier.height(8.dp))
                Text("Cargando información del usuario...")
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Botón para volver a la pantalla anterior
            TextButton(onClick = onVolver) {
                Text("Volver")
            }
        }
    }
}