package com.example.proyectopuc_v10.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class UsuarioSesion(
    val nombre: String,
    val email: String,
    val password: String,
    val especialidad: String = "Investigador / Profesional"
)

class AuthViewModel : ViewModel() {

    companion object {
        private const val TAG = "AUTH_SESSION"
    }

    // Lista en memoria con un usuario demo precargado para pruebas rápidas
    private val _usuariosRegistrados = MutableStateFlow(
        listOf(
            UsuarioSesion(
                nombre = "Investigador CIM UC",
                email = "admin@cimuc.cl",
                password = "123456",
                especialidad = "Ingeniería en Madera"
            )
        )
    )

    private val _usuarioActual = MutableStateFlow<UsuarioSesion?>(null)
    val usuarioActual: StateFlow<UsuarioSesion?> = _usuarioActual

    private val _authError = MutableStateFlow<String?>(null)
    val authError: StateFlow<String?> = _authError

    fun clearError() {
        _authError.value = null
    }

    fun iniciarSesion(email: String, password: String): Boolean {
        val correoLimpio = email.trim().lowercase()
        val encontrado = _usuariosRegistrados.value.find {
            it.email.lowercase() == correoLimpio && it.password == password
        }
        return if (encontrado != null) {
            _usuarioActual.value = encontrado
            _authError.value = null
            Log.d(TAG, "Sesión iniciada correctamente para: ${encontrado.email}")
            true
        } else {
            _authError.value = "Correo o contraseña incorrectos"
            Log.w(TAG, "Intento fallido de inicio de sesión para: $correoLimpio")
            false
        }
    }

    fun registrarUsuario(
        nombre: String,
        email: String,
        password: String,
        especialidad: String
    ): Boolean {
        val correoLimpio = email.trim().lowercase()
        if (_usuariosRegistrados.value.any { it.email.lowercase() == correoLimpio }) {
            _authError.value = "Ya existe una cuenta con ese correo"
            return false
        }

        val nuevoUsuario = UsuarioSesion(
            nombre = nombre.trim(),
            email = correoLimpio,
            password = password,
            especialidad = especialidad.trim().ifBlank { "Profesional CIM UC" }
        )

        _usuariosRegistrados.value = _usuariosRegistrados.value + nuevoUsuario
        _usuarioActual.value = nuevoUsuario
        _authError.value = null
        Log.d(TAG, "Nuevo usuario registrado e iniciado: ${nuevoUsuario.email}")
        return true
    }

    fun cerrarSesion() {
        Log.d(TAG, "Sesión cerrada para: ${_usuarioActual.value?.email}")
        _usuarioActual.value = null
        _authError.value = null
    }
}