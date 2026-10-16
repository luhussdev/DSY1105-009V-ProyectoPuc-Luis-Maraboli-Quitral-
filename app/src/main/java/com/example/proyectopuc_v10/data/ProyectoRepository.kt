package com.example.proyectopuc_v10.data

import com.example.proyectopuc_v10.model.ProyectoCim

object ProyectoRepository {
    fun obtenerProyectosDestacados(): List<ProyectoCim> {
        return listOf(
            ProyectoCim(
                id = 1,
                titulo = "Centro Comunitarios Resilientes",
                areaTematica = "Proyectos Destacados",
                descripcion = "Proyectos para las localidades de Penco y Lota, Región del Biobío.",
                liderInvestigacion = "Equipo CIM UC",
                anio = 2026
            ),
            ProyectoCim(
                id = 2,
                titulo = "Durabilidad Natural de la Madera",
                areaTematica = "Propiedades e Investigación",
                descripcion = "Clasificación, propiedades y evaluación del riesgo en estructuras de madera.",
                liderInvestigacion = "Mario Ubilla (Investigador CENAMAD)",
                anio = 2026
            ),
            ProyectoCim(
                id = 3,
                titulo = "Ciclo de Webinar: Normativas para Construir en Madera",
                areaTematica = "Noticias y Formación",
                descripcion = "Alcances de la normativas NCh789 y NCh819 para la durabilidad de la madera.",
                liderInvestigacion = "CENAMAD / CIM UC",
                anio = 2026
            )
        )
    }
}