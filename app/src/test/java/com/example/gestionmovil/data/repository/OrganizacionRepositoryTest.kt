package com.example.gestionmovil.data.repository

import com.example.gestionmovil.data.local.entities.ContactoEntity
import com.example.gestionmovil.data.local.entities.OrganizacionEntity
import com.example.gestionmovil.fakes.FakeOrganizacionDao
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class OrganizacionRepositoryTest {

    private lateinit var fakeDao: FakeOrganizacionDao
    private lateinit var repository: OrganizacionRepository

    @Before
    fun setUp() {
        fakeDao = FakeOrganizacionDao()
        repository = OrganizacionRepository(fakeDao)
    }

    @Test
    fun insertarYObtenerOrganizacionPorId() = runBlocking {
        val org = OrganizacionEntity(
            nombre = "Empresa ABC",
            tipoOrganizacion = "Privada",
            rubro = "Construcción",
            comuna = "Providencia",
            region = "Metropolitana"
        )

        val id = repository.insertOrganizacion(org)

        val retrieved = repository.getOrganizacionById(id)
        assertNotNull(retrieved)
        assertEquals("Empresa ABC", retrieved?.nombre)
    }

    @Test
    fun actualizarOrganizacion() = runBlocking {
        val org = OrganizacionEntity(
            id = 1L,
            nombre = "Empresa Vieja",
            tipoOrganizacion = "Pública",
            rubro = "Educación",
            comuna = "Santiago",
            region = "Metropolitana"
        )
        fakeDao.insertOrganizacion(org)

        val mod = org.copy(nombre = "Empresa Nueva")
        repository.updateOrganizacion(mod)

        val res = repository.getOrganizacionById(1L)
        assertEquals("Empresa Nueva", res?.nombre)
    }

    @Test
    fun obtenerTodasLasOrganizaciones() = runBlocking {
        val org1 = OrganizacionEntity(id = 1L, nombre = "Zeta Corp", tipoOrganizacion = "T", rubro = "R", comuna = "C", region = "R")
        val org2 = OrganizacionEntity(id = 2L, nombre = "Alpha Corp", tipoOrganizacion = "T", rubro = "R", comuna = "C", region = "R")

        fakeDao.insertOrganizacion(org1)
        fakeDao.insertOrganizacion(org2)

        val list = repository.getAllOrganizaciones().first()
        assertEquals(2, list.size)
        assertEquals("Alpha Corp", list[0].nombre)
    }

    @Test
    fun insertarYActualizarContacto() = runBlocking {
        val contacto = ContactoEntity(
            id = 5L,
            organizacionId = 1L,
            nombre = "Juan Pérez",
            cargo = "Gerente",
            area = "Ventas",
            correo = "juan@empresa.com",
            telefono = "+56912345678",
            medioPreferente = "Email",
            estado = "Activo",
            fechaUltimoContacto = "2025-01-01"
        )

        repository.insertContacto(contacto)

        var list = repository.getContactsByOrganizacion(1L).first()
        assertEquals(1, list.size)
        assertEquals("Juan Pérez", list[0].nombre)

        val mod = contacto.copy(cargo = "Director")
        repository.updateContacto(mod)

        list = repository.getContactsByOrganizacion(1L).first()
        assertEquals("Director", list[0].cargo)
    }
}
