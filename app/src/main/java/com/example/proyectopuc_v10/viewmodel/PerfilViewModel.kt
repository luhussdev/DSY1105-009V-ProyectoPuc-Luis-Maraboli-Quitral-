package com.example.proyectopuc_v10.viewmodel

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.File

class PerfilViewModel : ViewModel() {

    // URI de la imagen seleccionada o capturada
    private val _imagenUri = MutableStateFlow<Uri?>(null)
    val imagenUri: StateFlow<Uri?> = _imagenUri.asStateFlow()

    // Guardar URI proveniente de la Galeria o Cámara
    fun actualizarImagen(uri: Uri?) {
        _imagenUri.value = uri
    }

    // Generar un Uri temporal seguro utilizando FileProvider para la Cámara
    fun crearUriTemporal(context: Context): Uri {
        val directorio = File(context.getExternalFilesDir(null), "Pictures")
        if (!directorio.exists()) {
            directorio.mkdirs()
        }
        val archivo = File.createTempFile(
            "Foto_perfil_${System.currentTimeMillis()}",
            ".jpg",
            directorio
        )
        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            archivo
        )
    }
}