package com.example.proyectopuc_v10.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


// Pantalla(1) de inicio
@Composable
fun LoginScreen(onNavigateToHome: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("ProyectoPuc - VcM", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = onNavigateToHome) {
            Text("Ingresar al Sistema")
        }
    }
}

// Pantalla(2) home/principal
@Composable
fun HomeScreen(
    onNavigateToDetail: () -> Unit,
    onLogout: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Panel Principal", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onNavigateToDetail) {
            Text("Ver Detalle del Proyecto")
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(onClick = onLogout) {
            Text("Cerrar Sesión")
        }
    }
}

// Pantalla(3) Detalle
@Composable
fun DetailScreen(onBack: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Detalle del Caso CIM UC", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))
        Text("Proyecto dirigido por mi, codificado por mi, revisado por mi, lo hice solo u.u")
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = onBack) {
            Text("Volver")
        }
    }
}