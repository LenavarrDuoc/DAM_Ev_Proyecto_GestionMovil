package com.example.gestionmovil.data.repository

import com.example.gestionmovil.data.local.entities.OportunidadEntity
import com.example.gestionmovil.data.local.entities.OrganizacionEntity
import com.example.gestionmovil.fakes.FakeOportunidadDao
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class OportunidadRepositoryTest {

    private lateinit var fakeDao: FakeOportunidadDao
    private lateinit var repository: OportunidadRepository

    @Before
    fun setUp() {
        fakeDao = FakeOportunidadDao()
        repository = OportunidadRepository(fakeDao)
    }

    @Test
    fun insertAndGetOportunidadById() = runBlocking {
        val op = OportunidadEntity(
            nombre = "Nueva Oportunidad",
            tipoOportunidad = "Servicio",
            descripcion = "Descripción",
            fuenteInformacion = "Web",
            fechaDeteccion = "2025-01-01",
            responsableInterno = "Juan",
            prioridad = "Alta",
            potencialEstimado = "$1000",
            estado = "Nueva",
            fechaUltimaActualizacion = "2025-01-01",
            organizacionId = 1L
        )

        val id = repository.insertOportunidad(op)

        val retrieved = repository.getOportunidadById(id)
        assertNotNull(retrieved)
        assertEquals("Nueva Oportunidad", retrieved?.nombre)
    }

    @Test
    fun updateOportunidad() = runBlocking {
        val op = OportunidadEntity(
            id = 1L,
            nombre = "Oportunidad Inicial",
            tipoOportunidad = "Servicio",
            descripcion = "Desc",
            fuenteInformacion = "Web",
            fechaDeteccion = "2025-01-01",
            responsableInterno = "Juan",
            prioridad = "Baja",
            potencialEstimado = "$1000",
            estado = "Nueva",
            fechaUltimaActualizacion = "2025-01-01",
            organizacionId = 1L
        )
        fakeDao.insertOportunidad(op)

        val actualizada = op.copy(nombre = "Oportunidad Modificada", prioridad = "Alta")
        repository.updateOportunidad(actualizada)

        val res = repository.getOportunidadById(1L)
        assertEquals("Oportunidad Modificada", res?.nombre)
        assertEquals("Alta", res?.prioridad)
    }

    @Test
    fun updateEstadoOportunidad() = runBlocking {
        val op = OportunidadEntity(
            id = 2L,
            nombre = "Op Estado",
            tipoOportunidad = "Venta",
            descripcion = "Desc",
            fuenteInformacion = "Web",
            fechaDeteccion = "2025-01-01",
            responsableInterno = "Ana",
            prioridad = "Media",
            potencialEstimado = "$2000",
            estado = "Nueva",
            fechaUltimaActualizacion = "2025-01-01",
            organizacionId = 1L
        )
        fakeDao.insertOportunidad(op)

        repository.updateEstadoOportunidad(2L, "Ganada", "2025-01-05")

        val res = repository.getOportunidadById(2L)
        assertEquals("Ganada", res?.estado)
        assertEquals("2025-01-05", res?.fechaUltimaActualizacion)
    }

    @Test
    fun getAllOportunidades() = runBlocking {
        fakeDao.insertOportunidad(
            OportunidadEntity(
                id = 1L, nombre = "Op 1", tipoOportunidad = "T", descripcion = "D",
                fuenteInformacion = "F", fechaDeteccion = "F", responsableInterno = "R",
                prioridad = "P", potencialEstimado = "P", estado = "E", fechaUltimaActualizacion = "F",
                organizacionId = 1L
            )
        )

        val list = repository.getAllOportunidades().first()
        assertEquals(1, list.size)
        assertEquals("Op 1", list[0].nombre)
    }

    @Test
    fun getCantidadOportunidades() = runBlocking {
        assertEquals(0, repository.getCantidadOportunidades().first())

        fakeDao.insertOportunidad(
            OportunidadEntity(
                id = 1L, nombre = "Op 1", tipoOportunidad = "T", descripcion = "D",
                fuenteInformacion = "F", fechaDeteccion = "F", responsableInterno = "R",
                prioridad = "P", potencialEstimado = "P", estado = "E", fechaUltimaActualizacion = "F",
                organizacionId = 1L
            )
        )

        assertEquals(1, repository.getCantidadOportunidades().first())
    }

    @Test
    fun getOportunidadesConOrganizacion() = runBlocking {
        val org = OrganizacionEntity(
            id = 10L,
            nombre = "Empresa ACME",
            tipoOrganizacion = "Privada",
            rubro = "Tecnología",
            comuna = "Santiago",
            region = "Metropolitana"
        )
        fakeDao.organizaciones.add(org)

        fakeDao.insertOportunidad(
            OportunidadEntity(
                id = 1L, nombre = "Op 1", tipoOportunidad = "T", descripcion = "D",
                fuenteInformacion = "F", fechaDeteccion = "F", responsableInterno = "R",
                prioridad = "P", potencialEstimado = "P", estado = "E", fechaUltimaActualizacion = "F",
                organizacionId = 10L
            )
        )

        val list = repository.getOportunidadesConOrganizacion().first()
        assertEquals(1, list.size)
        assertEquals("Empresa ACME", list[0].organizacion?.nombre)
    }
}
