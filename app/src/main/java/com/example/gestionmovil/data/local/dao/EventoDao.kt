package com.example.gestionmovil.data.local.dao

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.Query
import com.example.gestionmovil.data.local.entities.EventoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EventoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarEvento(evento: EventoEntity)

    @Query("SELECT * FROM eventos ORDER BY fecha DESC")
    fun obtenerTodosLosEventos(): Flow<List<EventoEntity>>
}