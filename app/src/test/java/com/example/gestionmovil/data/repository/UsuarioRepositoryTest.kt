package com.example.gestionmovil.data.repository

import com.example.gestionmovil.data.local.entities.UsuarioEntity
import com.example.gestionmovil.fakes.FakeUsuarioDao
import com.example.gestionmovil.utils.Seguridad
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
    fun loginUsuario_Exitoso() = runBlocking {
        val email = "user@test.com"
        val rawPassword = "Password123!"
        val hashed = Seguridad.hashPassword(rawPassword)

        val user = UsuarioEntity(
            id = "1",
            pNombre = "Pedro",
            email = email,
            password = hashed,
            rol = "CLIENTE"
        )
        fakeDao.insertarUsuario(user)

        val result = repository.loginUsuario(email, rawPassword)

        assertTrue(result.isSuccess)
        assertEquals("Pedro", result.getOrNull()?.pNombre)
    }

    @Test
    fun loginUsuario_CorreoNoRegistrado() = runBlocking {
        val result = repository.loginUsuario("noexiste@test.com", "Password123!")

        assertTrue(result.isFailure)
        assertEquals("El correo no está registrado", result.exceptionOrNull()?.message)
    }

    @Test
    fun loginUsuario_PasswordIncorrecta() = runBlocking {
        val email = "user@test.com"
        val hashed = Seguridad.hashPassword("Password123!")
        val user = UsuarioEntity(
            id = "1",
            pNombre = "Pedro",
            email = email,
            password = hashed,
            rol = "CLIENTE"
        )
        fakeDao.insertarUsuario(user)

        val result = repository.loginUsuario(email, "ClaveErronea123!")

        assertTrue(result.isFailure)
        assertEquals("Contraseña incorrecta", result.exceptionOrNull()?.message)
    }

    @Test
    fun loginUsuario_Excepcion() = runBlocking {
        val throwingDao = object : FakeUsuarioDao() {
            override suspend fun obtenerPorEmail(email: String): UsuarioEntity? {
                throw RuntimeException("Error BD")
            }
        }
        val repo = UsuarioRepository(throwingDao)
        val result = repo.loginUsuario("error@test.com", "Password123!")

        assertTrue(result.isFailure)
        assertEquals("Error BD", result.exceptionOrNull()?.message)
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

        assertTrue(result.isSuccess)
        val guardado = fakeDao.obtenerPorEmail("maria@test.com")
        assertNotNull(guardado)
        assertEquals("CLIENTE", guardado?.rol)
        assertNotEquals("Password123!", guardado?.password) // Debe estar hasheado
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
        assertEquals("Error en insert BD", result.exceptionOrNull()?.message)
    }
}
