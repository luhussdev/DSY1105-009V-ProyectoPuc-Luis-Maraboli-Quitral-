package com.example.proyectopuc_v10.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.proyectopuc_v10.ui.theme.AzulInstitucional
import com.example.proyectopuc_v10.ui.theme.Blanco
import com.example.proyectopuc_v10.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrearUsuarioScreen(
    authViewModel: AuthViewModel,
    onVolver: () -> Unit,
    onUsuarioCreado: () -> Unit
) {
    val authError by authViewModel.authError.collectAsState()

    var nombre by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var organizacion by remember { mutableStateOf("") }
    var areaInteres by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var estaSuscrito by remember { mutableStateOf(true) }

    // Estado para mostrar la alerta de confirmación al crear la cuenta
    var mostrarAlertaExito by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("CIM UC - Crear Usuario") },
                navigationIcon = {
                    IconButton(onClick = {
                        authViewModel.clearError()
                        onVolver()
                    }) {
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
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Registro y Creación de Perfil",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Completa tus datos para crear tu perfil en la plataforma CIM UC.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre completo") },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    authViewModel.clearError()
                },
                label = { Text("Correo electrónico") },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = organizacion,
                onValueChange = { organizacion = it },
                label = { Text("Organización / Institución") },
                leadingIcon = { Icon(Icons.Default.Business, contentDescription = null) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = areaInteres,
                onValueChange = { areaInteres = it },
                label = { Text("Área de Interés (ej. CLT, Normativa)") },
                leadingIcon = { Icon(Icons.Default.Badge, contentDescription = null) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña (mín. 6 caracteres)") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Checkbox(
                    checked = estaSuscrito,
                    onCheckedChange = { estaSuscrito = it }
                )
                Text(
                    text = "Suscribirme al boletín de proyectos I+D CIM UC",
                    style = MaterialTheme.typography.bodySmall
                )
            }

            if (authError != null) {
                Text(
                    text = authError ?: "",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Botón Crear Perfil -> Guarda en Room y dispara alerta de éxito
            Button(
                onClick = {
                    val creado = authViewModel.registrarUsuario(
                        nombre = nombre,
                        email = email,
                        password = password,
                        organizacion = organizacion,
                        areaInteres = areaInteres,
                        estaSuscrito = estaSuscrito
                    )
                    if (creado) {
                        mostrarAlertaExito = true
                    }
                },
                enabled = nombre.isNotBlank() &&
                        email.contains("@") &&
                        organizacion.isNotBlank() &&
                        password.length >= 6,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("Crear Perfil")
            }

            // Botón Volver explícito -> Regresa a Pantalla 1 (Login)
            OutlinedButton(
                onClick = {
                    authViewModel.clearError()
                    onVolver()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("Volver")
            }
        }

        // Alerta de confirmación de creación de usuario
        if (mostrarAlertaExito) {
            AlertDialog(
                onDismissRequest = {
                    mostrarAlertaExito = false
                    onUsuarioCreado()
                },
                icon = {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = AzulInstitucional,
                        modifier = Modifier.size(40.dp)
                    )
                },
                title = {
                    Text(
                        text = "¡Usuario Creado!",
                        fontWeight = FontWeight.Bold
                    )
                },
                text = {
                    Text(
                        text = "Tu cuenta y perfil ($email) se han guardado correctamente en la base de datos local."
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            mostrarAlertaExito = false
                            onUsuarioCreado()
                        }
                    ) {
                        Text("Ir al Panel Principal")
                    }
                }
            )
        }
    }
}