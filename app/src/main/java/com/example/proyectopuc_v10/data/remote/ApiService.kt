package com.example.proyectopuc_v10.data.remote

import com.example.proyectopuc_v10.model.Post
import retrofit2.http.GET

interface ApiService {
    @GET("posts")
    suspend fun getPosts(): List<Post>
}