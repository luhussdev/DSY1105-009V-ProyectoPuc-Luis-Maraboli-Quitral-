package com.example.proyectopuc_v10

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.example.proyectopuc_v10.model.Post
import com.example.proyectopuc_v10.ui.screens.PostScreen
import com.example.proyectopuc_v10.viewmodel.PostViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test

class PostScreenTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun el_titulo_de_post_debe_aparecer_en_pantalla() {
        val fakePosts = listOf(
            Post(userId = 1, id = 1, title = "Título 1", body = "Contenido 1"),
            Post(userId = 2, id = 2, title = "Título 2", body = "Contenido 2")
        )

        val fakeViewModel = object : PostViewModel() {
            override val postList = MutableStateFlow(fakePosts)
            override fun fetchPosts() {
                // Evita llamada real a red durante la prueba de UI
            }
        }

        composeRule.setContent {
            PostScreen(postViewModel = fakeViewModel)
        }

        composeRule.onNodeWithText("#1 - Título 1").assertIsDisplayed()
        composeRule.onNodeWithText("#2 - Título 2").assertIsDisplayed()
    }
}

