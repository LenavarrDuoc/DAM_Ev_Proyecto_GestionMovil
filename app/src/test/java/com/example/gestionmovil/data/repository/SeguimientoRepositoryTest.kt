package com.example.gestionmovil.data.repository

import com.example.gestionmovil.data.local.entities.SeguimientoEntity
import com.example.gestionmovil.fakes.FakeSeguimientoApiService
import com.example.gestionmovil.fakes.FakeSeguimientoDao
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class SeguimientoRepositoryTest {

    private lateinit var fakeDao: FakeSeguimientoDao
    private lateinit var repository: SeguimientoRepository

    @Before
    fun setUp() {
        fakeDao = FakeSeguimientoDao()
        repository = SeguimientoRepository(fakeDao, FakeSeguimientoApiService())
    }

    @Test
    fun guardarYObtenerSeguimiento() = runBlocking {
        val seg = SeguimientoEntity(
            oportunidadId = 100L,
            contactoId = 1L,
            tipoGestion = "Llamada",
            propuesta = "Propuesta A",
            respuestaCliente = "Interesado",
            proximaAccion = "Enviar Cotización",
            fechaGestion = "2025-02-01",
            fechaProximaAccion = "2025-02-05",
            observacion = "Llamar mañana"
        )

        val id = repository.guardarSeguimiento(seg)

        val retrieved = repository.obtenerSeguimientoPorId(id)
        assertNotNull(retrieved)
        assertEquals("Llamada", retrieved?.tipoGestion)

        val listOpp = repository.obtenerSeguimientosPorOportunidad(100L).first()
        assertEquals(1, listOpp.size)
    }

    @Test
    fun actualizarYEliminarSeguimiento() = runBlocking {
        val seg = SeguimientoEntity(
            id = 5L,
            oportunidadId = 200L,
            contactoId = 2L,
            tipoGestion = "Reunión",
            propuesta = "Propuesta B",
            respuestaCliente = "En revisión",
            proximaAccion = "Seguimiento email",
            fechaGestion = "2025-02-01",
            fechaProximaAccion = "2025-02-10",
            observacion = ""
        )
        fakeDao.insertarSeguimiento(seg)

        val mod = seg.copy(respuestaCliente = "Aprobado")
        repository.actualizarSeguimiento(mod)

        var res = repository.obtenerSeguimientoPorId(5L)
        assertEquals("Aprobado", res?.respuestaCliente)

        repository.eliminarSeguimiento(mod)
        res = repository.obtenerSeguimientoPorId(5L)
        assertNull(res)
    }

    @Test
    fun obtenerProximasAccionesPendientes() = runBlocking {
        val s1 = SeguimientoEntity(id = 1L, oportunidadId = 1L, contactoId = 1L, tipoGestion = "Llamada", propuesta = "", respuestaCliente = "", proximaAccion = "Llamar", fechaGestion = "", fechaProximaAccion = "2025-03-01", observacion = "")
        val s2 = SeguimientoEntity(id = 2L, oportunidadId = 1L, contactoId = 1L, tipoGestion = "Mail", propuesta = "", respuestaCliente = "", proximaAccion = "", fechaGestion = "", fechaProximaAccion = "", observacion = "")

        fakeDao.insertarSeguimiento(s1)
        fakeDao.insertarSeguimiento(s2)

        val pendientes = repository.obtenerProximasAccionesPendientes().first()
        assertEquals(1, pendientes.size)
        assertEquals("Llamar", pendientes[0].proximaAccion)
    }
}
