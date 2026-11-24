package com.example.gestionmovil.data.repository

import com.example.gestionmovil.data.local.dao.OportunidadDao
import com.example.gestionmovil.data.local.entities.OportunidadConOrganizacion
import com.example.gestionmovil.data.local.entities.OportunidadEntity
import com.example.gestionmovil.data.remote.api.OportunidadApiService
import com.example.gestionmovil.data.remote.client.RetrofitClient
import com.example.gestionmovil.data.remote.mapper.toEntity
import com.example.gestionmovil.data.remote.mapper.toEntityList
import com.example.gestionmovil.data.remote.mapper.toRequestDto
import com.example.gestionmovil.data.remote.mapper.toUpdateDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class OportunidadRepository(
    private val oportunidadDao: OportunidadDao,
    private val oportunidadApi: OportunidadApiService = RetrofitClient.oportunidadApi
) {

    // =======================================================
    // 1. LECTURA (Siempre desde Room para UI Reactiva)
    // =======================================================

    fun getAllOportunidades(): Flow<List<OportunidadEntity>> {
        return oportunidadDao.getAllOportunidades()
    }

    fun getOportunidadesConOrganizacion(): Flow<List<OportunidadConOrganizacion>> {
        return oportunidadDao.getOportunidadesConOrganizacion()
    }

    suspend fun getOportunidadById(id: Long): OportunidadEntity? {
        return oportunidadDao.getOportunidadById(id)
    }

    fun getCantidadOportunidades(): Flow<Int> {
        return oportunidadDao.getCantidadOportunidades()
    }

    // =======================================================
    // 2. SINCRONIZACIÓN (Remote -> Local)
    // =======================================================

    /**
     * Descarga las oportunidades desde Spring Boot y actualiza Room.
     * La UI se actualizará automáticamente porque observa el Flow de Room.
     */
    suspend fun sincronizarOportunidades(): Result<List<OportunidadEntity>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = oportunidadApi.findAll()
                if (response.isSuccessful && response.body() != null) {
                    val entidades = response.body()!!.toEntityList()
                    entidades.forEach { oportunidadDao.insertOportunidad(it) }
                    Result.success(entidades)
                } else {
                    Result.failure(Exception("Error al sincronizar oportunidades (${response.code()})"))
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
     * Crea la oportunidad en Spring Boot (POST /api/oportunidades).
     * Si tiene éxito, guarda en Room usando el ID oficial generado por MySQL.
     */
    suspend fun crearOportunidad(oportunidadLocal: OportunidadEntity): Result<OportunidadEntity> {
        return withContext(Dispatchers.IO) {
            try {
                val requestDto = oportunidadLocal.toRequestDto()
                val response = oportunidadApi.create(requestDto)

                if (response.isSuccessful && response.body() != null) {
                    val entidadConfirmada = response.body()!!.toEntity()
                    oportunidadDao.insertOportunidad(entidadConfirmada)
                    Result.success(entidadConfirmada)
                } else {
                    Result.failure(Exception("Error del servidor (${response.code()})"))
                }
            } catch (e: Exception) {
                // Respaldo offline: si no hay conexión, guarda localmente en Room
                val id = oportunidadDao.insertOportunidad(oportunidadLocal)
                Result.success(if (oportunidadLocal.id == 0L) oportunidadLocal.copy(id = id) else oportunidadLocal)
            }
        }
    }

    /**
     * Actualiza una oportunidad completa en Spring Boot (PUT /api/oportunidades/{id}) y en Room.
     */
    suspend fun updateOportunidad(oportunidad: OportunidadEntity): Result<OportunidadEntity> {
        return withContext(Dispatchers.IO) {
            try {
                val response = oportunidadApi.update(oportunidad.id, oportunidad.toUpdateDto())
                if (response.isSuccessful && response.body() != null) {
                    val actualizada = response.body()!!.toEntity()
                    oportunidadDao.updateOportunidad(actualizada)
                    Result.success(actualizada)
                } else {
                    Result.failure(Exception("Error al actualizar oportunidad (${response.code()})"))
                }
            } catch (e: Exception) {
                // Actualización local en caso de operar sin red
                oportunidadDao.updateOportunidad(oportunidad)
                Result.success(oportunidad)
            }
        }
    }

    /**
     * Actualiza el estado y fecha de última actualización en el Backend (PUT) y en Room.
     */
    suspend fun updateEstadoOportunidad(id: Long, estado: String, fecha: String): Result<Boolean> {
        return withContext(Dispatchers.IO) {
            try {
                val local = oportunidadDao.getOportunidadById(id)
                if (local != null) {
                    val modificada = local.copy(estado = estado, fechaUltimaActualizacion = fecha)
                    val response = oportunidadApi.update(id, modificada.toUpdateDto())
                    if (response.isSuccessful && response.body() != null) {
                        oportunidadDao.updateOportunidad(response.body()!!.toEntity())
                        return@withContext Result.success(true)
                    }
                }
                oportunidadDao.updateEstado(id, estado, fecha)
                Result.success(true)
            } catch (e: Exception) {
                oportunidadDao.updateEstado(id, estado, fecha)
                Result.success(true)
            }
        }
    }

    /**
     * Elimina una oportunidad por ID en Spring Boot (DELETE /api/oportunidades/{id}) y en Room.
     */
    suspend fun deleteOportunidad(id: Long): Result<Boolean> {
        return withContext(Dispatchers.IO) {
            try {
                val response = oportunidadApi.delete(id)
                if (response.isSuccessful) {
                    oportunidadDao.deleteOportunidadById(id)
                    Result.success(true)
                } else {
                    Result.failure(Exception("No se pudo eliminar la oportunidad (${response.code()})"))
                }
            } catch (e: Exception) {
                // Respaldo offline: elimina en Room aunque no haya red
                oportunidadDao.deleteOportunidadById(id)
                Result.success(true)
            }
        }
    }
}