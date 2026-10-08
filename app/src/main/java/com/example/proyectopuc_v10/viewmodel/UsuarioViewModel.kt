package com.example.proyectopuc_v10.viewmodel

import androidx.lifecycle.ViewModel
import com.example.proyectopuc_v10.model.FormularioErrores
import com.example.proyectopuc_v10.model.FormularioUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class UsuarioViewModel : ViewModel() {

    // Estado privado mutable
    private val _estado = MutableStateFlow(FormularioUiState())

    // Estado público inmutable expuesto a la UI
    val estado: StateFlow<FormularioUiState> = _estado.asStateFlow()

    // Funciones para actualizar cada campo que el usuario interactue
    fun onNombreChange(nuevoNombre: String) {
        _estado.update { it.copy(nombre = nuevoNombre) }
    }

    fun onCorreoChange(nuevoCorreo: String) {
        _estado.update { it.copy(correo = nuevoCorreo) }
    }

    fun onClaveChange(nuevaClave: String) {
        _estado.update { it.copy(clave = nuevaClave) }
    }

    fun onDireccionChange(nuevaDireccion: String) {
        _estado.update { it.copy(direccion = nuevaDireccion) }
    }

    fun onAceptaTerminosChange(aceptado: Boolean) {
        _estado.update { it.copy(aceptaTerminos = aceptado) }
    }

    // Función validación del Formulario
    fun validarFormulario(): Boolean {
        val current = _estado.value

        val errorNombre = if (current.nombre.isBlank()) "El nombre no puede estar vacio" else null
        val errorCorreo = if (current.correo.contains("@")) "El correo no es válido" else null
        val errorClave = if (current.clave.length < 6) "La contraseña debe tener al menos 6 caracteres" else null
        val errorDireccion = if (current.direccion.isBlank()) "La dirección no puede estar vacia" else null
        val errorTerminos = if (!current.aceptaTerminos) "Debe aceptar los términos y condiciones" else null

        val nuevosErrores = FormularioErrores(
            nombre = errorNombre,
            correo = errorCorreo,
            clave = errorClave,
            direccion = errorDireccion,
            aceptaTerminos = errorTerminos
        )

        // Actualizamos lo errores dentro del estado
        _estado.update { it.copy(errores = nuevosErrores) }

        // Si todos los errores son nulos, el formulario es valido
        return errorNombre == null &&
                errorCorreo == null &&
                errorClave == null &&
                errorDireccion == null &&
                errorTerminos == null
    }

    // Funciones para reiniciar el formulario de ser necesario
    fun limpiarFormulario() {
        _estado.value = FormularioUiState()
    }
}

