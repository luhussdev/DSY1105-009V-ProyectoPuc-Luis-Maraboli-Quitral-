package com.example.proyectopuc_v10.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.proyectopuc_v10.viewmodel.UsuarioViewModel
import android.widget.Toast


// Función para invocar el Intent del correo
fun simularEnvioCorreoSuscripcion(context: Context, correoUsuario: String) {
    // 1. Inserte aquí su correo personal para recibir la alerta
    val miCorreoPersonal = "barenas7719@gmail.com"

    val asunto = "[CIM UC] Confirmación de suscripción al Boletin I+D"
    val cuerpo = """
        ¡Hola!
        
        Gracias por suscribirte al boletín de novedades y proyectos I+D del Centro UC de Innovación en Madera (CIM UC). Tu registro desde la aplicación móvil se ha completado con éxito.
        
        A partir de este momento recibirás información sobre:
        • Proyectos de investigación aplicada.
        • Transferencia tecnológica y construcción en madera.
        • Publicaciones y próximos eventos.
        
        Detalles de tu suscripción:
        
        • Correo registrado: $correoUsuario
        • Estado: Suscripción Activa
        • Fecha de registro: Noviembre 2026
        
        Esta es una notificación automática generada por ProyectoPUC_V10.
        Atentamente,
        
        Equipo de Desarrollo VcM - CIM UC
    """.trimIndent()

    // 2. Construcción del Intent nativo tipo ACTION_SENDTO (mailto:)
    val intent = Intent(Intent.ACTION_SENDTO).apply {
        data = Uri.parse("mailto:$miCorreoPersonal")
        putExtra(Intent.EXTRA_EMAIL, arrayOf(miCorreoPersonal))
        putExtra(Intent.EXTRA_SUBJECT, asunto)
        putExtra(Intent.EXTRA_TEXT, cuerpo)
    }

    // 3. Manejo seguro de la llamada al sistema
    try{
        val chooserIntent = Intent.createChooser(intent, "Confirmar suscripción con....")
        chooserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(chooserIntent)
    } catch (e: Exception) {
        Toast.makeText(
            context,
            "No se encontró una aplicación de correo instalada.",
            Toast.LENGTH_LONG
        ).show()
    }
}

@Composable
fun RegistroScreen(
    usuarioViewModel: UsuarioViewModel = viewModel(),
    onSolicitudExitosa: () -> Unit = {},
    onVolver: () -> Unit = {}
) {
    val context = LocalContext.current
    // 1. Observamos el estado del ViewModel para los mensajes de error
    val uiState by usuarioViewModel.uiState.collectAsState()

    var nombre by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var organizacion by remember { mutableStateOf("") }
    var areaInteres by remember { mutableStateOf("") }
    var estaSuscrito by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            @OptIn(ExperimentalMaterial3Api::class)
            TopAppBar(
                title = { Text("Registro de Usuario") },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver al Catálogo de Proyectos"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Ingresa tus Datos",
                style = MaterialTheme.typography.headlineSmall
            )

            // CAMPO: NOMBRE COMPLETO
            OutlinedTextField(
                value = nombre,
                onValueChange = {
                    nombre = it
                    usuarioViewModel.onNombreCambiado(it)
                },
                label = { Text("Nombre Completo") },
                isError = uiState.errores.nombre != null,
                supportingText = {
                    uiState.errores.nombre?.let { Text(it, color = MaterialTheme.colorScheme.error) }
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // CAMPO: CORREO ELECTRÓNICO
            OutlinedTextField(
                value = correo,
                onValueChange = {
                    correo = it
                    usuarioViewModel.onCorreoCambiado(it)
                },
                label = { Text("Correo Electrónico") },
                isError = uiState.errores.correo != null,
                supportingText = {
                    uiState.errores.correo?.let { Text(it, color = MaterialTheme.colorScheme.error) }
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // CAMPO: ORGANIZACIÓN / EMPRESA
            OutlinedTextField(
                value = organizacion,
                onValueChange = {
                    organizacion = it
                    usuarioViewModel.onOrganizacionCambiada(it)
                },
                label = { Text("Organización / Empresa") },
                isError = uiState.errores.organizacion != null,
                supportingText = {
                    uiState.errores.organizacion?.let { Text(it, color = MaterialTheme.colorScheme.error) }
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // CAMPO: ÁREA DE INTERÉS
            OutlinedTextField(
                value = areaInteres,
                onValueChange = {
                    areaInteres = it
                    usuarioViewModel.onAreaInteresCambiada(it)
                },
                label = { Text("Area de Interés") },
                isError = uiState.errores.areaInteres != null,
                supportingText = {
                    uiState.errores.areaInteres?.let { Text(it, color = MaterialTheme.colorScheme.error) }
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // CHECKBOX DE SUSCRIPCIÓN
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = estaSuscrito,
                        onCheckedChange = { estaSuscrito = it }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Deseo suscribirme al boletín mensual de proyectos e investigación I+D del CIM UC",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // BOTÓN PRINCIPAL: REGISTRAR
            Button(
                onClick = {
                    // 2. Ejecutar la validación completa
                    val esValido = usuarioViewModel.validarFormulario(
                        nombre = nombre,
                        correo = correo,
                        organizacion = organizacion,
                        areaInteres = areaInteres
                    )
                    if (esValido) {
                        // 3. Pasar 'nombre' como primer parámetro al guardar en Room
                        usuarioViewModel.guardarUsuarioEnRoom(
                            nombre = nombre,
                            correo = correo,
                            organizacion = organizacion,
                            areaInteres = areaInteres,
                            estaSuscrito = estaSuscrito
                        )
                        if (estaSuscrito) {
                            simularEnvioCorreoSuscripcion(context, correo)
                        }
                        onSolicitudExitosa()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "Registrar"
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Registrar Usuario")
            }

            // BOTÓN SECUNDARIO: VOLVER
            OutlinedButton(
                onClick = onVolver,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Volver"
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Volver al Catálogo de Proyectos")
            }
        }
    }
}