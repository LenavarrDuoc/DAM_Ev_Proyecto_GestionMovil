package com.example.gestionmovil.data.repository

import com.example.gestionmovil.data.local.dao.SeguimientoDao
import com.example.gestionmovil.data.local.entities.SeguimientoEntity
import kotlinx.coroutines.flow.Flow

class SeguimientoRepository(
    private val seguimientoDao: SeguimientoDao
) {
    fun obtenerSeguimientosPorOportunidad(oportunidadId: Long): Flow<List<SeguimientoEntity>> {
        return seguimientoDao.obtenerSeguimientosPorOportunidad(oportunidadId)
    }

    fun obtenerProximasAccionesPendientes(): Flow<List<SeguimientoEntity>> {
        return seguimientoDao.obtenerProximasAccionesPendientes()
    }

    suspend fun obtenerSeguimientoPorId(id: Long): SeguimientoEntity? {
        return seguimientoDao.obtenerSeguimientoPorId(id)
    }

    suspend fun guardarSeguimiento(seguimiento: SeguimientoEntity): Long {
        return seguimientoDao.insertarSeguimiento(seguimiento)
    }

    suspend fun actualizarSeguimiento(seguimiento: SeguimientoEntity) {
        seguimientoDao.actualizarSeguimiento(seguimiento)
    }

    suspend fun eliminarSeguimiento(seguimiento: SeguimientoEntity) {
        seguimientoDao.eliminarSeguimiento(seguimiento)
    }
}