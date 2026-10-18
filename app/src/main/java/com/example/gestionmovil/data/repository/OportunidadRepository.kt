package com.example.gestionmovil.data.repository

import com.example.gestionmovil.data.local.dao.OportunidadDao
import com.example.gestionmovil.data.local.entities.OportunidadEntity
import kotlinx.coroutines.flow.Flow

class OportunidadRepository(
    private val oportunidadDao: OportunidadDao
) {
    /**
     * Obtiene la lista completa de oportunidades ordenadas descendentemente.
     * Retorna un Flow para actualizar la UI automáticamente ante cambios.
     */
    fun getAllOportunidades(): Flow<List<OportunidadEntity>> {
        return oportunidadDao.getAllOportunidades()
    }

    /**
     * Obtiene los datos de una oportunidad específica por su ID.
     */
    suspend fun getOportunidadById(id: Long): OportunidadEntity? {
        return oportunidadDao.getOportunidadById(id)
    }

    /**
     * Inserta una nueva oportunidad en la base de datos de Room.
     * Retorna el ID generado para la nueva oportunidad.
     */
    suspend fun insertOportunidad(oportunidad: OportunidadEntity): Long {
        return oportunidadDao.insertOportunidad(oportunidad)
    }

    /**
     * Actualiza los datos completos de una oportunidad existente.
     */
    suspend fun updateOportunidad(oportunidad: OportunidadEntity) {
        oportunidadDao.updateOportunidad(oportunidad)
    }

    /**
     * Actualiza únicamente el estado de la gestión y la fecha de última modificación.
     */
    suspend fun updateEstadoOportunidad(id: Long, estado: String, fecha: String) {
        oportunidadDao.updateEstado(id, estado, fecha)
    }
}