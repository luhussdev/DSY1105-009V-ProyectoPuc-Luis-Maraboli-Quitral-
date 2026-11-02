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


// Función para invocar el Intent del correo
fun simularEnvioCorreoSuscripcion(context: Context, correoDestino: String) {
    val asunto = "[CIM UC] Confirmación de suscripción al Boletin I+D"
    val cuerpo = """
        ¡Hola!
        
        Gracias por suscribirte al boletín de novedades y proyectos I+D del Centro UC de Innovación en Madera (CIM UC).
        
        A partir de este momento recibirás información sobre:
        - Proyectos de investigación aplicada.
        - Transferencia tecnológica y construcción en madera.
        - Publicaciones y próximos eventos.
        
        Atentamente,
        Equipo CIM UC
    """.trimIndent()

    val intent = Intent(Intent.ACTION_SENDTO).apply {
        data = Uri.parse("mailto:")
        putExtra(Intent.EXTRA_EMAIL, arrayOf(correoDestino))
        putExtra(Intent.EXTRA_SUBJECT, asunto)
        putExtra(Intent.EXTRA_TEXT, cuerpo)
    }

    if (intent.resolveActivity(context.packageManager) != null) {
        context.startActivity(intent)
    } else {
        context.startActivity(Intent.createChooser(intent, "Enviar correo de prueba con..."))
    }
}

@Composable
fun RegistroScreen(
    usuarioViewModel: UsuarioViewModel = viewModel(),
    onVolver: () -> Unit = {}
) {
    val context = LocalContext.current

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

            OutlinedTextField(
                value = correo,
                onValueChange = { correo = it },
                label = { Text("Correo Electrónico") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = organizacion,
                onValueChange = { organizacion = it },
                label = { Text("Organización / Empresa") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = areaInteres,
                onValueChange = { areaInteres = it },
                label = { Text("Area de Interés") },
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
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // BOTÓN REGISTRAR Y DISPARAR INTENT
            Button(
                onClick = {
                    if (correo.isNotBlank()) {
                        // 1. Persistir usuario y preferencia
                        usuarioViewModel.guardarUsuarioEnRoom(correo, organizacion, areaInteres, estaSuscrito)

                        // 2. Disparar el Intent del correo si aceptó suscribirse
                        if (estaSuscrito) {
                            simularEnvioCorreoSuscripcion(context, correo)
                        }

                        onVolver()
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

            // NAVEGACIÓN DE REGRESO ECPLÍCITA
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