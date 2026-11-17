package com.example.proyectopuc_v10.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectopuc_v10.data.local.AppDatabase
import com.example.proyectopuc_v10.data.local.UsuarioEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class UsuarioSesion(
    val nombre: String,
    val email: String,
    val password: String,
    val organizacion: String = "CIM UC",
    val especialidad: String = "Investigador / Profesional"
)

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        private const val TAG = "AUTH_SESSION"
    }

    private val usuarioDao = AppDatabase.obtenerBaseDatos(application).UsuarioDao()

    private val _usuariosRegistrados = MutableStateFlow(
        listOf(
            UsuarioSesion(
                nombre = "Investigador CIM UC",
                email = "admin@cimuc.cl",
                password = "123456",
                organizacion = "Pontificia Universidad Católica",
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
        organizacion: String,
        areaInteres: String,
        estaSuscrito: Boolean = true
    ): Boolean {
        val correoLimpio = email.trim().lowercase()
        if (_usuariosRegistrados.value.any { it.email.lowercase() == correoLimpio }) {
            _authError.value = "Ya existe una cuenta con ese correo"
            return false
        }

        val orgFinal = organizacion.trim().ifBlank { "Independiente / CIM UC" }
        val areaFinal = areaInteres.trim().ifBlank { "Construcción en Madera" }

        val nuevoUsuario = UsuarioSesion(
            nombre = nombre.trim(),
            email = correoLimpio,
            password = password,
            organizacion = orgFinal,
            especialidad = areaFinal
        )

        _usuariosRegistrados.value = _usuariosRegistrados.value + nuevoUsuario
        _usuarioActual.value = nuevoUsuario
        _authError.value = null

        // Persistir el nuevo usuario en la base de datos local Room
        viewModelScope.launch {
            val entidadRoom = UsuarioEntity(
                nombre = nombre.trim(),
                correo = correoLimpio,
                organizacion = orgFinal,
                areaInteres = areaFinal,
                aceptaTerminos = true,
                estaSuscrito = estaSuscrito
            )
            val nuevoId = usuarioDao.guardarUsuario(entidadRoom)
            Log.d(TAG, "Usuario guardado en Room con ID=$nuevoId: $correoLimpio")
        }

        return true
    }

    fun cerrarSesion() {
        Log.d(TAG, "Sesión cerrada para: ${_usuarioActual.value?.email}")
        _usuarioActual.value = null
        _authError.value = null
    }
}