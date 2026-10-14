package com.example.proyectopuc_v10.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectopuc_v10.data.EstadoDataStore
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class EstadoViewModel(application: Application): AndroidViewModel(application) {

    private val estadoDataStore = EstadoDataStore(application)

    // Estado reactivo del interruptor (null representa 'cargando')
    private val _activo = MutableStateFlow<Boolean?>(null)
    val activo: StateFlow<Boolean?> = _activo

    // Estado para controlar la visibilidad del mensaje animado
    private val _mostrarMensaje = MutableStateFlow(false)
    val mostrarMensaje: StateFlow<Boolean> = _mostrarMensaje

    init {
        cargarEstado()
    }

    fun cargarEstado() {
        viewModelScope.launch {
            // Simula tiempo de carga para el Loader
            delay(1500)
            _activo.value = estadoDataStore.obtenerEstado().first() ?: false
        }
    }

    fun alternarEstado() {
        viewModelScope.launch {
            val nuevoValor = !(_activo.value ?: false)

            // Persistir en DataStore
            estadoDataStore.guardarEstado(nuevoValor)
            _activo.value = nuevoValor

            // Feedback animado temporal
            _mostrarMensaje.value = true
            delay(2000)
            _mostrarMensaje.value = false
        }
    }
}