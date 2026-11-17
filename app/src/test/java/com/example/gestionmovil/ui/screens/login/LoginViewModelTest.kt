package com.example.gestionmovil.ui.screens.login

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
class LoginViewModelTest {

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
        val viewModel = LoginViewModel(app)

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
    }

    @Test
    fun testLoginValidationFail() {
        val app = TestUtils.createMockApplication()
        val viewModel = LoginViewModel(app)

        var successCalled = false
        viewModel.onLoginClick { successCalled = true }

        val state = viewModel.uiState.value
        assertFalse(successCalled)
        assertNotNull(state.errorMessage)
    }
}
