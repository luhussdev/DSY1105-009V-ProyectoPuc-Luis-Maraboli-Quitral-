package com.example.proyectopuc_v10.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Person
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
import com.example.proyectopuc_v10.ui.theme.AzulInstitucional
import com.example.proyectopuc_v10.ui.theme.Blanco


@Composable
fun PantallaPrincipal(
    modifier: Modifier = Modifier,
    onIrASolicitud: () -> Unit = {},
    onIrAPerfil: () -> Unit = {},
    onVerDetalle: (Int) -> Unit = {}
) {
    val proyectoViewModel: ProyectoViewModel = viewModel()

    // Estado del Catálogo de Proyectos CIM UC
    val proyectos by proyectoViewModel.proyectos.collectAsState()

    Scaffold(
        topBar = {
            @OptIn(ExperimentalMaterial3Api::class)
            TopAppBar(
                title = { Text("CIM UC - Innovación en Madera") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AzulInstitucional,
                    titleContentColor = Blanco
                )
            )
        }
    ) { innerPadding ->
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

            // Listado de proyectos del CIM UC
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

            //  Botones de Navegación del MVP con íconos de Material Desing 3

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onIrASolicitud,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.Mail,
                        contentDescription = "Contacto"
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Contacto")
                }

                OutlinedButton(
                    onClick = onIrAPerfil,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Mi Perfil"
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Mi Perfil")
                }
            }
        }
    }
}