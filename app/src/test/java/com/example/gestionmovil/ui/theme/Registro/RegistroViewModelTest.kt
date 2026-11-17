package com.example.gestionmovil.ui.theme.Registro

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

        viewModel.onPrimerNombreChange("Juan")
        viewModel.onSegundoNombreChange("Carlos")
        viewModel.onPrimerApellidoChange("Pérez")
        viewModel.onSegundoApellidoChange("Gómez")
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
}
