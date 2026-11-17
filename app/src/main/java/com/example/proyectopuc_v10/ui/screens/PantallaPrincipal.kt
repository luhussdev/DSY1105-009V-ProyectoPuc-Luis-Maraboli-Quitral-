package com.example.proyectopuc_v10.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.proyectopuc_v10.ui.components.TarjetaProyecto
import com.example.proyectopuc_v10.ui.theme.AzulInstitucional
import com.example.proyectopuc_v10.ui.theme.Blanco
import com.example.proyectopuc_v10.viewmodel.ProyectoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaPrincipal(
    modifier: Modifier = Modifier,
    onVerDetalle: (Int) -> Unit = {},
    onVolver: () -> Unit = {}
) {
    val proyectoViewModel: ProyectoViewModel = viewModel()
    val proyectos by proyectoViewModel.proyectos.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("CIM UC - Noticias y Proyectos") },
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
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Text(
                text = "Proyectos y Capacidades I+D Destacadas",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(proyectos) { proyecto ->
                    TarjetaProyecto(
                        proyecto = proyecto,
                        onClick = { onVerDetalle(proyecto.id) }
                    )
                }
            }
        }
    }
}