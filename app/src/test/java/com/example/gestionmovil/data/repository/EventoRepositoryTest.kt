package com.example.gestionmovil.data.repository

import com.example.gestionmovil.data.local.entities.EventoEntity
import com.example.gestionmovil.fakes.FakeEventoDao
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class EventoRepositoryTest {

    private lateinit var fakeDao: FakeEventoDao
    private lateinit var repository: EventoRepository

    @Before
    fun setUp() {
        fakeDao = FakeEventoDao()
        repository = EventoRepository(fakeDao)
    }

    @Test
    fun guardarYObtenerEventos() = runBlocking {
        val evento = EventoEntity(
            id = "EVT-1",
            titulo = "Seminario Tech",
            tipoEvento = "Seminario",
            fecha = "2025-05-10",
            hora = "10:00",
            region = "Metropolitana",
            comuna = "Santiago",
            organizacionId = "ORG-1",
            organizacionNombre = "Tech Corp",
            publicoEsperado = 100,
            condicionesParticipacion = "Presencial",
            rutaFotoEvidencia = null
        )

        repository.guardarEvento(evento)

        val lista = repository.todosLosEventos.first()
        assertEquals(1, lista.size)
        assertEquals("Seminario Tech", lista[0].titulo)
    }
}
