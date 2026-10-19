package com.example.gestionmovil.data.local

import android.content.Context
import androidx.room3.Database
import androidx.room3.Room
import androidx.room3.RoomDatabase

// DAOs
import com.example.gestionmovil.data.local.dao.OportunidadDao
import com.example.gestionmovil.data.local.dao.OrganizacionDao
import com.example.gestionmovil.data.local.dao.SeguimientoDao
import com.example.gestionmovil.data.local.dao.UsuarioDao

// Entidades
import com.example.gestionmovil.data.local.entities.ContactoEntity
import com.example.gestionmovil.data.local.entities.OportunidadEntity
import com.example.gestionmovil.data.local.entities.OrganizacionEntity
import com.example.gestionmovil.data.local.entities.SeguimientoEntity
import com.example.gestionmovil.data.local.entities.UsuarioEntity

// Imports para la precarga de datos y corrutinas:
import androidx.sqlite.SQLiteConnection
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.example.gestionmovil.data.local.entities.RolUsuarioEntity

@Database(
    entities = [
        OrganizacionEntity::class,
        ContactoEntity::class,
        OportunidadEntity::class,
        SeguimientoEntity::class,
        UsuarioEntity::class
    ],
    version = 4,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun usuarioDao(): UsuarioDao
    abstract fun seguimientoDao(): SeguimientoDao
    abstract fun oportunidadDao(): OportunidadDao
    abstract fun organizacionDao(): OrganizacionDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "gestion_movil_db"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(AppDatabaseCallback())
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class AppDatabaseCallback : RoomDatabase.Callback() {
            override suspend fun onOpen(connection: SQLiteConnection) {
                super.onOpen(connection)

                // Disparamos en un hilo de E/S desacoplado
                CoroutineScope(Dispatchers.IO).launch {
                    // Esperamos a que la instancia Singleton esté asignada
                    var db = INSTANCE
                    var retries = 0
                    while (db == null && retries < 10) {
                        kotlinx.coroutines.delay(100)
                        db = INSTANCE
                        retries++
                    }

                    db?.let { database ->
                        val usuarioDao = database.usuarioDao()
                        val adminExistente = usuarioDao.obtenerPorEmail("admin@empresa.cl")
                        if (adminExistente == null) {
                            poblarCuentasIniciales(usuarioDao)
                        }
                    }
                }
            }

            suspend fun poblarCuentasIniciales(usuarioDao: UsuarioDao) {
                val passwordHasheada = com.example.gestionmovil.utils.Seguridad.hashPassword("Clave123+")

                val cuentasPrueba = listOf(
                    UsuarioEntity(
                        pNombre = "Admin",
                        pApellido = "Sistema",
                        email = "admin@empresa.cl",
                        password = passwordHasheada,
                        rol = RolUsuarioEntity.ADMIN.name
                    ),
                    UsuarioEntity(
                        pNombre = "Carlos",
                        pApellido = "Mendoza",
                        email = "supervisor@empresa.cl",
                        password = passwordHasheada,
                        rol = RolUsuarioEntity.SUPERVISOR.name
                    ),
                    UsuarioEntity(
                        pNombre = "Ana",
                        pApellido = "López",
                        email = "ejecutivo1@empresa.cl",
                        password = passwordHasheada,
                        rol = RolUsuarioEntity.EJECUTIVO.name
                    ),
                    UsuarioEntity(
                        pNombre = "Mateo",
                        pApellido = "Silva",
                        email = "ejecutivo2@empresa.cl",
                        password = passwordHasheada,
                        rol = RolUsuarioEntity.EJECUTIVO.name
                    ),
                    UsuarioEntity(
                        pNombre = "Sofía",
                        pApellido = "Rojas",
                        email = "ejecutivo3@empresa.cl",
                        password = passwordHasheada,
                        rol = RolUsuarioEntity.EJECUTIVO.name
                    )
                )
                usuarioDao.insertarUsuarios(cuentasPrueba)
            }
        }
    }
}