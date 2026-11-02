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

    fun validarFormulario(): Boolean {
        val estado = _uiState.value
        var esValido = true

        var errNombre: String? = null
        var errCorreo: String? = null
        var errOrg: String? = null
        var errArea: String? = null
        var errTerminos: String? = null

        if (estado.nombre.isBlank()) {
            errNombre = "El nombre no puede estar vacío"
            esValido = false
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(estado.correo).matches()) {
            errCorreo = "Ingresa un correo institucional válido"
            esValido = false
        }

        if (estado.organizacion.isBlank()) {
            errOrg = "Indica tu institución o empresa"
            esValido = false
        }

        if (estado.areaInteres.isBlank()) {
            errArea = "Especifica el área I+D de interés"
            esValido = false
        }

        if (!estado.aceptaTerminos) {
            errTerminos = "Debe aceptar las políticas de colaboración"
            esValido = false
        }

        _uiState.update {
            it.copy(
                errores = FormularioErrores(
                    nombre = errNombre,
                    correo = errCorreo,
                    organizacion = errOrg,
                    areaInteres = errArea,
                    aceptaTerminos = errTerminos
                )
            )
        }
        if (esValido) {
            guardarUsuarioEnRoom(estado.correo, estado.organizacion, estado.areaInteres, false)
        }

        return esValido
    }

    fun guardarUsuarioEnRoom(
        correo: String,
        organizacion: String,
        areaInteres: String,
        estaSuscrito: Boolean
    ){
        viewModelScope.launch(Dispatchers.IO) {
            val nuevoUsuario = UsuarioEntity(
                nombre = "Usuario Registrado",
                correo = correo,
                organizacion = organizacion,
                areaInteres = areaInteres,
                aceptaTerminos = true,
                estaSuscrito = estaSuscrito
            )
            usuarioDao.guardarUsuario(nuevoUsuario)
        }
    }

}

