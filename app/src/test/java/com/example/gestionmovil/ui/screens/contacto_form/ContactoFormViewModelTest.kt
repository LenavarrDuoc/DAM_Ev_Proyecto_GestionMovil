package com.example.gestionmovil.ui.screens.contacto_form

import com.example.gestionmovil.data.repository.ContactoRepository
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
        repository = ContactoRepository(fakeDao)
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
    fun testSaveContacto() = runBlocking {
        viewModel.onNombreContactoChanged("Ana")
        viewModel.onCargoChanged("Analista")
        viewModel.onCorreoChanged("ana@empresa.com")
        viewModel.onTelefonoChanged("+56987654321")

        viewModel.saveContacto("2025-01-01")

        assertTrue(viewModel.uiState.value.isSavedSuccess)
        assertEquals(1, fakeDao.contactos.size)
        assertEquals("Ana", fakeDao.contactos[0].nombre)
    }
}
