package com.example.proyectopuc_v10

import android.app.Application
import android.net.Uri
import android.util.Log
import android.util.Patterns
import com.example.proyectopuc_v10.data.PostRepository
import com.example.proyectopuc_v10.data.ProyectoRepository
import com.example.proyectopuc_v10.data.local.AppDatabase
import com.example.proyectopuc_v10.data.local.ProyectoDao
import com.example.proyectopuc_v10.data.local.ProyectoEntity
import com.example.proyectopuc_v10.data.local.UsuarioDao
import com.example.proyectopuc_v10.data.local.UsuarioEntity
import com.example.proyectopuc_v10.model.Post
import com.example.proyectopuc_v10.viewmodel.AuthViewModel
import com.example.proyectopuc_v10.viewmodel.EstadoViewModel
import com.example.proyectopuc_v10.viewmodel.PerfilViewModel
import com.example.proyectopuc_v10.viewmodel.PostViewModel
import com.example.proyectopuc_v10.viewmodel.ProyectoViewModel
import com.example.proyectopuc_v10.viewmodel.UsuarioViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.mockkObject
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import java.util.regex.Pattern
import kotlinx.coroutines.flow.flowOf
import com.example.proyectopuc_v10.data.EstadoDataStore

@OptIn(ExperimentalCoroutinesApi::class)
class ViewModelsCimUcTest : StringSpec({

    val testDispatcher = StandardTestDispatcher()
    lateinit var mockApp: Application
    lateinit var mockDb: AppDatabase
    lateinit var mockUsuarioDao: UsuarioDao
    lateinit var mockProyectoDao: ProyectoDao

    beforeTest {
        Dispatchers.setMain(testDispatcher)
        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0
        every { Log.w(any(), any<String>()) } returns 0
        every { Log.e(any(), any(), any()) } returns 0

        mockApp = mockk(relaxed = true)
        mockDb = mockk(relaxed = true)
        mockUsuarioDao = mockk(relaxed = true)
        mockProyectoDao = mockk(relaxed = true)

        mockkObject(AppDatabase.Companion)
        every { AppDatabase.obtenerBaseDatos(any()) } returns mockDb
        every { mockDb.UsuarioDao() } returns mockUsuarioDao
        every { mockDb.proyectoDao() } returns mockProyectoDao
    }

    afterTest {
        Dispatchers.resetMain()
        unmockkAll()
    }

    // ==================== 1. AUTH VIEWMODEL ====================
    "AuthViewModel: iniciarSesion valida credenciales correctas, incorrectas y cierra sesion" {
        runTest(testDispatcher) {
            coEvery { mockUsuarioDao.guardarUsuario(any()) } returns 1L
            val authViewModel = AuthViewModel(mockApp)

            // Credenciales incorrectas
            authViewModel.iniciarSesion("admin@cimuc.cl", "wrong").shouldBeFalse()
            authViewModel.authError.value shouldBe "Correo o contraseña incorrectos"
            authViewModel.clearError()
            authViewModel.authError.value.shouldBeNull()

            // Credenciales por defecto
            authViewModel.iniciarSesion("ADMIN@cimuc.cl", "123456").shouldBeTrue()
            authViewModel.usuarioActual.value.shouldNotBeNull()
            authViewModel.usuarioActual.value?.email shouldBe "admin@cimuc.cl"

            // Cerrar sesión
            authViewModel.cerrarSesion()
            authViewModel.usuarioActual.value.shouldBeNull()
        }
    }

    "AuthViewModel: registrarUsuario crea cuenta nueva, guarda en Room y bloquea duplicados" {
        runTest(testDispatcher) {
            coEvery { mockUsuarioDao.guardarUsuario(any()) } returns 2L
            val authViewModel = AuthViewModel(mockApp)

            val registrado = authViewModel.registrarUsuario(
                nombre = "Luis Maraboli",
                email = "luis@uc.cl",
                password = "pass123",
                organizacion = "",
                areaInteres = ""
            )
            advanceUntilIdle()

            registrado.shouldBeTrue()
            authViewModel.usuarioActual.value?.organizacion shouldBe "Independiente / CIM UC"
            authViewModel.usuarioActual.value?.especialidad shouldBe "Construcción en Madera"
            coVerify { mockUsuarioDao.guardarUsuario(any()) }

            // Correo duplicado
            val duplicado = authViewModel.registrarUsuario(
                nombre = "Otro",
                email = "luis@uc.cl",
                password = "pass123",
                organizacion = "UC",
                areaInteres = "Madera"
            )
            duplicado.shouldBeFalse()
            authViewModel.authError.value shouldBe "Ya existe una cuenta con ese correo"
        }
    }

    // ==================== 2. USUARIO VIEWMODEL ====================
    "UsuarioViewModel: actualiza campos reactivos y valida formulario completo" {
        runTest(testDispatcher) {
            coEvery { mockUsuarioDao.guardarUsuario(any()) } returns 5L

            val vm = UsuarioViewModel(mockApp)
            vm.onNombreCambiado("Lu")
            vm.onCorreoCambiado("correo_invalido")
            vm.onOrganizacionCambiada("")
            vm.onAreaInteresCambiada("")
            vm.onTerminosCambiados(true)

            // Validación fallida
            vm.validarFormulario("Lu", "correo_invalido", "", "").shouldBeFalse()
            vm.uiState.value.errores.nombre.shouldNotBeNull()
            vm.uiState.value.errores.correo.shouldNotBeNull()
            vm.uiState.value.errores.organizacion.shouldNotBeNull()
            vm.uiState.value.errores.areaInteres.shouldNotBeNull()

            // Validación exitosa
            vm.validarFormulario(
                nombre = "Luis Maraboli",
                correo = "luis@cimuc.cl",
                organizacion = "CIM UC",
                areaInteres = "Estructuras"
            ).shouldBeTrue()
            vm.uiState.value.errores.nombre.shouldBeNull()

            vm.guardarUsuarioEnRoom("Luis Maraboli", "luis@cimuc.cl", "CIM UC", "Estructuras", true)
            advanceUntilIdle()
            coVerify { mockUsuarioDao.guardarUsuario(any()) }
        }
    }

    // ==================== 3. PROYECTO VIEWMODEL ====================
    "ProyectoViewModel: sincroniza proyectos con microservicio y agrega nuevo proyecto" {
        runTest(testDispatcher) {
            val flujoProyectos = MutableStateFlow(
                listOf(
                    ProyectoEntity(
                        id = 1,
                        titulo = "Torre Madera",
                        areaTematica = "Estructuras",
                        descripcion = "Edificio en altura",
                        liderInvestigacion = "Dr. Silva",
                        anio = 2026
                    )
                )
            )
            every { mockProyectoDao.obtenerTodosLosProyectos() } returns flujoProyectos

            mockkConstructor(ProyectoRepository::class)
            coEvery { anyConstructed<ProyectoRepository>().sincronizarConMicroservicio() } returns Unit
            coEvery {
                anyConstructed<ProyectoRepository>().agregarProyecto(any(), any(), any(), any(), any())
            } returns Unit

            val vm = ProyectoViewModel(mockApp)
            advanceUntilIdle()

            val lista = vm.proyectos.first { it.isNotEmpty() }
            lista shouldHaveSize 1
            lista.first().titulo shouldBe "Torre Madera"
            vm.sincronizando.value.shouldBeFalse()

            vm.agregarProyecto("Nuevo", "Sismica", "Desc", "Dra. Perez", 2026)
            advanceUntilIdle()
            coVerify {
                anyConstructed<ProyectoRepository>().agregarProyecto("Nuevo", "Sismica", "Desc", "Dra. Perez", 2026)
            }
        }
    }

    // ==================== 4. PERFIL VIEWMODEL ====================
    "PerfilViewModel: actualiza imagen URI y persiste cambios de perfil en Room" {
        runTest(testDispatcher) {
            val usuarioInicial = UsuarioEntity(
                id = 10,
                nombre = "Luis",
                correo = "old@uc.cl",
                organizacion = "UC",
                areaInteres = "Madera",
                aceptaTerminos = true,
                estaSuscrito = true
            )
            every { mockUsuarioDao.obtenerUltimoUsuario() } returns MutableStateFlow(usuarioInicial)
            coEvery { mockUsuarioDao.guardarUsuario(any()) } returns 10L

            val vm = PerfilViewModel(mockApp)
            val fakeUri = mockk<Uri>()
            vm.actualizarImagen(fakeUri)
            vm.imagenUri.value shouldBe fakeUri

            // Suscribir el StateFlow para que tome usuarioInicial
            vm.usuarioState.first { it != null }
            vm.actualizarPerfil("nuevo@uc.cl", "CIM UC", "Bioingenieria")
            advanceUntilIdle()

            coVerify {
                mockUsuarioDao.guardarUsuario(
                    match { it.correo == "nuevo@uc.cl" && it.organizacion == "CIM UC" }
                )
            }
        }
    }

    // ==================== 5. ESTADO VIEWMODEL ====================
    "EstadoViewModel: carga estado inicial y alterna interruptor con mensaje temporal" {
        runTest(testDispatcher) {
            mockkConstructor(EstadoDataStore::class)
            every { anyConstructed<EstadoDataStore>().obtenerEstado() } returns flowOf(false)
            coEvery { anyConstructed<EstadoDataStore>().guardarEstado(any()) } returns Unit

            val vm = EstadoViewModel(mockApp)
            advanceTimeBy(1600)
            advanceUntilIdle()

            vm.activo.value shouldBe false

            vm.alternarEstado()
            advanceTimeBy(100)
            vm.activo.value shouldBe true
            vm.mostrarMensaje.value.shouldBeTrue()

            advanceTimeBy(2100)
            vm.mostrarMensaje.value.shouldBeFalse()
        }
    }

    // ==================== 6. POST VIEWMODEL CRUD COMPLETO ====================
    "PostViewModel: ejecuta CRUD completo (GET, POST, PUT, DELETE) sobre API externa" {
        runTest(testDispatcher) {
            val postInicial = Post(userId = 1, id = 1, title = "Titulo 1", body = "Cuerpo 1")
            val postCreado = Post(userId = 1, id = 101, title = "Nuevo Post", body = "Contenido")
            val postActualizado = postInicial.copy(title = "Editado", body = "Cuerpo Editado")

            mockkConstructor(PostRepository::class)
            coEvery { anyConstructed<PostRepository>().getPosts() } returns listOf(postInicial)
            coEvery { anyConstructed<PostRepository>().createPost(any()) } returns postCreado
            coEvery { anyConstructed<PostRepository>().updatePost(any(), any()) } returns postActualizado
            coEvery { anyConstructed<PostRepository>().deletePost(any()) } returns true

            val vm = PostViewModel()
            vm.fetchPosts()
            advanceUntilIdle()
            vm.postList.value shouldHaveSize 1

            // CREATE
            vm.createPost("Nuevo Post", "Contenido")
            advanceUntilIdle()
            vm.postList.value shouldHaveSize 2
            vm.operationMessage.value shouldBe "Post creado (ID remoto: 101)"

            // UPDATE
            vm.updatePost(postInicial, "Editado", "Cuerpo Editado")
            advanceUntilIdle()
            vm.postList.value.any { it.title == "Editado" }.shouldBeTrue()

            // DELETE
            vm.deletePost(1)
            advanceUntilIdle()
            vm.postList.value.any { it.id == 1 }.shouldBeFalse()
            vm.clearOperationMessage()
            vm.operationMessage.value.shouldBeNull()
        }
    }
})