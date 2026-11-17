package com.example.proyectopuc_v10.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.proyectopuc_v10.ui.components.ImagenInteligente
import com.example.proyectopuc_v10.ui.theme.AzulInstitucional
import com.example.proyectopuc_v10.ui.theme.Blanco
import com.example.proyectopuc_v10.viewmodel.PerfilViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilScreen(
    viewModel: PerfilViewModel = viewModel(),
    onVolver: () -> Unit = {}
) {
    val context = LocalContext.current
    val imagenUri by viewModel.imagenUri.collectAsState()
    val usuario by viewModel.usuarioState.collectAsState()
    var tempCameraUri by remember { mutableStateOf<Uri?>(null) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Estados editables inicializados cuando carga el usuario desde Room
    var correo by remember { mutableStateOf("") }
    var organizacion by remember { mutableStateOf("") }
    var areaInteres by remember { mutableStateOf("") }

    LaunchedEffect(usuario) {
        usuario?.let { u ->
            correo = u.correo
            organizacion = u.organizacion
            areaInteres = u.areaInteres
        }
    }

    val galeriaLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            viewModel.actualizarImagen(uri)
        }
    }

    val camaraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { exito: Boolean ->
        if (exito && tempCameraUri != null) {
            viewModel.actualizarImagen(tempCameraUri)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("CIM UC - Editar Perfil") },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = Blanco
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AzulInstitucional,
                    titleContentColor = Blanco
                )
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ImagenInteligente(imagenUri = imagenUri)

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { galeriaLauncher.launch("image/*") },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.PhotoLibrary, contentDescription = "Galería")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Galería")
                }

                OutlinedButton(
                    onClick = {
                        val uri = viewModel.crearUriTemporal(context)
                        tempCameraUri = uri
                        camaraLauncher.launch(uri)
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.PhotoCamera, contentDescription = "Cámara")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Cámara")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (usuario != null) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedTextField(
                        value = correo,
                        onValueChange = { correo = it },
                        label = { Text("Correo electrónico") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = organizacion,
                        onValueChange = { organizacion = it },
                        label = { Text("Organización / Institución") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = areaInteres,
                        onValueChange = { areaInteres = it },
                        label = { Text("Área de Interés") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = {
                            viewModel.actualizarPerfil(correo, organizacion, areaInteres)
                            scope.launch {
                                snackbarHostState.showSnackbar("Perfil actualizado correctamente")
                            }
                        },
                        enabled = correo.isNotBlank() && organizacion.isNotBlank(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    ) {
                        Icon(Icons.Default.Save, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Guardar Cambios")
                    }
                }
            } else {
                CircularProgressIndicator()
                Spacer(modifier = Modifier.height(8.dp))
                Text("Cargando información del usuario...")
            }
        }
    }
}