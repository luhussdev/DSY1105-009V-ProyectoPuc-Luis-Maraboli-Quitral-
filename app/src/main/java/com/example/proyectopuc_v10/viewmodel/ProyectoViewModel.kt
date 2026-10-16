package com.example.proyectopuc_v10.viewmodel

import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel
import com.example.proyectopuc_v10.data.ProyectoRepository
import com.example.proyectopuc_v10.model.ProyectoCim
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ProyectoViewModel : ViewModel() {

    private val _proyectos = MutableStateFlow<List<ProyectoCim>>(emptyList())
    val proyectos: StateFlow<List<ProyectoCim>> = _proyectos.asStateFlow()

    init {
        cargarProyectos()
    }

    private fun cargarProyectos() {
        _proyectos.value = ProyectoRepository.obtenerProyectosDestacados()
    }
}

