package com.example.proyectopuc_v10.data

import com.example.proyectopuc_v10.data.remote.RetrofitInstance
import com.example.proyectopuc_v10.model.Post

class PostRepository {
    suspend fun getPosts(): List<Post> {
        return RetrofitInstance.api.getPosts()
    }
}