package com.example.proyectopuc_v10.ui.screens

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.proyectopuc_v10.viewmodel.UsuarioViewModel

// Definicion de rutas utlizando Sealed Class
sealed class Screen(val route: String) {
    object Registro : Screen("registro")
    object Resumen : Screen("resumen")
    object Animaciones : Screen("animaciones")
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    // Se comparte la misma instancia con ViewModel entre pantallas
    val usuarioViewModel: UsuarioViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = Screen.Registro.route
    ) {
        // Pantalla de Formulario de Registro
        composable(Screen.Registro.route) {
            RegistroScreen(
                viewModel = usuarioViewModel,
                onNavegarResumen = {
                    navController.navigate(Screen.Resumen.route)
                }
            )
        }

        // Pantalla de Resumen
        composable(Screen.Resumen.route) {
            ResumenScreen(
                viewModel = usuarioViewModel,
                onVolver = {
                    navController.popBackStack()
                },
                onIrAAnimaciones = {
                    navController.navigate(Screen.Animaciones.route)
                }
            )
        }
        // Pantalla Principal de la Guía 12
        composable(Screen.Animaciones.route) {
            PantallaPrincipal()
        }
    }
}