package com.example.gestionmovil.data.repository

import com.example.gestionmovil.data.local.dao.EventoDao
import com.example.gestionmovil.data.local.entities.EventoEntity
import kotlinx.coroutines.flow.Flow
class EventoRepository(private val eventoDao: EventoDao) {

    val todosLosEventos: Flow<List<EventoEntity>> = eventoDao.obtenerTodosLosEventos()

    suspend fun guardarEvento(evento: EventoEntity) {
        eventoDao.insertarEvento(evento)
    }
}