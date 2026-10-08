package com.example.proyectopuc_v10.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation

@Composable
fun CampoTexto(
    valor: String,
    onValueChange: (String) -> Unit,
    label: String,
    error: String? = null,
    esPassword: Boolean = false,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = valor,
        onValueChange = onValueChange,
        label = { Text(label) },
        isError = error != null,
        visualTransformation = if (esPassword) PasswordVisualTransformation() else VisualTransformation.None,
        supportingText = {
            error?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error
                )
            }
        },
        singleLine = true,
        modifier = modifier.fillMaxWidth()
    )
}