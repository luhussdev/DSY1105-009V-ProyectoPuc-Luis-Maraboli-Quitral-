package com.example.proyectopuc_v10.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UsuarioDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun guardarUsuario(usuario: UsuarioEntity): Long

    @Query("SELECT * FROM usuarios ORDER BY id DESC LIMIT 1")
    fun obtenerUltimoUsuario(): Flow<UsuarioEntity?>

    @Query("SELECT * FROM usuarios")
    fun ObtenerTodosLosUsuarios(): Flow<List<UsuarioEntity>>
}