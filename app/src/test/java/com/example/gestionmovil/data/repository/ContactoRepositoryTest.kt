package com.example.gestionmovil.data.repository

import com.example.gestionmovil.data.local.entities.ContactoEntity
import com.example.gestionmovil.fakes.FakeContactoDao
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class ContactoRepositoryTest {

    private lateinit var fakeDao: FakeContactoDao
    private lateinit var repository: ContactoRepository

    @Before
    fun setUp() {
        fakeDao = FakeContactoDao()
        repository = ContactoRepository(fakeDao)
    }

    @Test
    fun insertarYObtenerContactoPorId() = runBlocking {
        val contacto = ContactoEntity(
            organizacionId = 1L,
            nombre = "Ana Soto",
            cargo = "Analista",
            area = "TI",
            correo = "ana@empresa.com",
            telefono = "+56987654321",
            medioPreferente = "Teléfono",
            estado = "Activo",
            fechaUltimoContacto = "2025-01-01"
        )

        val id = repository.insertarContacto(contacto)

        val retrieved = repository.obtenerContactoPorId(id)
        assertNotNull(retrieved)
        assertEquals("Ana Soto", retrieved?.nombre)
    }

    @Test
    fun actualizarYEliminarContacto() = runBlocking {
        val contacto = ContactoEntity(
            id = 10L,
            organizacionId = 2L,
            nombre = "Carlos M",
            cargo = "Jefe",
            area = "Operaciones",
            correo = "carlos@empresa.com",
            telefono = "+56911112222",
            medioPreferente = "Email",
            estado = "Activo",
            fechaUltimoContacto = "2025-01-01"
        )

        fakeDao.insertarContacto(contacto)

        val modificado = contacto.copy(nombre = "Carlos Modificado")
        repository.actualizarContacto(modificado)

        var res = repository.obtenerContactoPorId(10L)
        assertEquals("Carlos Modificado", res?.nombre)

        repository.eliminarContacto(modificado)

        res = repository.obtenerContactoPorId(10L)
        assertNull(res)
    }

    @Test
    fun obtenerContactosPorOrganizacion() = runBlocking {
        val c1 = ContactoEntity(id = 1L, organizacionId = 1L, nombre = "B", cargo = "C", area = "A", correo = "e1", telefono = "t1", medioPreferente = "m", estado = "e", fechaUltimoContacto = "f")
        val c2 = ContactoEntity(id = 2L, organizacionId = 1L, nombre = "A", cargo = "C", area = "A", correo = "e2", telefono = "t2", medioPreferente = "m", estado = "e", fechaUltimoContacto = "f")
        val c3 = ContactoEntity(id = 3L, organizacionId = 2L, nombre = "C", cargo = "C", area = "A", correo = "e3", telefono = "t3", medioPreferente = "m", estado = "e", fechaUltimoContacto = "f")

        fakeDao.insertarContactos(listOf(c1, c2, c3))

        val listOrg1 = repository.obtenerContactosPorOrganizacion(1L).first()
        assertEquals(2, listOrg1.size)
        assertEquals("A", listOrg1[0].nombre) // Ordenado ASC
        assertEquals("B", listOrg1[1].nombre)

        val todos = repository.todosLosContactos.first()
        assertEquals(3, todos.size)
    }
}
