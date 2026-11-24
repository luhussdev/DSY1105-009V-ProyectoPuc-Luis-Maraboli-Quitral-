package com.example.proyectopuc_v10

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.preferencesOf
import com.example.proyectopuc_v10.data.EstadoDataStore
import com.example.proyectopuc_v10.data.PostRepository
import com.example.proyectopuc_v10.data.ProyectoRepository
import com.example.proyectopuc_v10.data.dataStore
import com.example.proyectopuc_v10.data.local.ProyectoDao
import com.example.proyectopuc_v10.data.local.ProyectoEntity
import com.example.proyectopuc_v10.data.remote.ApiService
import com.example.proyectopuc_v10.data.remote.CimucApiService
import com.example.proyectopuc_v10.data.remote.RetrofitInstance
import com.example.proyectopuc_v10.model.Post
import com.example.proyectopuc_v10.model.ProyectoRemoto
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response
import java.io.IOException

class DataRepositoriesTest : StringSpec({

    lateinit var mockApi: ApiService
    lateinit var mockCimucApi: CimucApiService
    lateinit var mockProyectoDao: ProyectoDao

    beforeTest {
        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0
        every { Log.w(any(), any<String>()) } returns 0
        every { Log.e(any(), any()) } returns 0
        every { Log.e(any(), any(), any()) } returns 0

        mockApi = mockk()
        mockCimucApi = mockk()
        mockProyectoDao = mockk(relaxed = true)

        mockkObject(RetrofitInstance)
        every { RetrofitInstance.api } returns mockApi
        every { RetrofitInstance.cimucApi } returns mockCimucApi
    }

    afterTest {
        unmockkAll()
    }

    // ==================== 1. POST REPOSITORY (ÉXITO Y ERRORES) ====================
    "PostRepository: ejecuta CRUD completo (GET, POST, PUT, DELETE) exitosamente" {
        runTest {
            val repo = PostRepository()
            val samplePost = Post(userId = 1, id = 1, title = "Titulo", body = "Contenido")

            coEvery { mockApi.getPosts() } returns listOf(samplePost)
            coEvery { mockApi.createPost(samplePost) } returns samplePost.copy(id = 101)
            coEvery { mockApi.updatePost(1, samplePost) } returns samplePost.copy(title = "Editado")
            coEvery { mockApi.deletePost(1) } returns Response.success(Unit)

            repo.getPosts() shouldHaveSize 1
            repo.createPost(samplePost).id shouldBe 101
            repo.updatePost(1, samplePost).title shouldBe "Editado"
            repo.deletePost(1).shouldBeTrue()
        }
    }

    "PostRepository: maneja respuesta HTTP fallida en deletePost y relanza excepciones de red" {
        runTest {
            val repo = PostRepository()
            val samplePost = Post(userId = 1, id = 1, title = "Titulo", body = "Contenido")

            coEvery { mockApi.deletePost(99) } returns Response.error(404, "Not Found".toResponseBody())
            repo.deletePost(99).shouldBeFalse()

            coEvery { mockApi.getPosts() } throws IOException("Sin red")
            shouldThrow<IOException> { repo.getPosts() }

            coEvery { mockApi.createPost(any()) } throws IOException("Sin red")
            shouldThrow<IOException> { repo.createPost(samplePost) }

            coEvery { mockApi.updatePost(any(), any()) } throws IOException("Sin red")
            shouldThrow<IOException> { repo.updatePost(1, samplePost) }

            coEvery { mockApi.deletePost(1) } throws IOException("Sin red")
            shouldThrow<IOException> { repo.deletePost(1) }
        }
    }

    // ==================== 2. PROYECTO REPOSITORY (ONLINE + OFFLINE) ====================
    "ProyectoRepository: sincroniza microservicio Render con Room y consulta por ID" {
        runTest {
            val existente = ProyectoEntity(1, "Viejo", "Area", "Desc", "Lider", 2025)
            val remoto = ProyectoRemoto(
                id = 10L,
                titulo = "Nuevo Render",
                areaTematica = "Sismica",
                descripcion = "Ensayo",
                investigadorPrincipal = "Dr. Soto",
                estado = "Activo"
            )

            every { mockProyectoDao.obtenerTodosLosProyectos() } returns flowOf(listOf(existente))
            coEvery { mockCimucApi.obtenerProyectos() } returns listOf(remoto)
            coEvery { mockProyectoDao.obtenerProyectoPorId(10) } returns existente.copy(id = 10)

            val repo = ProyectoRepository(mockProyectoDao, mockCimucApi)
            repo.sincronizarConMicroservicio()

            coVerify { mockProyectoDao.eliminarProyecto(existente) }
            coVerify { mockProyectoDao.insertarProyecto(match { it.id == 10 && it.titulo == "Nuevo Render" }) }

            repo.obtenerProyectosPorId(10)?.id shouldBe 10
        }
    }

    "ProyectoRepository: maneja contingencia sin red en sincronizar, agregar y eliminar" {
        runTest {
            every { mockProyectoDao.obtenerTodosLosProyectos() } returns flowOf(emptyList())
            val repo = ProyectoRepository(mockProyectoDao, mockCimucApi)

            // Fallo en sincronización no rompe la app
            coEvery { mockCimucApi.obtenerProyectos() } throws RuntimeException("Timeout Render")
            repo.sincronizarConMicroservicio()

            // Agregar proyecto con éxito remoto
            coEvery { mockCimucApi.crearProyecto(any()) } returns ProyectoRemoto(
                id = 55L, titulo = "T1", areaTematica = "A1", descripcion = "D1", investigadorPrincipal = "L1", "En ejecución"
            )
            repo.agregarProyecto("T1", "A1", "D1", "L1", 2026)
            coVerify { mockProyectoDao.insertarProyecto(match { it.id == 55 }) }

            // Agregar proyecto offline (fallback id = 0)
            coEvery { mockCimucApi.crearProyecto(any()) } throws IOException("Offline")
            repo.agregarProyecto("T2", "A2", "D2", "L2", 2026)
            coVerify { mockProyectoDao.insertarProyecto(match { it.id == 0 && it.titulo == "T2" }) }

            // Eliminar proyecto (éxito remoto + fallo remoto controlado)
            val entidad = ProyectoEntity(55, "T1", "A1", "D1", "L1", 2026)
            coEvery { mockCimucApi.eliminarProyecto(55L) } throws IOException("Offline")
            repo.eliminarProyecto(entidad)
            coVerify { mockProyectoDao.eliminarProyecto(entidad) }
        }
    }

    // ==================== 3. ESTADO DATASTORE ====================
    "EstadoDataStore: lee el valor booleano almacenado desde Preferences DataStore" {
        runTest {
            mockkStatic("com.example.proyectopuc_v10.data.EstadoDataStoreKt")
            val mockContext = mockk<Context>(relaxed = true)
            val mockDataStore = mockk<DataStore<Preferences>>()
            val key = booleanPreferencesKey("modo_activado")

            every { mockContext.dataStore } returns mockDataStore
            every { mockDataStore.data } returns flowOf(preferencesOf(key to true))

            val estadoDataStore = EstadoDataStore(mockContext)
            estadoDataStore.obtenerEstado().first() shouldBe true
        }
    }
})