package com.example.proyectopuc_v10

import com.example.proyectopuc_v10.data.ProyectoRepository
import com.example.proyectopuc_v10.data.local.ProyectoDao
import com.example.proyectopuc_v10.data.local.ProyectoEntity
import com.example.proyectopuc_v10.data.remote.CimucApiService
import com.example.proyectopuc_v10.model.ProyectoRemoto
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.flow.flowOf

class ProyectoRepositoryTest : StringSpec({

    "sincronizarConMicroservicio debe obtener proyectos de Render y guardarlos en Room" {
        val mockDao = mockk<ProyectoDao>(relaxed = true)
        val mockApi = mockk<CimucApiService>()

        val proyectosRender = listOf(
            ProyectoRemoto(
                id = 1L,
                titulo = "Edificación en Altura CLT",
                areaTematica = "Construcción Industrializada",
                descripcion = "Sistemas híbridos en madera masiva",
                investigadorPrincipal = "Dr. Juan José Ugarte",
                estado = "En ejecución"
            )
        )

        coEvery { mockApi.obtenerProyectos() } returns proyectosRender
        coEvery { mockDao.obtenerTodosLosProyectos() } returns flowOf(emptyList())

        val repository = ProyectoRepository(mockDao, mockApi)
        repository.sincronizarConMicroservicio()

        val entidadCapturada = slot<ProyectoEntity>()
        coVerify(exactly = 1) { mockDao.insertarProyecto(capture(entidadCapturada)) }
        entidadCapturada.captured.titulo shouldBe "Edificación en Altura CLT"
        entidadCapturada.captured.liderInvestigacion shouldBe "Dr. Juan José Ugarte"
    }

    "sincronizarConMicroservicio debe tolerar errores de red sin lanzar excepciones" {
        val mockDao = mockk<ProyectoDao>(relaxed = true)
        val mockApi = mockk<CimucApiService>()

        coEvery { mockApi.obtenerProyectos() } throws RuntimeException("Timeout Render")

        val repository = ProyectoRepository(mockDao, mockApi)
        repository.sincronizarConMicroservicio()

        coVerify(exactly = 0) { mockDao.insertarProyecto(any()) }
    }

    "agregarProyecto debe enviar el registro al microservicio Spring Boot y guardarlo en Room" {
        val mockDao = mockk<ProyectoDao>(relaxed = true)
        val mockApi = mockk<CimucApiService>()

        coEvery { mockApi.crearProyecto(any()) } returns ProyectoRemoto(
            id = 10L,
            titulo = "Nuevo Proyecto Madera",
            areaTematica = "Sustentabilidad",
            descripcion = "Ensayo térmico",
            investigadorPrincipal = "Equipo CIM UC",
            estado = "En ejecución"
        )

        val repository = ProyectoRepository(mockDao, mockApi)
        repository.agregarProyecto(
            titulo = "Nuevo Proyecto Madera",
            areaTematica = "Sustentabilidad",
            descripcion = "Ensayo térmico",
            liderInvestigacion = "Equipo CIM UC",
            anio = 2026
        )

        coVerify(exactly = 1) { mockApi.crearProyecto(any()) }
        coVerify(exactly = 1) { mockDao.insertarProyecto(match { it.id == 10 && it.titulo == "Nuevo Proyecto Madera" }) }
    }
})