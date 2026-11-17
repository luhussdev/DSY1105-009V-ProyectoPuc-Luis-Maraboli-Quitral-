package com.example.proyectopuc_v10.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.proyectopuc_v10.model.Post
import com.example.proyectopuc_v10.ui.theme.AzulInstitucional
import com.example.proyectopuc_v10.ui.theme.Blanco
import com.example.proyectopuc_v10.viewmodel.PostViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostScreen(
    postViewModel: PostViewModel = viewModel(),
    onVolver: () -> Unit = {}
) {
    val posts by postViewModel.postList.collectAsState()
    val isLoading by postViewModel.isLoading.collectAsState()
    val errorMessage by postViewModel.errorMessage.collectAsState()
    val operationMessage by postViewModel.operationMessage.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    var mostrarDialogoCrear by remember { mutableStateOf(false) }
    var postAEditar by remember { mutableStateOf<Post?>(null) }

    // Mostrar feedback en Snackbar cada vez que se crea, edita o elimina un post
    LaunchedEffect(operationMessage) {
        operationMessage?.let { mensaje ->
            snackbarHostState.showSnackbar(mensaje)
            postViewModel.clearOperationMessage()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("CIM UC - Foro Técnico") },
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
        floatingActionButton = {
            FloatingActionButton(
                onClick = { mostrarDialogoCrear = true },
                containerColor = AzulInstitucional,
                contentColor = Blanco
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Nuevo aporte"
                )
            }
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when {
                isLoading && posts.isEmpty() -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = AzulInstitucional
                    )
                }

                errorMessage != null && posts.isEmpty() -> {
                    Column(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = errorMessage ?: "Error al cargar el foro",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Button(onClick = { postViewModel.fetchPosts() }) {
                            Text("Reintentar")
                        }
                    }
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(posts, key = { it.id }) { post ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                                ),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "#${post.id} - ${post.title}",
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onSurface,
                                            modifier = Modifier.weight(1f)
                                        )
                                        IconButton(onClick = { postAEditar = post }) {
                                            Icon(
                                                imageVector = Icons.Default.Edit,
                                                contentDescription = "Editar",
                                                tint = AzulInstitucional
                                            )
                                        }
                                        IconButton(onClick = { postViewModel.deletePost(post.id) }) {
                                            Icon(
                                                imageVector = Icons.Default.Delete,
                                                contentDescription = "Eliminar",
                                                tint = MaterialTheme.colorScheme.error
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = post.body,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Diálogo para Crear Publicación (POST)
        if (mostrarDialogoCrear) {
            PostFormDialog(
                tituloDialogo = "Nueva Publicación Técnica",
                tituloInicial = "",
                cuerpoInicial = "",
                onDismiss = { mostrarDialogoCrear = false },
                onConfirm = { titulo, cuerpo ->
                    postViewModel.createPost(titulo, cuerpo)
                    mostrarDialogoCrear = false
                }
            )
        }

        // Diálogo para Editar Publicación (PUT)
        postAEditar?.let { post ->
            PostFormDialog(
                tituloDialogo = "Editar Publicación #${post.id}",
                tituloInicial = post.title,
                cuerpoInicial = post.body,
                onDismiss = { postAEditar = null },
                onConfirm = { titulo, cuerpo ->
                    postViewModel.updatePost(post, titulo, cuerpo)
                    postAEditar = null
                }
            )
        }
    }
}

@Composable
private fun PostFormDialog(
    tituloDialogo: String,
    tituloInicial: String,
    cuerpoInicial: String,
    onDismiss: () -> Unit,
    onConfirm: (String, String) -> Unit
) {
    var titulo by remember { mutableStateOf(tituloInicial) }
    var cuerpo by remember { mutableStateOf(cuerpoInicial) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = tituloDialogo,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = titulo,
                    onValueChange = { titulo = it },
                    label = { Text("Título del tema") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = cuerpo,
                    onValueChange = { cuerpo = it },
                    label = { Text("Contenido o consulta técnica") },
                    minLines = 3,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(titulo, cuerpo) },
                enabled = titulo.isNotBlank() && cuerpo.isNotBlank()
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}