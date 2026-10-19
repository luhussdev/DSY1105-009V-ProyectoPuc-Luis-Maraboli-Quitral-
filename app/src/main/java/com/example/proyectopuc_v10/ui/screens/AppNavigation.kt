package com.example.proyectopuc_v10.ui.screens

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.proyectopuc_v10.viewmodel.UsuarioViewModel

// Definicion de rutas utlizando Sealed Class
sealed class Screen(val route: String) {
    object Principal : Screen("principal")
    object Registro : Screen("registro")
    object Resumen : Screen("resumen")
    object Perfil : Screen("perfil")
    object Detalle : Screen("detalle/{proyectoId}") {
        fun crearRuta(proyectoId: Int) = "detalle/$proyectoId"
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    // Se comparte la misma instancia con ViewModel entre pantallas
    val usuarioViewModel: UsuarioViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = Screen.Principal.route
    ) {
        // Pantalla Principal (Catálogo I+D + DataStore)
        composable(Screen.Principal.route) {
            PantallaPrincipal(
                onIrASolicitud = {navController.navigate(Screen.Registro.route) },
                onIrAPerfil = {navController.navigate(Screen.Perfil.route) },
                onVerDetalle = { id -> navController.navigate(Screen.Detalle.crearRuta(id)) }
            )
        }

        // Pantalla de Detalle por ID
        composable(
            route = Screen.Detalle.route,
            arguments = listOf(navArgument("proyectoId") { type = NavType.IntType })
        ) { backStackEntry ->
            val proyectoId = backStackEntry.arguments?.getInt("proyectoId") ?: 0
            DetalleProyectoScreen(
                proyectoId = proyectoId,
                onVolver = { navController.popBackStack() }
            )
        }

        // Formulario de Solicitud / contacto
        composable(Screen.Registro.route) {
            RegistroScreen(
                viewModel = usuarioViewModel,
                onSolicitudExitosa = {
                    navController.navigate(Screen.Resumen.route)
                },
                onVolver = {
                    navController.popBackStack()
                }
            )
        }

        // Resumen de la solicitud
        composable(Screen.Resumen.route) {
            ResumenScreen(
                viewModel = usuarioViewModel,
                onVolverInicio = {
                    navController.navigate(Screen.Principal.route) {
                        popUpTo(Screen.Principal.route) { inclusive = true }
                    }
                },
                onIrAPerfil = {
                    navController.navigate(Screen.Perfil.route)
                }
            )
        }

        // Perfil de Usuario / Investigador
        composable(Screen.Perfil.route) {
            PerfilScreen(
                onVolver = { navController.popBackStack() }
            )
        }
    }
}