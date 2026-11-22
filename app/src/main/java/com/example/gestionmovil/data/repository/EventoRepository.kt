package com.example.gestionmovil.data.repository

import com.example.gestionmovil.data.local.dao.EventoDao
import com.example.gestionmovil.data.local.entities.EventoEntity
import com.example.gestionmovil.data.remote.api.EventoApiService
import com.example.gestionmovil.data.remote.client.RetrofitClient
import com.example.gestionmovil.data.remote.mapper.toEntity
import com.example.gestionmovil.data.remote.mapper.toEntityList
import com.example.gestionmovil.data.remote.mapper.toRequestDto
import com.example.gestionmovil.data.remote.mapper.toUpdateDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class EventoRepository(
    private val eventoDao: EventoDao,
    private val eventoApi: EventoApiService = RetrofitClient.eventoApi
) {

    // Fuente única de verdad reactiva para la UI (Jetpack Compose)
    val todosLosEventos: Flow<List<EventoEntity>> = eventoDao.obtenerTodosLosEventos()

    /**
     * Descarga todos los eventos desde Spring Boot y actualiza la caché local en Room.
     */
    suspend fun sincronizarEventos(): Result<List<EventoEntity>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = eventoApi.findAll()
                if (response.isSuccessful && response.body() != null) {
                    val eventosRemotos = response.body()!!.toEntityList()
                    eventosRemotos.forEach { eventoDao.insertarEvento(it) }
                    Result.success(eventosRemotos)
                } else {
                    Result.failure(Exception("Error al sincronizar eventos (${response.code()})"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    /**
     * Guarda un nuevo evento en el Backend (POST /api/eventos) y luego lo persiste en Room
     * con el ID oficial retornado por el servidor. Si no hay red, guarda localmente como respaldo.
     */
    suspend fun guardarEvento(evento: EventoEntity): Result<EventoEntity> {
        return withContext(Dispatchers.IO) {
            try {
                val response = eventoApi.create(evento.toRequestDto())
                if (response.isSuccessful && response.body() != null) {
                    val eventoOficial = response.body()!!.toEntity()
                    eventoDao.insertarEvento(eventoOficial)
                    Result.success(eventoOficial)
                } else {
                    Result.failure(Exception("Error del servidor al crear evento (${response.code()})"))
                }
            } catch (e: Exception) {
                // Respaldo offline: si no hay internet, persiste en Room con su UUID local
                eventoDao.insertarEvento(evento)
                Result.success(evento)
            }
        }
    }

    /**
     * Actualiza un evento existente en el Backend (PUT /api/eventos/{id}) y en Room.
     */
    suspend fun actualizarEvento(evento: EventoEntity): Result<EventoEntity> {
        return withContext(Dispatchers.IO) {
            try {
                val response = eventoApi.update(evento.id, evento.toUpdateDto())
                if (response.isSuccessful && response.body() != null) {
                    val actualizado = response.body()!!.toEntity()
                    eventoDao.insertarEvento(actualizado)
                    Result.success(actualizado)
                } else {
                    Result.failure(Exception("Error al actualizar evento (${response.code()})"))
                }
            } catch (e: Exception) {
                // Actualización local en caso de operar sin conexión
                eventoDao.insertarEvento(evento)
                Result.success(evento)
            }
        }
    }

    /**
     * Elimina un evento por ID en el Backend (DELETE /api/eventos/{id}).
     * (Nota: Si tu EventoDao tiene método eliminar/delete, puedes invocarlo aquí tras el éxito).
     */
    suspend fun eliminarEventoRemoto(id: String): Result<Boolean> {
        return withContext(Dispatchers.IO) {
            try {
                val response = eventoApi.delete(id)
                if (response.isSuccessful) {
                    Result.success(true)
                } else {
                    Result.failure(Exception("No se pudo eliminar el evento (${response.code()})"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}