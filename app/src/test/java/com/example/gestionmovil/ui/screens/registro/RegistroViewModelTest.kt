package com.example.gestionmovil.ui.screens.registro

import com.example.gestionmovil.data.local.entities.UsuarioEntity
import com.example.gestionmovil.data.repository.UsuarioRepository
import com.example.gestionmovil.fakes.FakeUsuarioApiService
import com.example.gestionmovil.fakes.FakeUsuarioDao
import com.example.gestionmovil.utils.TestUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import java.util.Calendar

@OptIn(ExperimentalCoroutinesApi::class)
class RegistroViewModelTest {

    companion object {
        @BeforeClass
        @JvmStatic
        fun setUpClass() {
            TestUtils.initPatterns()
        }
    }

    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var fakeUsuarioDao: FakeUsuarioDao
    private lateinit var fakeUsuarioApi: FakeUsuarioApiService
    private lateinit var usuarioRepository: UsuarioRepository

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        fakeUsuarioDao = FakeUsuarioDao()
        fakeUsuarioApi = FakeUsuarioApiService()
        TestUtils.injectFakeDatabase(fakeUsuarioDao)
        usuarioRepository = UsuarioRepository(fakeUsuarioDao, fakeUsuarioApi)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testFormStateChanges() {
        val app = TestUtils.createMockApplication()
        val viewModel = RegistroViewModel(app, usuarioRepository)

        val cal = Calendar.getInstance().apply { add(Calendar.YEAR, -25) }
        viewModel.onPrimerNombreChange("Juan")
        viewModel.onSegundoNombreChange("Carlos")
        viewModel.onPrimerApellidoChange("Pérez")
        viewModel.onSegundoApellidoChange("Gómez")
        viewModel.onFechaNacimientoSelected(cal.time)
        viewModel.onEmailChange("juan@example.com")
        viewModel.onPasswordChange("Password123!")
        viewModel.onConfirmPasswordChange("Password123!")
        viewModel.onAceptaTerminosChange(true)

        val state = viewModel.uiState.value
        assertEquals("Juan", state.primerNombre)
        assertEquals("Carlos", state.segundoNombre)
        assertEquals("Pérez", state.primerApellido)
        assertEquals("Gómez", state.segundoApellido)
        assertEquals("juan@example.com", state.email)
        assertEquals("Password123!", state.password)
        assertEquals("Password123!", state.confirmPassword)
        assertTrue(state.aceptaTerminos)

        viewModel.togglePasswordVisibility()
        assertTrue(viewModel.uiState.value.isPasswordVisible)

        viewModel.toggleConfirmPasswordVisibility()
        assertTrue(viewModel.uiState.value.isConfirmPasswordVisible)
    }

    @Test
    fun testRegistrarUsuarioValidationFail() {
        val app = TestUtils.createMockApplication()
        val viewModel = RegistroViewModel(app, usuarioRepository)

        var successCalled = false
        viewModel.registrarUsuario { successCalled = true }

        val state = viewModel.uiState.value
        assertFalse(successCalled)
        assertNotNull(state.errorMessage)
    }

    @Test
    fun testRegistrarUsuarioExitoso() = runBlocking {
        fakeUsuarioApi.shouldFail = false
        val app = TestUtils.createMockApplication()
        val viewModel = RegistroViewModel(app, usuarioRepository)

        val cal = Calendar.getInstance().apply { add(Calendar.YEAR, -25) }
        viewModel.onPrimerNombreChange("Juan")
        viewModel.onSegundoNombreChange("")
        viewModel.onPrimerApellidoChange("Pérez")
        viewModel.onSegundoApellidoChange("")
        viewModel.onFechaNacimientoSelected(cal.time)
        viewModel.onEmailChange("juan@example.com")
        viewModel.onPasswordChange("Password123!")
        viewModel.onConfirmPasswordChange("Password123!")
        viewModel.onAceptaTerminosChange(true)

        var successCalled = false
        viewModel.registrarUsuario { successCalled = true }
        kotlinx.coroutines.delay(500)
        testDispatcher.scheduler.advanceUntilIdle()

        assertTrue(successCalled)
        assertEquals(1, fakeUsuarioDao.usuarios.size)
        assertEquals("juan@example.com", fakeUsuarioDao.usuarios[0].email)
    }

    @Test
    fun testRegistrarUsuarioFailure_CorreoExistente() = runBlocking {
        fakeUsuarioDao.usuarios.add(
            UsuarioEntity(
                id = "10", pNombre = "Pedro", email = "existente@example.com", password = "hash", rol = "CLIENTE"
            )
        )

        val app = TestUtils.createMockApplication()
        val viewModel = RegistroViewModel(app, usuarioRepository)

        val cal = Calendar.getInstance().apply { add(Calendar.YEAR, -25) }
        viewModel.onPrimerNombreChange("Juan")
        viewModel.onPrimerApellidoChange("Pérez")
        viewModel.onFechaNacimientoSelected(cal.time)
        viewModel.onEmailChange("existente@example.com")
        viewModel.onPasswordChange("Password123!")
        viewModel.onConfirmPasswordChange("Password123!")
        viewModel.onAceptaTerminosChange(true)

        var successCalled = false
        viewModel.registrarUsuario { successCalled = true }
        kotlinx.coroutines.delay(200)

        assertFalse(successCalled)
        assertEquals("El correo ya se encuentra registrado", viewModel.uiState.value.errorMessage)
    }
}
