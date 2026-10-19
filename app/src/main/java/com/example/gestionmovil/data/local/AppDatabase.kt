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

@Database(
    entities = [
        OrganizacionEntity::class,
        ContactoEntity::class,
        OportunidadEntity::class,
        SeguimientoEntity::class,
        UsuarioEntity::class // O UsuarioEntity::class según esté definido en tu proyecto
    ],
    version = 3,
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
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}