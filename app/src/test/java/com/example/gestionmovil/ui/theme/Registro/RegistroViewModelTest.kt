package com.example.gestionmovil.ui.theme.Registro

import com.example.gestionmovil.data.local.entities.UsuarioEntity
import com.example.gestionmovil.fakes.FakeUsuarioDao
import com.example.gestionmovil.utils.TestUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.util.Calendar

@OptIn(ExperimentalCoroutinesApi::class)
class RegistroViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var fakeUsuarioDao: FakeUsuarioDao

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        TestUtils.initPatterns()
        fakeUsuarioDao = FakeUsuarioDao()
        TestUtils.injectFakeDatabase(fakeUsuarioDao)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testFormStateChanges() {
        val app = TestUtils.createMockApplication()
        val viewModel = RegistroViewModel(app)

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
        val viewModel = RegistroViewModel(app)

        var successCalled = false
        viewModel.registrarUsuario { successCalled = true }

        val state = viewModel.uiState.value
        assertFalse(successCalled)
        assertNotNull(state.errorMessage)
    }

    @Test
    fun testRegistrarUsuarioExitoso() {
        val app = TestUtils.createMockApplication()
        val viewModel = RegistroViewModel(app)

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

        assertTrue(successCalled)
        assertEquals(1, fakeUsuarioDao.usuarios.size)
        assertEquals("juan@example.com", fakeUsuarioDao.usuarios[0].email)
    }

    @Test
    fun testRegistrarUsuarioFailure_CorreoExistente() {
        fakeUsuarioDao.usuarios.add(
            UsuarioEntity(
                id = "10", pNombre = "Pedro", email = "existente@example.com", password = "hash", rol = "CLIENTE"
            )
        )

        val app = TestUtils.createMockApplication()
        val viewModel = RegistroViewModel(app)

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

        assertFalse(successCalled)
        assertEquals("El correo ya se encuentra registrado", viewModel.uiState.value.errorMessage)
    }
}
