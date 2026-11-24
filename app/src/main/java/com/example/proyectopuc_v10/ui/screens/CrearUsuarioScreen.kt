package com.example.proyectopuc_v10.ui.screens

import android.util.Patterns
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

    // Estados de error por campo
    var errorNombre by remember { mutableStateOf<String?>(null) }
    var errorEmail by remember { mutableStateOf<String?>(null) }
    var errorOrganizacion by remember { mutableStateOf<String?>(null) }
    var errorPassword by remember { mutableStateOf<String?>(null) }

    // Estado para mostrar la alerta de confirmación al crear la cuenta
    var mostrarAlertaExito by remember { mutableStateOf(false) }

    fun validarFormulario(): Boolean {
        var esValido = true

        if (nombre.trim().length < 3) {
            errorNombre = "Ingresa tu nombre completo (mínimo 3 caracteres)"
            esValido = false
        } else {
            errorNombre = null
        }

        if (email.isBlank() || !Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()) {
            errorEmail = "Ingresa un correo electrónico válido (ej. usuario@cimuc.cl)"
            esValido = false
        } else {
            errorEmail = null
        }

        if (organizacion.isBlank()) {
            errorOrganizacion = "La organización o institución es obligatoria"
            esValido = false
        } else {
            errorOrganizacion = null
        }

        if (password.length < 6) {
            errorPassword = "La contraseña debe tener al menos 6 caracteres"
            esValido = false
        } else {
            errorPassword = null
        }

        return esValido
    }

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
                onValueChange = {
                    nombre = it
                    if (errorNombre != null) errorNombre = null
                },
                label = { Text("Nombre completo") },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                isError = errorNombre != null,
                supportingText = {
                    errorNombre?.let { Text(text = it, color = MaterialTheme.colorScheme.error) }
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    authViewModel.clearError()
                    if (errorEmail != null) errorEmail = null
                },
                label = { Text("Correo electrónico") },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                isError = errorEmail != null,
                supportingText = {
                    errorEmail?.let { Text(text = it, color = MaterialTheme.colorScheme.error) }
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = organizacion,
                onValueChange = {
                    organizacion = it
                    if (errorOrganizacion != null) errorOrganizacion = null
                },
                label = { Text("Organización / Institución") },
                leadingIcon = { Icon(Icons.Default.Business, contentDescription = null) },
                isError = errorOrganizacion != null,
                supportingText = {
                    errorOrganizacion?.let { Text(text = it, color = MaterialTheme.colorScheme.error) }
                },
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
                onValueChange = {
                    password = it
                    if (errorPassword != null) errorPassword = null
                },
                label = { Text("Contraseña (mín. 6 caracteres)") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                isError = errorPassword != null,
                supportingText = {
                    errorPassword?.let { Text(text = it, color = MaterialTheme.colorScheme.error) }
                },
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

            // Botón Crear Perfil -> Valida campos y guarda en Room
            Button(
                onClick = {
                    if (validarFormulario()) {
                        val creado = authViewModel.registrarUsuario(
                            nombre = nombre.trim(),
                            email = email.trim(),
                            password = password,
                            organizacion = organizacion.trim(),
                            areaInteres = areaInteres.trim(),
                            estaSuscrito = estaSuscrito
                        )
                        if (creado) {
                            mostrarAlertaExito = true
                        }
                    }
                },
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