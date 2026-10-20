package com.example.proyectopuc_v10.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "proyectos")
data class ProyectoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val titulo: String,
    val areaTematica: String,
    val descripcion: String,
    val liderInvestigacion: String,
    val anio: Int
)
