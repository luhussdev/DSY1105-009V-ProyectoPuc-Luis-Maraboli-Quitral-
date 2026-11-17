package com.example.proyectopuc_v10.ui.screens

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.proyectopuc_v10.viewmodel.AuthViewModel

sealed class Screen(val route: String) {
    object Login : Screen("login")                // Pantalla 1
    object Inicio : Screen("inicio")              // Pantalla 2 (Dashboard sesión iniciada)
    object CrearUsuario : Screen("crear_usuario") // Pantalla 3 (Única pantalla de registro)
    object Principal : Screen("principal")        // Pantalla 4 (Noticias y Proyectos I+D)
    object Foro : Screen("foro")                  // Pantalla 5 (Foro Técnico API REST)
    object Perfil : Screen("perfil")              // Pantalla 6 (Editar Perfil)
    object Detalle : Screen("detalle/{proyectoId}") {
        fun crearRuta(proyectoId: Int) = "detalle/$proyectoId"
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        // PANTALLA 1: Login + Acceso a Registro único
        composable(Screen.Login.route) {
            LoginScreen(
                authViewModel = authViewModel,
                onLoginExitoso = {
                    navController.navigate(Screen.Inicio.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onIrACrearUsuario = {
                    navController.navigate(Screen.CrearUsuario.route)
                }
            )
        }

        // PANTALLA 3: Única Pantalla de Registro de Usuario
        composable(Screen.CrearUsuario.route) {
            CrearUsuarioScreen(
                authViewModel = authViewModel,
                onVolver = { navController.popBackStack() },
                onUsuarioCreado = {
                    navController.navigate(Screen.Inicio.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        // PANTALLA 2: Dashboard Sesión Iniciada
        composable(Screen.Inicio.route) {
            InicioScreen(
                authViewModel = authViewModel,
                onIrANoticias = { navController.navigate(Screen.Principal.route) },
                onIrAForo = { navController.navigate(Screen.Foro.route) },
                onIrAEditarPerfil = { navController.navigate(Screen.Perfil.route) },
                onCerrarSesion = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Inicio.route) { inclusive = true }
                    }
                }
            )
        }

        // PANTALLA 4: Noticias y Proyectos I+D (limpia, sin botones inferiores)
        composable(Screen.Principal.route) {
            PantallaPrincipal(
                onVerDetalle = { id -> navController.navigate(Screen.Detalle.crearRuta(id)) },
                onVolver = { navController.popBackStack() }
            )
        }

        // PANTALLA 5: Foro Técnico (API REST)
        composable(Screen.Foro.route) {
            PostScreen(
                onVolver = { navController.popBackStack() }
            )
        }

        // PANTALLA 6: Editar Perfil
        composable(Screen.Perfil.route) {
            PerfilScreen(
                onVolver = { navController.popBackStack() }
            )
        }

        // Detalle de noticia/proyecto por ID
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
    }
}