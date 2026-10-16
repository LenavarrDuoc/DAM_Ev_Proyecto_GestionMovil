package com.example.gestionmovil.data.local

import android.content.Context
import androidx.room3.Database
import androidx.room3.Room
import androidx.room3.RoomDatabase
import com.example.gestionmovil.data.local.dao.OportunidadDao
import com.example.gestionmovil.data.local.dao.OrganizacionDao
import com.example.gestionmovil.data.local.entities.ContactoEntity
import com.example.gestionmovil.data.local.entities.OportunidadEntity
import com.example.gestionmovil.data.local.entities.OrganizacionEntity

@Database(
    entities = [
        OportunidadEntity::class,
        OrganizacionEntity::class,
        ContactoEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

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
                    "db-gestionmovil"
                ).fallbackToDestructiveMigration()
                 .build()
                INSTANCE = instance
                instance
            }
        }
    }
}