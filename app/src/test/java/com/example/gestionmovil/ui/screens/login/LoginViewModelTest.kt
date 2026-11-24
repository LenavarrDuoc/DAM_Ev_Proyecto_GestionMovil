package com.example.gestionmovil.ui.screens.login

import com.example.gestionmovil.data.local.entities.UsuarioEntity
import com.example.gestionmovil.data.repository.AuthRepository
import com.example.gestionmovil.fakes.FakeAuthApiService
import com.example.gestionmovil.fakes.FakeUsuarioDao
import com.example.gestionmovil.utils.Seguridad
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
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var fakeUsuarioDao: FakeUsuarioDao
    private lateinit var authRepository: AuthRepository

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        TestUtils.initPatterns()
        fakeUsuarioDao = FakeUsuarioDao()
        TestUtils.injectFakeDatabase(fakeUsuarioDao)
        authRepository = AuthRepository(FakeAuthApiService(), fakeUsuarioDao)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testFormStateChanges() {
        val app = TestUtils.createMockApplication()
        val viewModel = LoginViewModel(app, authRepository)

        viewModel.onEmailChange("test@example.com")
        viewModel.onPasswordChange("Password123!")

        val state = viewModel.uiState.value
        assertEquals("test@example.com", state.email)
        assertEquals("Password123!", state.password)
        assertFalse(state.errorCorreo)
        assertFalse(state.errorPassword)
        assertNull(state.errorMessage)

        viewModel.togglePasswordVisibility()
        assertTrue(viewModel.uiState.value.isPasswordVisible)

        viewModel.limpiarCampos()
        assertEquals("", viewModel.uiState.value.email)
        assertEquals("", viewModel.uiState.value.password)

        viewModel.limpiarEstadoCompleto()
        assertEquals(LoginUiState(), viewModel.uiState.value)
    }

    @Test
    fun testLoginValidationFail() {
        val app = TestUtils.createMockApplication()
        val viewModel = LoginViewModel(app, authRepository)

        var successCalled = false
        viewModel.onLoginClick { successCalled = true }

        val state = viewModel.uiState.value
        assertFalse(successCalled)
        assertNotNull(state.errorMessage)
    }

    @Test
    fun testLoginSuccess() = runBlocking {
        val app = TestUtils.createMockApplication()
        val rawPassword = "Password123!"
        val email = "user@test.com"
        val user = UsuarioEntity(
            id = "1",
            pNombre = "Pedro",
            email = email,
            password = Seguridad.hashPassword(rawPassword),
            rol = "CLIENTE"
        )
        fakeUsuarioDao.usuarios.add(user)

        val viewModel = LoginViewModel(app, authRepository)
        viewModel.onEmailChange(email)
        viewModel.onPasswordChange(rawPassword)

        var loggedUser: UsuarioEntity? = null
        viewModel.onLoginClick { loggedUser = it }
        kotlinx.coroutines.delay(200)

        assertNotNull(loggedUser)
        assertEquals("Pedro", loggedUser?.pNombre)
    }

    @Test
    fun testLoginFailure_CorreoNoRegistrado() = runBlocking {
        val app = TestUtils.createMockApplication()
        val viewModel = LoginViewModel(app, authRepository)

        viewModel.onEmailChange("noexiste@test.com")
        viewModel.onPasswordChange("Password123!")

        var successCalled = false
        viewModel.onLoginClick { successCalled = true }
        kotlinx.coroutines.delay(200)

        assertFalse(successCalled)
        val state = viewModel.uiState.value
        assertTrue(state.errorCorreo)
        assertFalse(state.errorPassword)
        assertEquals("El correo no está registrado", state.errorMessage)
    }

    @Test
    fun testLoginFailure_PasswordIncorrecta() = runBlocking {
        val app = TestUtils.createMockApplication()
        val email = "user@test.com"
        val user = UsuarioEntity(
            id = "1",
            pNombre = "Pedro",
            email = email,
            password = Seguridad.hashPassword("Password123!"),
            rol = "CLIENTE"
        )
        fakeUsuarioDao.usuarios.add(user)

        val viewModel = LoginViewModel(app, authRepository)
        viewModel.onEmailChange(email)
        viewModel.onPasswordChange("OtraClave123!")

        var successCalled = false
        viewModel.onLoginClick { successCalled = true }
        kotlinx.coroutines.delay(200)

        assertFalse(successCalled)
        val state = viewModel.uiState.value
        assertFalse(state.errorCorreo)
        assertTrue(state.errorPassword)
        assertEquals("Contraseña incorrecta", state.errorMessage)
    }
}
