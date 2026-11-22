package com.example.gestionmovil.data.repository

import com.example.gestionmovil.data.local.dao.ContactoDao
import com.example.gestionmovil.data.local.entities.ContactoEntity
import com.example.gestionmovil.data.remote.api.ContactoApiService
import com.example.gestionmovil.data.remote.client.RetrofitClient
import com.example.gestionmovil.data.remote.mapper.toEntity
import com.example.gestionmovil.data.remote.mapper.toEntityList
import com.example.gestionmovil.data.remote.mapper.toRequestDto
import com.example.gestionmovil.data.remote.mapper.toUpdateDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class ContactoRepository(
    private val contactoDao: ContactoDao,
    private val contactoApi: ContactoApiService = RetrofitClient.contactoApi
) {

    val todosLosContactos: Flow<List<ContactoEntity>> = contactoDao.obtenerTodosLosContactos()

    /**
     * Descarga todos los contactos desde el Backend y actualiza la caché local en Room.
     */
    suspend fun sincronizarContactos(): Result<List<ContactoEntity>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = contactoApi.findAll()
                if (response.isSuccessful && response.body() != null) {
                    val entidades = response.body()!!.toEntityList()
                    contactoDao.insertarContactos(entidades)
                    Result.success(entidades)
                } else {
                    Result.failure(Exception("Error al sincronizar contactos (${response.code()})"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    /**
     * Crea el contacto en Spring Boot y lo guarda en Room con el ID retornado.
     */
    suspend fun insertarContacto(contacto: ContactoEntity): Long {
        return withContext(Dispatchers.IO) {
            try {
                val response = contactoApi.create(contacto.toRequestDto())
                if (response.isSuccessful && response.body() != null) {
                    val entidadOficial = response.body()!!.toEntity()
                    contactoDao.insertarContacto(entidadOficial)
                    return@withContext entidadOficial.id
                }
            } catch (_: Exception) {
                // Fallback offline
            }
            contactoDao.insertarContacto(contacto)
        }
    }

    /**
     * Actualiza el contacto en Spring Boot y luego en Room.
     */
    suspend fun actualizarContacto(contacto: ContactoEntity) {
        withContext(Dispatchers.IO) {
            try {
                val response = contactoApi.update(contacto.id, contacto.toUpdateDto())
                if (response.isSuccessful && response.body() != null) {
                    contactoDao.actualizarContacto(response.body()!!.toEntity())
                    return@withContext
                }
            } catch (_: Exception) {
                // Fallback offline
            }
            contactoDao.actualizarContacto(contacto)
        }
    }

    /**
     * Elimina el contacto en Spring Boot y en Room.
     */
    suspend fun eliminarContacto(contacto: ContactoEntity) {
        withContext(Dispatchers.IO) {
            try {
                contactoApi.delete(contacto.id)
            } catch (_: Exception) {
                // Si no hay conexión, al menos se elimina localmente
            }
            contactoDao.eliminarContacto(contacto)
        }
    }

    /**
     * Obtiene un contacto por ID (intenta desde API, fallback a Room).
     */
    suspend fun obtenerContactoPorId(id: Long): ContactoEntity? {
        return withContext(Dispatchers.IO) {
            try {
                val response = contactoApi.findById(id)
                if (response.isSuccessful && response.body() != null) {
                    val entidad = response.body()!!.toEntity()
                    contactoDao.insertarContacto(entidad)
                    return@withContext entidad
                }
            } catch (_: Exception) { }
            contactoDao.obtenerContactoPorId(id)
        }
    }

    fun obtenerContactosPorOrganizacion(orgId: Long): Flow<List<ContactoEntity>> {
        return contactoDao.obtenerContactosPorOrganizacion(orgId)
    }
}