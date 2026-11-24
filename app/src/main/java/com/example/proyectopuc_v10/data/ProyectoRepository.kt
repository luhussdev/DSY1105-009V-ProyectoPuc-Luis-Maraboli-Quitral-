package com.example.proyectopuc_v10.data

import android.util.Log
import com.example.proyectopuc_v10.data.local.ProyectoDao
import com.example.proyectopuc_v10.data.local.ProyectoEntity
import com.example.proyectopuc_v10.data.remote.CimucApiService
import com.example.proyectopuc_v10.data.remote.RetrofitInstance
import com.example.proyectopuc_v10.model.ProyectoRemoto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class ProyectoRepository(
    private val proyectoDao: ProyectoDao,
    private val cimucApi: CimucApiService = RetrofitInstance.cimucApi
) {

    // Flujo reactivo con la lista de proyectos en la base de datos SQLite (Room)
    val todosLosProyectos: Flow<List<ProyectoEntity>> = proyectoDao.obtenerTodosLosProyectos()

    suspend fun sincronizarConMicroservicio() {
        try {
            val remotos = cimucApi.obtenerProyectos()
            if (remotos.isNotEmpty()) {
                val localesActuales = proyectoDao.obtenerTodosLosProyectos().first()
                localesActuales.forEach { proyectoDao.eliminarProyecto(it) }

                remotos.forEach { remoto ->
                    proyectoDao.insertarProyecto(
                        ProyectoEntity(
                            id = remoto.id?.toInt() ?: 0,
                            titulo = remoto.titulo,
                            areaTematica = remoto.areaTematica,
                            descripcion = "${remoto.descripcion} [Estado: ${remoto.estado}]",
                            liderInvestigacion = remoto.investigadorPrincipal,
                            anio = 2026
                        )
                    )
                }
            }
        } catch (e: Exception) {
            // Si Render está despertando o no hay red, mantiene la caché local de Room sin crashear
            Log.e("ProyectoRepository", "Error sincronizando con Render: ${e.message}")
        }
    }

    suspend fun obtenerProyectosPorId(id: Int): ProyectoEntity? {
        return proyectoDao.obtenerProyectoPorId(id)
    }

    suspend fun agregarProyecto(
        titulo: String,
        areaTematica: String,
        descripcion: String,
        liderInvestigacion: String,
        anio: Int
    ) {
        var idGuardado = 0
        try {
            val creadoEnRender = cimucApi.crearProyecto(
                ProyectoRemoto(
                    titulo = titulo,
                    areaTematica = areaTematica,
                    descripcion = descripcion,
                    investigadorPrincipal = liderInvestigacion,
                    estado = "En ejecución"
                )
            )
            idGuardado = creadoEnRender.id?.toInt() ?: 0
        } catch (e: Exception) {
            Log.e("ProyectoRepository", "No se pudo enviar a Render, guardando localmente: ${e.message}")
        }

        val nuevoProyecto = ProyectoEntity(
            id = idGuardado,
            titulo = titulo,
            areaTematica = areaTematica,
            descripcion = descripcion,
            liderInvestigacion = liderInvestigacion,
            anio = anio
        )
        proyectoDao.insertarProyecto(nuevoProyecto)
    }

    suspend fun eliminarProyecto(proyecto: ProyectoEntity) {
        try {
            if (proyecto.id > 0) {
                cimucApi.eliminarProyecto(proyecto.id.toLong())
            }
        } catch (e: Exception) {
            Log.e("ProyectoRepository", "Error eliminando en Render: ${e.message}")
        }
        proyectoDao.eliminarProyecto(proyecto)
    }
}