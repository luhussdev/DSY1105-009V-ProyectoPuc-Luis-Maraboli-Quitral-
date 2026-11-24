package com.example.proyectopuc_v10.model

data class ProyectoRemoto(
    val id: Long? = null,
    val titulo: String,
    val areaTematica: String,
    val descripcion: String,
    val investigadorPrincipal: String,
    val estado: String
)