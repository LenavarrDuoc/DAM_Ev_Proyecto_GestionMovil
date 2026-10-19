package com.example.gestionmovil.data.local

import android.content.Context
import androidx.room3.Database
import androidx.room3.Room
import androidx.room3.RoomDatabase
import com.example.gestionmovil.data.local.dao.UsuarioDao

// DAOs
import com.example.gestionmovil.data.local.dao.OportunidadDao
import com.example.gestionmovil.data.local.dao.OrganizacionDao
import com.example.gestionmovil.data.local.dao.SeguimientoDao
// import com.example.gestionmovil.data.local.dao.UsuarioDao // Descomenta o importa según la ubicación de tu UsuarioDao

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
        UsuarioEntity::class // O UsuarioEntity::class según esté definido en tu proyecto
    ],
    version = 4,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    // DAOs provenientes de la rama principal (Usuarios y Seguimiento)
    abstract fun usuarioDao(): UsuarioDao
    abstract fun seguimientoDao(): SeguimientoDao

    // DAOs provenientes de la rama Oportunidades y Contactos
    abstract fun oportunidadDao(): OportunidadDao

    /*abstract fun contactoDao(): ContactoDao*/
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
            override suspend fun onCreate(connection: SQLiteConnection) {
                super.onCreate(connection)
                INSTANCE?.let { database ->
                    poblarCuentasIniciales(database.usuarioDao())
                }
            }
            suspend fun poblarCuentasIniciales(usuarioDao: UsuarioDao) {
                val passwordHasheada = com.example.gestionmovil.utils.Seguridad.hashPassword("Clave123+")

                val cuentasPrueba = listOf(
                    // 1 Administrador
                    UsuarioEntity(
                        pNombre = "Admin",
                        pApellido = "Sistema",
                        email = "admin@empresa.cl",
                        password = passwordHasheada,
                        rol = RolUsuarioEntity.ADMIN.name
                    ),
                    // 1 Supervisor Comercial
                    UsuarioEntity(
                        pNombre = "Carlos",
                        pApellido = "Mendoza",
                        email = "supervisor@empresa.cl",
                        password = passwordHasheada,
                        rol = RolUsuarioEntity.SUPERVISOR.name
                    ),
                    // 3 Ejecutivos Comerciales
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