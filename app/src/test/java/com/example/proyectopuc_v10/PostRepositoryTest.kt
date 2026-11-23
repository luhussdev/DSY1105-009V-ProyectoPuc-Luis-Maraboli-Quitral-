package com.example.proyectopuc_v10

import com.example.proyectopuc_v10.data.PostRepository
import com.example.proyectopuc_v10.data.remote.ApiService
import com.example.proyectopuc_v10.model.Post
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest

class TestablePostRepository(private val testApi: ApiService) : PostRepository() {
    override suspend fun getPosts(): List<Post> {
        return testApi.getPosts()
    }
}

class PostRepositoryTest : StringSpec({
    "getPosts() debe retornar una lista de posts simulada" {
        val fakePosts = listOf(
            Post(userId = 1, id = 1, title = "Título 1", body = "Cuerpo 1"),
            Post(userId = 2, id = 2, title = "Título 2", body = "Cuerpo 2")
        )

        val mockApi = mockk<ApiService>()
        coEvery { mockApi.getPosts() } returns fakePosts

        val repo = TestablePostRepository(testApi = mockApi)

        runTest {
            val result = repo.getPosts()
            result shouldContainExactly fakePosts
        }
    }
})