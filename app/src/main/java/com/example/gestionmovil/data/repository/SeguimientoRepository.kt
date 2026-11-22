package com.example.gestionmovil.data.repository

import com.example.gestionmovil.data.local.dao.SeguimientoDao
import com.example.gestionmovil.data.local.entities.SeguimientoEntity
import com.example.gestionmovil.data.remote.api.SeguimientoApiService
import com.example.gestionmovil.data.remote.client.RetrofitClient
import com.example.gestionmovil.data.remote.mapper.toEntity
import com.example.gestionmovil.data.remote.mapper.toEntityList
import com.example.gestionmovil.data.remote.mapper.toRequestDto
import com.example.gestionmovil.data.remote.mapper.toUpdateDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class SeguimientoRepository(
    private val seguimientoDao: SeguimientoDao,
    private val seguimientoApi: SeguimientoApiService = RetrofitClient.seguimientoApi
) {

    // =======================================================
    // 1. LECTURA (Siempre desde Room para UI Reactiva)
    // =======================================================

    fun obtenerSeguimientosPorOportunidad(oportunidadId: Long): Flow<List<SeguimientoEntity>> {
        return seguimientoDao.obtenerSeguimientosPorOportunidad(oportunidadId)
    }

    fun obtenerProximasAccionesPendientes(): Flow<List<SeguimientoEntity>> {
        return seguimientoDao.obtenerProximasAccionesPendientes()
    }

    suspend fun obtenerSeguimientoPorId(id: Long): SeguimientoEntity? {
        return seguimientoDao.obtenerSeguimientoPorId(id)
    }

    // =======================================================
    // 2. SINCRONIZACIÓN (Remote -> Local)
    // =======================================================

    /**
     * Descarga todos los seguimientos desde Spring Boot (GET /api/seguimientos)
     * y los persiste en Room para refrescar los Flows reactivos.
     */
    suspend fun sincronizarSeguimientos(): Result<List<SeguimientoEntity>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = seguimientoApi.findAll()
                if (response.isSuccessful && response.body() != null) {
                    val entidades = response.body()!!.toEntityList()
                    entidades.forEach { seguimientoDao.insertarSeguimiento(it) }
                    Result.success(entidades)
                } else {
                    Result.failure(Exception("Error al sincronizar seguimientos (${response.code()})"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    // =======================================================
    // 3. ESCRITURA (Remote -> Local con respaldo offline)
    // =======================================================

    /**
     * Registra un seguimiento en Spring Boot (POST /api/seguimientos) y lo guarda en Room
     * con el ID oficial de MySQL. Mantiene el retorno Long para compatibilidad con ViewModels.
     */
    suspend fun guardarSeguimiento(seguimiento: SeguimientoEntity): Long {
        return withContext(Dispatchers.IO) {
            try {
                val response = seguimientoApi.create(seguimiento.toRequestDto())
                if (response.isSuccessful && response.body() != null) {
                    val entidadOficial = response.body()!!.toEntity()
                    seguimientoDao.insertarSeguimiento(entidadOficial)
                    entidadOficial.id
                } else {
                    // Si el servidor rechaza la petición, guardamos localmente como respaldo
                    seguimientoDao.insertarSeguimiento(seguimiento)
                }
            } catch (e: Exception) {
                // Sin conexión: persistencia local offline
                seguimientoDao.insertarSeguimiento(seguimiento)
            }
        }
    }

    /**
     * Actualiza un seguimiento existente en Spring Boot (PUT /api/seguimientos/{id}) y en Room.
     */
    suspend fun actualizarSeguimiento(seguimiento: SeguimientoEntity) {
        withContext(Dispatchers.IO) {
            try {
                val response = seguimientoApi.update(seguimiento.id, seguimiento.toUpdateDto())
                if (response.isSuccessful && response.body() != null) {
                    seguimientoDao.actualizarSeguimiento(response.body()!!.toEntity())
                } else {
                    seguimientoDao.actualizarSeguimiento(seguimiento)
                }
            } catch (e: Exception) {
                seguimientoDao.actualizarSeguimiento(seguimiento)
            }
        }
    }

    /**
     * Elimina un seguimiento en Spring Boot (DELETE /api/seguimientos/{id}) y en Room.
     */
    suspend fun eliminarSeguimiento(seguimiento: SeguimientoEntity) {
        withContext(Dispatchers.IO) {
            try {
                seguimientoApi.delete(seguimiento.id)
            } catch (_: Exception) {
                // Si falla la red, igual permitimos eliminar de la vista local
            } finally {
                seguimientoDao.eliminarSeguimiento(seguimiento)
            }
        }
    }
}