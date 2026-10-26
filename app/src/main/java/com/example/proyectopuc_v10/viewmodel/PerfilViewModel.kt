package com.example.proyectopuc_v10.viewmodel

import android.app.Application
import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectopuc_v10.data.local.AppDatabase
import com.example.proyectopuc_v10.data.local.UsuarioEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import java.io.File

class PerfilViewModel (application: Application) : AndroidViewModel(application) {

    private val usuarioDao = AppDatabase.obtenerBaseDatos(application).UsuarioDao()

    // Carga en tiempo real el último usuario insertado en Room
    val usuarioState: StateFlow<UsuarioEntity?> = usuarioDao.obtenerUltimoUsuario()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

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