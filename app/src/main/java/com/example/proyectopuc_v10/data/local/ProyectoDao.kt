package com.example.proyectopuc_v10.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ProyectoDao {
    // Read: Obtener todos los proyectos en tiempo real mediante Flow
    @Query("SELECT * FROM proyectos ORDER BY id DESC")
    fun obtenerTodosLosProyectos(): Flow<List<ProyectoEntity>>

    // Read: Consultar un proyecto especifico por su ID
    @Query("SELECT * FROM proyectos WHERE id = :id")
    suspend fun obtenerProyectoPorId(id: Int): ProyectoEntity?

    // Create: Insertar un nuevo proyecto
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarProyecto(proyecto: ProyectoEntity)

    // Delete: Eliminar un proyecto
    @Delete
    suspend fun eliminarProyecto(proyecto: ProyectoEntity)
}