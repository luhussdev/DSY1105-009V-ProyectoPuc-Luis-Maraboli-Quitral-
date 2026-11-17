package com.example.proyectopuc_v10.data

import android.util.Log
import com.example.proyectopuc_v10.data.remote.RetrofitInstance
import com.example.proyectopuc_v10.model.Post

class PostRepository {

    companion object {
        private const val TAG = "API_REST_CRUD"
    }

    suspend fun getPosts(): List<Post> {
        Log.d(TAG, "[GET] Solicitando listado de posts a /posts...")
        return try {
            val response = RetrofitInstance.api.getPosts()
            Log.d(TAG, "[GET] Éxito: ${response.size} posts recibidos.")
            response
        } catch (e: Exception) {
            Log.e(TAG, "[GET] Error al obtener posts: ${e.message}", e)
            throw e
        }
    }

    suspend fun createPost(post: Post): Post {
        Log.d(TAG, "[POST] Enviando nuevo post -> Título: '${post.title}'")
        return try {
            val created = RetrofitInstance.api.createPost(post)
            Log.d(TAG, "[POST] Éxito: Post creado con ID remoto = ${created.id}")
            created
        } catch (e: Exception) {
            Log.e(TAG, "[POST] Error al crear post: ${e.message}", e)
            throw e
        }
    }

    suspend fun updatePost(id: Int, post: Post): Post {
        Log.d(TAG, "[PUT] Actualizando post ID=$id -> Nuevo título: '${post.title}'")
        return try {
            val updated = RetrofitInstance.api.updatePost(id, post)
            Log.d(TAG, "[PUT] Éxito: Post ID=${updated.id} actualizado correctamente.")
            updated
        } catch (e: Exception) {
            Log.e(TAG, "[PUT] Error al actualizar post ID=$id: ${e.message}", e)
            throw e
        }
    }

    suspend fun deletePost(id: Int): Boolean {
        Log.d(TAG, "[DELETE] Solicitando eliminación del post ID=$id...")
        return try {
            val response = RetrofitInstance.api.deletePost(id)
            if (response.isSuccessful) {
                Log.d(TAG, "[DELETE] Éxito: Post ID=$id eliminado (HTTP ${response.code()}).")
            } else {
                Log.w(TAG, "[DELETE] Fallo controlado para ID=$id (HTTP ${response.code()}).")
            }
            response.isSuccessful
        } catch (e: Exception) {
            Log.e(TAG, "[DELETE] Error de red al eliminar ID=$id: ${e.message}", e)
            throw e
        }
    }
}