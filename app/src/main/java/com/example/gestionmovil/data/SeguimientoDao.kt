package com.example.gestionmovil.data.local.dao

import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.Query
import androidx.room3.Update
import com.example.gestionmovil.data.local.entities.SeguimientoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SeguimientoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarSeguimiento(seguimiento: SeguimientoEntity): Long

    @Update
    suspend fun actualizarSeguimiento(seguimiento: SeguimientoEntity)

    @Delete
    suspend fun eliminarSeguimiento(seguimiento: SeguimientoEntity)

    // Obtener todo el historial de seguimientos de una oportunidad específica ordenado por fecha
    @Query("SELECT * FROM seguimientos WHERE oportunidadId = :oportunidadId ORDER BY id DESC")
    fun obtenerSeguimientosPorOportunidad(oportunidadId: Long): Flow<List<SeguimientoEntity>>

    // Obtener un seguimiento por su ID
    @Query("SELECT * FROM seguimientos WHERE id = :id LIMIT 1")
    suspend fun obtenerSeguimientoPorId(id: Long): SeguimientoEntity?

    // Obtener gestiones que tengan una próxima acción pendiente (para alertas/listado general)
    @Query("SELECT * FROM seguimientos WHERE proximaAccion != '' ORDER BY fechaProximaAccion ASC")
    fun obtenerProximasAccionesPendientes(): Flow<List<SeguimientoEntity>>
}