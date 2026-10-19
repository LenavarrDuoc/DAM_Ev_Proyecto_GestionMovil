package com.example.gestionmovil.data.local.dao

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy.Companion.REPLACE
import androidx.room3.Query
import androidx.room3.Transaction
import androidx.room3.Update
import com.example.gestionmovil.data.local.entities.OportunidadConOrganizacion
import com.example.gestionmovil.data.local.entities.OportunidadEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface OportunidadDao {
    @Transaction
    @Query("SELECT * FROM oportunidades")
    fun getOportunidadesConOrganizacion(): Flow<List<OportunidadConOrganizacion>>
    @Insert(onConflict = REPLACE)
    suspend fun insertOportunidad(oportunidad: OportunidadEntity): Long

    @Update
    suspend fun updateOportunidad(oportunidad: OportunidadEntity)

    @Query("SELECT * FROM oportunidades ORDER BY id DESC")
    fun getAllOportunidades(): Flow<List<OportunidadEntity>>

    @Query("SELECT * FROM oportunidades WHERE id = :opId")
    suspend fun getOportunidadById(opId: Long): OportunidadEntity?

    @Query("UPDATE oportunidades SET estado = :nuevoEstado, fechaUltimaActualizacion = :fechaActualizacion WHERE id = :estId")
    suspend fun updateEstado(estId: Long, nuevoEstado: String, fechaActualizacion: String)

}