package com.example.proyectopuc_v10.model
data class FormularioUiState (
    val nombre: String = "",
    val correo: String = "",
    val organizacion: String = "",
    val areaInteres: String = "",
    val aceptaTerminos: Boolean = false,
    val errores: FormularioErrores = FormularioErrores()
)
