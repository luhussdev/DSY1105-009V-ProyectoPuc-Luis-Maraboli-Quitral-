package com.example.proyectopuc_v10.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectopuc_v10.data.local.AppDatabase
import com.example.proyectopuc_v10.data.local.UsuarioEntity
import com.example.proyectopuc_v10.model.FormularioErrores
import com.example.proyectopuc_v10.model.FormularioUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UsuarioViewModel(application: Application) : AndroidViewModel(application) {

    private val usuarioDao = AppDatabase.obtenerBaseDatos(application).UsuarioDao()

    // Estado privado mutable
    private val _uiState = MutableStateFlow(FormularioUiState())
    val uiState: StateFlow<FormularioUiState> = _uiState.asStateFlow()

    fun onNombreCambiado(nuevoNombre: String) {
        _uiState.update {
            it.copy(
                nombre = nuevoNombre,
                errores = it.errores.copy(nombre = null)
            )
        }
    }

    fun onCorreoCambiado(nuevoCorreo: String) {
        _uiState.update {
            it.copy(
                correo = nuevoCorreo,
                errores = it.errores.copy(correo = null)
            )
        }
    }

    fun onOrganizacionCambiada(nuevaOrg: String) {
        _uiState.update {
            it.copy(
                organizacion = nuevaOrg,
                errores = it.errores.copy(organizacion = null)
            )
        }
    }

    fun onAreaInteresCambiada(nuevaArea: String) {
        _uiState.update {
            it.copy(
                areaInteres = nuevaArea,
                errores = it.errores.copy(areaInteres = null)
            )
        }
    }

    fun onTerminosCambiados(aceptado: Boolean) {
        _uiState.update {
            it.copy(
                aceptaTerminos = aceptado,
                errores = it.errores.copy(aceptaTerminos = null)
            )
        }
    }

    fun validarFormulario(
        nombre: String,
        correo: String,
        organizacion: String,
        areaInteres: String
    ): Boolean {
        var esValido = true

        var errNombre: String? = null
        var errCorreo: String? = null
        var errOrg: String? = null
        var errArea: String? = null

        // 1. Validación de Nombre (No vacio y mínimo 3 caracteres)
        if (nombre.trim().isBlank()) {
            errNombre = "El nombre no puede estar vacío"
            esValido = false
        } else if (nombre.trim().length < 3) {
            errNombre = "El nombre debe tener al menos 3 caracteres"
            esValido = false
        }

        // 2. Validación de Correo (No vacio, contiene '@' y formato estándar)
        if (correo.trim().isBlank()) {
            errCorreo = "Ingresa un correo institucional válido"
            esValido = false
        } else if (!correo.contains("@") || !android.util.Patterns.EMAIL_ADDRESS.matcher(correo.trim()).matches()) {
            errCorreo = "Ingresa un correo institucional válido (ej: usuario@dominio.cl)"
            esValido = false
        }

        // 3. Validación
        if (organizacion.trim().isBlank()) {
            errOrg = "Indica tu institución o empresa"
            esValido = false
        }

        // 4. Validación de Área de Interés (No vacio)
        if (areaInteres.trim().isBlank()) {
            errArea = "Especifica el área I+D de interés"
            esValido = false
        }

        _uiState.update {
            it.copy(
                nombre = nombre,
                correo = correo,
                organizacion = organizacion,
                areaInteres = areaInteres,
                errores = FormularioErrores(
                    nombre = errNombre,
                    correo = errCorreo,
                    organizacion = errOrg,
                    areaInteres = errArea
                )
            )
        }
        return esValido
    }

    fun guardarUsuarioEnRoom(
        nombre: String,
        correo: String,
        organizacion: String,
        areaInteres: String,
        estaSuscrito: Boolean
    ){
        viewModelScope.launch(Dispatchers.IO) {
            val nuevoUsuario = UsuarioEntity(
                nombre = nombre.trim(),
                correo = correo.trim(),
                organizacion = organizacion.trim(),
                areaInteres = areaInteres.trim(),
                aceptaTerminos = true,
                estaSuscrito = estaSuscrito
            )
            usuarioDao.guardarUsuario(nuevoUsuario)
        }
    }

}

