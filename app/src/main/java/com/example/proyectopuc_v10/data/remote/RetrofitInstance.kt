package com.example.proyectopuc_v10.data.remote

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitInstance {
    // 1. API Externa (JSONPlaceholder - Requisito APIs externas)
    private const val BASE_URL_EXTERNA = "https://jsonplaceholder.typicode.com/"

    // 2. Microservicio propio Spring Boot en Render
    private const val BASE_URL_CIMUC = "https://cimuc-api.onrender.com/"

    private val okHttpClientRender = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL_EXTERNA)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    val cimucApi: CimucApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL_CIMUC)
            .client(okHttpClientRender)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CimucApiService::class.java)
    }
}