package com.example.proyectopuc_v10.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectopuc_v10.data.PostRepository
import com.example.proyectopuc_v10.model.Post
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PostViewModel : ViewModel() {

    companion object {
        private const val TAG = "API_REST_VIEWMODEL"
    }

    private val repository = PostRepository()

    private val _postList = MutableStateFlow<List<Post>>(emptyList())
    val postList: StateFlow<List<Post>> = _postList

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _operationMessage = MutableStateFlow<String?>(null)
    val operationMessage: StateFlow<String?> = _operationMessage

    init {
        fetchPosts()
    }

    fun clearOperationMessage() {
        _operationMessage.value = null
    }

    // READ (GET)
    fun fetchPosts() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                _postList.value = repository.getPosts()
                Log.d(TAG, "Estado UI actualizado con ${_postList.value.size} posts.")
            } catch (e: Exception) {
                Log.e(TAG, "Fallo en fetchPosts: ${e.message}", e)
                _errorMessage.value = "Error de conexión: ${e.localizedMessage ?: e.javaClass.simpleName}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // CREATE (POST)
    fun createPost(title: String, body: String) {
        viewModelScope.launch {
            try {
                val nuevoPost = Post(userId = 1, id = 0, title = title, body = body)
                val creado = repository.createPost(nuevoPost)
                // Lo agregamos al inicio de la lista en pantalla para visualizarlo de inmediato
                _postList.value = listOf(creado) + _postList.value
                _operationMessage.value = "Post creado (ID remoto: ${creado.id})"
                Log.d(TAG, "UI actualizada tras POST: ID=${creado.id}")
            } catch (e: Exception) {
                Log.e(TAG, "Fallo en createPost: ${e.message}", e)
                _operationMessage.value = "Error al crear post: ${e.localizedMessage}"
            }
        }
    }

    // UPDATE (PUT)
    fun updatePost(post: Post, newTitle: String, newBody: String) {
        viewModelScope.launch {
            try {
                val postEditado = post.copy(title = newTitle, body = newBody)
                val actualizado = repository.updatePost(post.id, postEditado)
                // Reemplazamos el elemento modificado en la lista actual
                _postList.value = _postList.value.map {
                    if (it.id == post.id) actualizado else it
                }
                _operationMessage.value = "Post ID=${post.id} actualizado"
                Log.d(TAG, "UI actualizada tras PUT: ID=${post.id}")
            } catch (e: Exception) {
                Log.e(TAG, "Fallo en updatePost: ${e.message}", e)
                _operationMessage.value = "Error al actualizar: ${e.localizedMessage}"
            }
        }
    }

    // DELETE
    fun deletePost(id: Int) {
        viewModelScope.launch {
            try {
                val eliminado = repository.deletePost(id)
                if (eliminado) {
                    _postList.value = _postList.value.filterNot { it.id == id }
                    _operationMessage.value = "Post ID=$id eliminado"
                    Log.d(TAG, "UI actualizada tras DELETE: ID=$id removido")
                } else {
                    _operationMessage.value = "No se pudo eliminar el post ID=$id"
                }
            } catch (e: Exception) {
                Log.e(TAG, "Fallo en deletePost: ${e.message}", e)
                _operationMessage.value = "Error al eliminar: ${e.localizedMessage}"
            }
        }
    }
}