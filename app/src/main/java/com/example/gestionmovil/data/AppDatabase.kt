package com.example.gestionmovil.data

import com.example.gestionmovil.models.Usuario
import android.content.Context
import androidx.room.TypeConverters
import androidx.room3.Database
import androidx.room3.Room
import androidx.room3.RoomDatabase

//*** INICIO Usuarios:
@Database(entities = [Usuario::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun usuarioDao(): UsuarioDao

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
                    .fallbackToDestructiveMigration() // Elimina y recrea la base de datos si cambia el esquema durante desarrollo
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
//*** FIN Usuarios:

