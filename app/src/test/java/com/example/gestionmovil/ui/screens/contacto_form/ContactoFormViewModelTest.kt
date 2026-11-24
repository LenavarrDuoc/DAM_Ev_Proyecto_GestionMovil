package com.example.gestionmovil.ui.screens.contacto_form

import androidx.lifecycle.ViewModel
import com.example.gestionmovil.data.repository.ContactoRepository
import com.example.gestionmovil.fakes.FakeContactoApiService
import com.example.gestionmovil.fakes.FakeContactoDao
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
class ContactoFormViewModelTest {

    private lateinit var fakeDao: FakeContactoDao
    private lateinit var repository: ContactoRepository
    private lateinit var viewModel: ContactoFormViewModel
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        TestUtils.initPatterns()
        fakeDao = FakeContactoDao()
        repository = ContactoRepository(fakeDao, FakeContactoApiService())
        viewModel = ContactoFormViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testFormInputsAndValidation() {
        viewModel.onNombreContactoChanged("Carlos")
        viewModel.onCargoChanged("Jefe TI")
        viewModel.onCorreoChanged("carlos@empresa.com")
        viewModel.onTelefonoChanged("+56912345678")
        viewModel.onAreaSelected("TI")
        viewModel.onMedioPreferenteSelected("Email")
        viewModel.onObservacionChanged("Nota de prueba")
        viewModel.onOrganizacionSelected(10L)

        val state = viewModel.uiState.value
        assertEquals("Carlos", state.nombreContacto)
        assertEquals("Jefe TI", state.cargo)
        assertEquals("carlos@empresa.com", state.correo)
        assertEquals("+56912345678", state.telefono)
        assertEquals("TI", state.area)
        assertEquals("Email", state.medioPreferente)
        assertEquals("Nota de prueba", state.observacion)
        assertEquals(10L, state.organizacionIdSeleccionada)
        assertTrue(state.isFormValid)
    }

    @Test
    fun testFormInputs_FormInvalido() {
        viewModel.onNombreContactoChanged("")
        viewModel.onCargoChanged("Jefe TI")
        viewModel.onCorreoChanged("invalido")
        viewModel.onTelefonoChanged("123")

        val state = viewModel.uiState.value
        assertFalse(state.isFormValid)

        // Al no ser válido, saveContacto no debe guardar nada
        viewModel.saveContacto("2025-01-01")
        assertFalse(viewModel.uiState.value.isSavedSuccess)
        assertEquals(0, fakeDao.contactos.size)
    }

    @Test
    fun testSaveContacto_ConOrganizacionIdNull() = runBlocking {
        viewModel.onNombreContactoChanged("Ana")
        viewModel.onCargoChanged("Analista")
        viewModel.onCorreoChanged("ana@empresa.com")
        viewModel.onTelefonoChanged("+56987654321")
        viewModel.onOrganizacionSelected(null) // Prueba fallback 1L

        viewModel.saveContacto("2025-01-01")
        kotlinx.coroutines.delay(200)

        assertTrue(viewModel.uiState.value.isSavedSuccess)
        assertEquals(1, fakeDao.contactos.size)
        assertEquals(1L, fakeDao.contactos[0].organizacionId)
    }

    @Test
    fun testContactoFormViewModelFactory() {
        val factory = ContactoFormViewModelFactory(repository)
        val created = factory.create(ContactoFormViewModel::class.java)
        assertNotNull(created)
    }

    @Test(expected = IllegalArgumentException::class)
    fun testContactoFormViewModelFactory_Desconocida() {
        val factory = ContactoFormViewModelFactory(repository)
        class DummyViewModel : ViewModel()
        factory.create(DummyViewModel::class.java)
    }
}
