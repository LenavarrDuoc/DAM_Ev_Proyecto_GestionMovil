package com.example.gestionmovil.data.repository

import com.example.gestionmovil.data.local.dao.OrganizacionDao
import com.example.gestionmovil.data.local.entities.ContactoEntity
import com.example.gestionmovil.data.local.entities.OrganizacionEntity
import com.example.gestionmovil.data.remote.api.ContactoApiService
import com.example.gestionmovil.data.remote.api.OrganizacionApiService
import com.example.gestionmovil.data.remote.client.RetrofitClient
import com.example.gestionmovil.data.remote.mapper.toEntity
import com.example.gestionmovil.data.remote.mapper.toEntityList
import com.example.gestionmovil.data.remote.mapper.toRequestDto
import com.example.gestionmovil.data.remote.mapper.toUpdateDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class OrganizacionRepository(
    private val organizacionDao: OrganizacionDao,
    private val organizacionApi: OrganizacionApiService = RetrofitClient.organizacionApi,
    private val contactoApi: ContactoApiService = RetrofitClient.contactoApi
) {
    // ==========================================
    // OPERACIONES DE ORGANIZACIONES
    // ==========================================

    /**
     * Obtiene el listado reactivo desde Room (Única fuente de verdad para la UI).
     */
    fun getAllOrganizaciones(): Flow<List<OrganizacionEntity>> {
        return organizacionDao.getAllOrganizaciones()
    }

    /**
     * Descarga todas las organizaciones desde Spring Boot y actualiza Room.
     */
    suspend fun sincronizarOrganizaciones(): Result<List<OrganizacionEntity>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = organizacionApi.findAll()
                if (response.isSuccessful && response.body() != null) {
                    val entidades = response.body()!!.toEntityList()
                    entidades.forEach { organizacionDao.insertOrganizacion(it) }
                    Result.success(entidades)
                } else {
                    Result.failure(Exception("Error al sincronizar organizaciones (${response.code()})"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    /**
     * Busca una organización por su identificador (intenta refrescar desde API, fallback a Room).
     */
    suspend fun getOrganizacionById(id: Long): OrganizacionEntity? {
        return withContext(Dispatchers.IO) {
            try {
                val response = organizacionApi.findById(id)
                if (response.isSuccessful && response.body() != null) {
                    val entidad = response.body()!!.toEntity()
                    organizacionDao.insertOrganizacion(entidad)
                    return@withContext entidad
                }
            } catch (_: Exception) {
                // Sin red: continuamos hacia la consulta local
            }
            organizacionDao.getOrganizacionById(id)
        }
    }

    /**
     * Registra una nueva organización en el Backend y luego en Room con el ID oficial.
     * Si no hay conexión, guarda localmente como respaldo.
     */
    suspend fun insertOrganizacion(organizacion: OrganizacionEntity): Long {
        return withContext(Dispatchers.IO) {
            try {
                val response = organizacionApi.create(organizacion.toRequestDto())
                if (response.isSuccessful && response.body() != null) {
                    val entidadOficial = response.body()!!.toEntity()
                    organizacionDao.insertOrganizacion(entidadOficial)
                    return@withContext entidadOficial.id
                }
            } catch (_: Exception) {
                // Fallback offline si falla la red
            }
            organizacionDao.insertOrganizacion(organizacion)
        }
    }

    /**
     * Modifica los antecedentes de una organización en Backend y Room.
     */
    suspend fun updateOrganizacion(organizacion: OrganizacionEntity) {
        withContext(Dispatchers.IO) {
            try {
                val response = organizacionApi.update(organizacion.id, organizacion.toUpdateDto())
                if (response.isSuccessful && response.body() != null) {
                    organizacionDao.updateOrganizacion(response.body()!!.toEntity())
                    return@withContext
                }
            } catch (_: Exception) {
                // Fallback offline
            }
            organizacionDao.updateOrganizacion(organizacion)
        }
    }

    /**
     * Elimina una organización en el Backend.
     */
    suspend fun deleteOrganizacion(id: Long): Result<Boolean> {
        return withContext(Dispatchers.IO) {
            try {
                val response = organizacionApi.delete(id)
                if (response.isSuccessful) {
                    organizacionDao.deleteOrganizacionById(id)
                    Result.success(true)
                } else {
                    Result.failure(Exception("No se pudo eliminar la organización (${response.code()})"))
                }
            } catch (e: Exception) {
                organizacionDao.deleteOrganizacionById(id)
                Result.success(true)
            }
        }
    }

    // ==========================================
    // OPERACIONES DE CONTACTOS CORPORATIVOS (Compatibilidad)
    // ==========================================

    fun getContactsByOrganizacion(orgId: Long): Flow<List<ContactoEntity>> {
        return organizacionDao.getContactosByOrganizacion(orgId)
    }

    suspend fun insertContacto(contacto: ContactoEntity) {
        withContext(Dispatchers.IO) {
            try {
                val response = contactoApi.create(contacto.toRequestDto())
                if (response.isSuccessful && response.body() != null) {
                    organizacionDao.insertContacto(response.body()!!.toEntity())
                    return@withContext
                }
            } catch (_: Exception) { }
            organizacionDao.insertContacto(contacto)
        }
    }

    suspend fun updateContacto(contacto: ContactoEntity) {
        withContext(Dispatchers.IO) {
            try {
                val response = contactoApi.update(contacto.id, contacto.toUpdateDto())
                if (response.isSuccessful && response.body() != null) {
                    organizacionDao.updateContacto(response.body()!!.toEntity())
                    return@withContext
                }
            } catch (_: Exception) { }
            organizacionDao.updateContacto(contacto)
        }
    }
}