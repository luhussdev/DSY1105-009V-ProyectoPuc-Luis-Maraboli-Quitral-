package com.example.proyectopuc_v10.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectopuc_v10.data.ProyectoRepository
import com.example.proyectopuc_v10.data.local.AppDatabase
import com.example.proyectopuc_v10.model.ProyectoCim
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProyectoViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ProyectoRepository

    // Expone la lista transformada a ProyectoCim para que la UI no sufra cambio de tipo

    val proyectos: StateFlow<List<ProyectoCim>>

    init {
        val database = AppDatabase.obtenerBaseDatos(application)
        repository = ProyectoRepository(database.proyectoDao())

        proyectos = repository.todosLosProyectos
            .map { listaEntity ->
                listaEntity.map { entity ->
                    ProyectoCim(
                        id = entity.id,
                        titulo = entity.titulo,
                        areaTematica = entity.areaTematica,
                        descripcion = entity.descripcion,
                        liderInvestigacion = entity.liderInvestigacion,
                        anio = entity.anio
                    )
                }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )
    }

    fun agregarProyecto(
        titulo: String,
        areaTematica: String,
        descripcion: String,
        liderInvestigacion: String,
        anio: Int
    ) {
        viewModelScope.launch {
            repository.agregarProyecto(
                titulo = titulo,
                areaTematica = areaTematica,
                descripcion = descripcion,
                liderInvestigacion = liderInvestigacion,
                anio = anio
            )
        }
    }
}

/*
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
*/