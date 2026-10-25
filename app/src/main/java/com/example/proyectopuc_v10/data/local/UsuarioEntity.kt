package com.example.proyectopuc_v10.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuarios")
data class UsuarioEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombre: String,
    val correo: String,
    val organizacion: String,
    val areaInteres: String,
    val aceptaTerminos: Boolean = true
)