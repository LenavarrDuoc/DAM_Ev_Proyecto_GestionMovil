package com.example.gestionmovil.data.repository

import com.example.gestionmovil.data.local.entities.EventoEntity
import com.example.gestionmovil.fakes.FakeEventoApiService
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
        repository = EventoRepository(fakeDao, FakeEventoApiService())
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
            organizacionId = 1L,
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

    @Test
    fun obtenerEventoPorIdYActualizar() = runBlocking {
        val evento = EventoEntity(
            id = "EVT-2",
            titulo = "Conferencia Inicial",
            tipoEvento = "Conferencia",
            fecha = "2025-06-01",
            hora = "11:00",
            region = "Metropolitana",
            comuna = "Providencia",
            organizacionId = 2L,
            organizacionNombre = "Org Test",
            publicoEsperado = 50,
            condicionesParticipacion = "Registro previo",
            rutaFotoEvidencia = null
        )

        repository.guardarEvento(evento)
        val guardado = repository.obtenerEventoPorId("EVT-2")
        assertNotNull(guardado)
        assertEquals("Conferencia Inicial", guardado?.titulo)

        val actualizado = guardado!!.copy(titulo = "Conferencia Actualizada", publicoEsperado = 150)
        repository.actualizarEvento(actualizado)

        val resultado = repository.obtenerEventoPorId("EVT-2")
        assertEquals("Conferencia Actualizada", resultado?.titulo)
        assertEquals(150, resultado?.publicoEsperado)
    }

    @Test
    fun eliminarEventoRemoto() = runBlocking {
        val evento = EventoEntity(
            id = "EVT-3",
            titulo = "Evento a eliminar",
            tipoEvento = "Seminario",
            fecha = "2025-07-01",
            hora = "12:00",
            region = "Valparaíso",
            comuna = "Viña del Mar",
            organizacionId = 3L,
            organizacionNombre = "Org Eliminacion",
            publicoEsperado = 30,
            condicionesParticipacion = "Ninguna",
            rutaFotoEvidencia = null
        )

        repository.guardarEvento(evento)
        assertNotNull(repository.obtenerEventoPorId("EVT-3"))

        val res = repository.eliminarEventoRemoto("EVT-3")
        assertTrue(res.isSuccess)
        assertNull(repository.obtenerEventoPorId("EVT-3"))
    }
}
