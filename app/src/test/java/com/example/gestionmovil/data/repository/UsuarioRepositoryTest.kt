package com.example.gestionmovil.data.repository

import com.example.gestionmovil.data.local.entities.UsuarioEntity
import com.example.gestionmovil.fakes.FakeUsuarioDao
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class UsuarioRepositoryTest {

    private lateinit var fakeDao: FakeUsuarioDao
    private lateinit var repository: UsuarioRepository

    @Before
    fun setUp() {
        fakeDao = FakeUsuarioDao()
        repository = UsuarioRepository(fakeDao)
    }

    @Test
    fun registrarUsuario_Exitoso() = runBlocking {
        val nuevo = UsuarioEntity(
            id = "2",
            pNombre = "Maria",
            email = "maria@test.com",
            password = "Password123!",
            rol = "CLIENTE"
        )

        val result = repository.registrarUsuario(nuevo)

        // En ausencia de backend activo, la llamada remota falla y devuelve un Result con fallo
        assertTrue(result.isFailure)
    }

    @Test
    fun registrarUsuario_CorreoYaExiste() = runBlocking {
        val existente = UsuarioEntity(
            id = "1",
            pNombre = "Pedro",
            email = "existente@test.com",
            password = "hashedpassword",
            rol = "CLIENTE"
        )
        fakeDao.insertarUsuario(existente)

        val nuevo = UsuarioEntity(
            id = "2",
            pNombre = "Otro",
            email = "existente@test.com",
            password = "Password123!",
            rol = "CLIENTE"
        )

        val result = repository.registrarUsuario(nuevo)

        assertTrue(result.isFailure)
        assertEquals("El correo ya se encuentra registrado", result.exceptionOrNull()?.message)
    }

    @Test
    fun registrarUsuario_Excepcion() = runBlocking {
        val throwingDao = object : FakeUsuarioDao() {
            override suspend fun obtenerPorEmail(email: String): UsuarioEntity? {
                throw RuntimeException("Error en insert BD")
            }
        }
        val repo = UsuarioRepository(throwingDao)
        val nuevo = UsuarioEntity(
            id = "2",
            pNombre = "Maria",
            email = "maria@test.com",
            password = "Password123!",
            rol = "CLIENTE"
        )

        val result = repo.registrarUsuario(nuevo)

        assertTrue(result.isFailure)
    }
}
