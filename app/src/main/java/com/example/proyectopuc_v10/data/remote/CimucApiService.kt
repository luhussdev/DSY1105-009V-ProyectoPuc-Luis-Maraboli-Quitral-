package com.example.proyectopuc_v10.data.remote

import com.example.proyectopuc_v10.model.ProyectoRemoto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface CimucApiService {

    @GET("api/proyectos")
    suspend fun obtenerProyectos(): List<ProyectoRemoto>

    @POST("api/proyectos")
    suspend fun crearProyecto(@Body proyecto: ProyectoRemoto): ProyectoRemoto

    @PUT("api/proyectos/{id}")
    suspend fun actualizarProyecto(
        @Path("id") id: Long,
        @Body proyecto: ProyectoRemoto
    ): ProyectoRemoto

    @DELETE("api/proyectos/{id}")
    suspend fun eliminarProyecto(@Path("id") id: Long): Response<Unit>
}