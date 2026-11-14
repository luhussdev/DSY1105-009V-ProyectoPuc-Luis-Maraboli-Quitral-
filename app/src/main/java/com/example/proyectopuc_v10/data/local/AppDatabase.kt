package com.example.proyectopuc_v10.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

@Database(entities = [ProyectoEntity::class, UsuarioEntity::class], version = 9, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun proyectoDao(): ProyectoDao
    abstract fun UsuarioDao(): UsuarioDao

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
            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                INSTANCE?.let { database ->
                    CoroutineScope(Dispatchers.IO).launch {
                        val dao = database.proyectoDao()
                        // Si la tabla está vacía tras migrar, repoblamos los datos
                        val proyectosExistentes = dao.obtenerTodosLosProyectos().firstOrNull()
                        if (proyectosExistentes.isNullOrEmpty()) {
                            precargarDatosIniciales(dao)
                        }
                    }
                }
            }
            suspend fun precargarDatosIniciales(dao: ProyectoDao) {
                val datosIniciales = listOf(
                    ProyectoEntity(
                        titulo = "Juan José Ugarte asume como rector de Duoc UC",
                        areaTematica = "Institucional y Liderazgo",
                        descripcion = "Tras liderar el CIM UC durante 16 años y presidir CORMA, el arquitecto y académico FADEU UC asume la rectoría de Duoc UC. Su gestión impulsará la formación técnico-profesional orientada a la innovación en edificación sustentable.",
                        liderInvestigacion = "Dr. Juan José Ugarte",
                        anio = 2026
                    ),
                    ProyectoEntity(
                        titulo = "Ciclo de Webinars: Normativas para Construir en Madera",
                        areaTematica = "Transferencia y Normativa",
                        descripcion = "Exitoso ciclo técnico de 5 sesiones junto a CENAMAD y Madera21 que convocó a más de 700 asistentes en vivo. Se abordaron actualizaciones clave en diseño sísmico (NCh433), durabilidad (NCh789/NCh819), desempeño térmico, CLT y seguridad contra incendios.",
                        liderInvestigacion = "Sebastián Berwart & Fabiana Lorca",
                        anio = 2026
                    ),
                    ProyectoEntity(
                        titulo = "Ditec y CIM UC capacitan a equipos técnicos en CLT",
                        areaTematica = "Capacitación y Normativa",
                        descripcion = "Ciclo 2026 de capacitaciones junto a Ditec Minvu para la incorporación de la Madera Contralaminada (CLT) en viviendas. Incluye transferencias en cálculo estructural, diseño sísmico y montaje práctico con el sistema Kits CLT.",
                        liderInvestigacion = "Francisca Lorenzini & Diego Valdivieso",
                        anio = 2026
                    ),
                )
                datosIniciales.forEach { dao.insertarProyecto(it) }
            }
        }
    }
}