package com.example.proyectopuc_v10.model
data class FormularioUiState (

    val nombre: String = "",
    val correo: String = "",
    val clave: String = "",
    val direccion: String = "",
    val aceptaTerminos: Boolean = false,
    val errores: FormularioErrores = FormularioErrores()
)
