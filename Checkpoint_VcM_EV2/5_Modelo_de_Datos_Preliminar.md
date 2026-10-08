# 5. Versión Preliminar del Modelo de Datos

El modelo de datos se ha diseñado utilizando Kotlin Data Classes para representar las entidades del dominio de la aplicación, manteniendo una estructura limpia e independiente de la fuente de origen (MOCK o API REST futura).

## 5.1 Diagrama Entidad-Relación Conceptual

+------------------+         1..*   +------------------+
|   ContenidoCIM   |---------------->|   AreaTematica   |
+------------------+                +------------------+
| id: String       |
| titulo: String   |         1..*   +------------------+
| integranteId     |--------------->|    Integrante    |
+------------------+                +------------------+
|                                   |
| 0..1                              | 1..*
v                                   v
+------------------+                +------------------+
| SolicitudContacto|                |     Proyecto     |
+------------------+                +------------------+


## 5.2 Definición de Estructuras en Kotlin

```kotlin
package com.example.mitercerproyecto.model

// Enum para tipificar el tipo de contenido
enum class TipoContenido {
    PROYECTO,
    CAPACIDAD_ID,
    PUBLICACION,
    ACTIVIDAD
}

// Entidad Principal del Catálogo Unificado
data class ContenidoCIM(
    val id: String,
    val titulo: String,
    val descripcionBreve: String,
    val descripcionDetallada: String,
    val tipo: TipoContenido,
    val areaTematica: String,
    val anio: Int?,
    val estado: String?, // ej. "En ejecución", "Finalizado", "Disponible"
    val integranteId: String?, // Relación con el integrante responsable
    val imagenUrl: String, // Referencia a drawable o URL ficticia
    val esFavorito: Boolean = false
)

// Entidad Integrante / Investigador
data class Integrante(
    val id: String,
    val nombre: String,
    val cargo: String,
    val correoContactoSimulado: String,
    val areaEspecialidad: String,
    val fotoUrl: String
)

// Entidad Formulario de Contacto
data class SolicitudContacto(
    val id: String,
    val nombreRemitente: String,
    val correoRemitente: String,
    val mensaje: String,
    val contenidoIdRelacionado: String? = null,
    val fechaEnvio: String
)