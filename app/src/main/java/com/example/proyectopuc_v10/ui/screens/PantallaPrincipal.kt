package com.example.proyectopuc_v10.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.proyectopuc_v10.ui.components.TarjetaProyecto
import com.example.proyectopuc_v10.viewmodel.EstadoViewModel
import com.example.proyectopuc_v10.viewmodel.ProyectoViewModel


@Composable
fun PantallaPrincipal(
    modifier: Modifier = Modifier,
    //estadoViewModel: EstadoViewModel = viewModel(),
    //proyectoViewModel: ProyectoViewModel = viewModel(),
    onIrASolicitud: () -> Unit = {},
    onIrAPerfil: () -> Unit = {},
    onVerDetalle: (Int) -> Unit = {}
) {
    /*
    // Estados (DataStore y Animaciones)
    val estado = estadoViewModel.activo.collectAsState()
    val mostrarMensaje = estadoViewModel.mostrarMensaje.collectAsState()
     */

    val context = LocalContext.current
    val estadoViewModel: EstadoViewModel = viewModel(
        factory = androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.getInstance(
            context.applicationContext as android.app.Application
        )
    )
    val proyectoViewModel: ProyectoViewModel = viewModel()

    // Estados (DataStore y Animaciones)
    val estado = estadoViewModel.activo.collectAsState()
    val mostrarMensaje = estadoViewModel.mostrarMensaje.collectAsState()

    // Estado del Catálogo de Proyectos CIM UC
    val proyectos by proyectoViewModel.proyectos.collectAsState()

    Scaffold(
        topBar = {
            @OptIn(ExperimentalMaterial3Api::class)
            TopAppBar(
                title = { Text("CIM UC - Innovación en Madera") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { innerPadding ->

        // Loader si el DataStore no se ha cargado aún
        if(estado.value == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            val estadoActivo = estado.value!!

            // Animación suave del color del botón de Estado
            val colorAnimado by animateColorAsState(
                targetValue = if (estadoActivo) MaterialTheme.colorScheme.primary else Color(0xFFBBBEC5),
                animationSpec = tween(durationMillis = 500),
                label = "ColorBotonAnimation"
            )

            val textoBoton by remember(estadoActivo) {
                derivedStateOf { if (estadoActivo) "Suscripción a Novedades Activa" else "Activar Suscripción a Novedades" }
            }

            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp)
            ) {
                // Titulo de la sección de contenidos
                Text(
                    text = "Proyectos y Capacidades I+D Destacadas",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Listado de proyectos reales/ficticios del CIM UC
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(proyectos) { proyecto ->
                        TarjetaProyecto(
                            proyecto = proyecto,
                            onClick = { onVerDetalle(proyecto.id) }
                            )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                //  Sección Guía 12: DataStore + Animación
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Button(
                            onClick = { estadoViewModel.alternarEstado() },
                            colors = ButtonDefaults.buttonColors(containerColor = colorAnimado),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = textoBoton, style = MaterialTheme.typography.bodyLarge)
                        }

                        AnimatedVisibility(visible = mostrarMensaje.value) {
                            Text(
                                text = "¡Preferencia guardada en DataStore!",
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Botones de Navegación del MVP
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = onIrASolicitud,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Contacto")
                    }

                    OutlinedButton(
                        onClick = onIrAPerfil,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Mi Perfil")
                    }
                }
            }
        }
    }
}