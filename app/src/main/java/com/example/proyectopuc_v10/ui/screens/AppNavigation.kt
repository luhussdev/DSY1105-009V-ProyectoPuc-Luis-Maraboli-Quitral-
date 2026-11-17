package com.example.proyectopuc_v10.ui.screens

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.proyectopuc_v10.viewmodel.UsuarioViewModel

// Definición de rutas utilizando Sealed Class
sealed class Screen(val route: String) {
    object Inicio : Screen("inicio")
    object Principal : Screen("principal")
    object Foro : Screen("foro")
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
    val usuarioViewModel: UsuarioViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = Screen.Inicio.route
    ) {
        // Nueva Pantalla de Bienvenida / Hub de Navegación
        composable(Screen.Inicio.route) {
            InicioScreen(
                onIrAProyectos = { navController.navigate(Screen.Principal.route) },
                onIrAForo = { navController.navigate(Screen.Foro.route) },
                onIrAContacto = { navController.navigate(Screen.Registro.route) },
                onIrAPerfil = { navController.navigate(Screen.Perfil.route) }
            )
        }

        // Catálogo de Proyectos I+D + DataStore
        composable(Screen.Principal.route) {
            PantallaPrincipal(
                onIrASolicitud = { navController.navigate(Screen.Registro.route) },
                onIrAPerfil = { navController.navigate(Screen.Perfil.route) },
                onVerDetalle = { id -> navController.navigate(Screen.Detalle.crearRuta(id)) }
            )
        }

        // Foro Técnico (CRUD API REST con Retrofit + Logs)
        composable(Screen.Foro.route) {
            PostScreen(
                onVolver = { navController.popBackStack() }
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

        // Formulario de Solicitud / Contacto
        composable(Screen.Registro.route) {
            RegistroScreen(
                usuarioViewModel = usuarioViewModel,
                onVolver = { navController.popBackStack() }
            )
        }

        // Resumen de la solicitud
        composable(Screen.Resumen.route) {
            ResumenScreen(
                viewModel = usuarioViewModel,
                onVolverInicio = {
                    navController.navigate(Screen.Inicio.route) {
                        popUpTo(Screen.Inicio.route) { inclusive = true }
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