package com.example.proyectopuc_v10.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [ProyectoEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun proyectoDao(): ProyectoDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun obtenerBaseDatos(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "cim_uc_database"
                )
                    .fallbackToDestructiveMigration(dropAllTables = true)
                    .addCallback(AppDatabaseCallback())
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class AppDatabaseCallback : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    CoroutineScope(Dispatchers.IO).launch {
                        precargarDatosIniciales(database.proyectoDao())
                    }
                }
            }
            suspend fun precargarDatosIniciales(dao: ProyectoDao) {
                val datosIniciales = listOf(
                    ProyectoEntity(
                        titulo = "Torre Peñuelas",
                        areaTematica = "Construcción en Alturta",
                        descripcion = "Primer edificio en madera laminada de 6 pisos en Chile para validar eficiencia térmica y acústica.",
                        liderInvestigacion = "Dr. Juan José Ugarte",
                        anio = 2021
                    ),
                    ProyectoEntity(
                        titulo = "Vivienda Social Industrializada",
                        areaTematica = "Hábitat Sustentable",
                        descripcion = "Sistema modular de rápida construcción con altos estándares energéticos para emergencias.",
                        liderInvestigacion = "Arq. Felipe Victorero",
                        anio = 2023
                    ),
                    ProyectoEntity(
                        titulo = "Protección Retardante de Fuego",
                        areaTematica = "Biomateriales",
                        descripcion = "Desarrollo de recubrimientos no tóxicos basados en lignina para mejorar resistencia al fuego.",
                        liderInvestigacion = "Dra. María Carmen Mingo",
                        anio = 2024
                    )
                )
                datosIniciales.forEach { dao.insertarProyecto(it) }
            }
        }
    }
}