package com.example.gestionmovil.ui.screens.oportunidad_form

import com.example.gestionmovil.data.repository.OportunidadRepository
import com.example.gestionmovil.fakes.FakeOportunidadDao
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
class OportunidadFormViewModelTest {

    private lateinit var fakeDao: FakeOportunidadDao
    private lateinit var repository: OportunidadRepository
    private lateinit var viewModel: OportunidadFormViewModel
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        TestUtils.initPatterns()
        fakeDao = FakeOportunidadDao()
        repository = OportunidadRepository(fakeDao)
        viewModel = OportunidadFormViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testFormFieldsValidationAndSave() = runBlocking {
        viewModel.onNombreChanged("Venta Licencias")
        viewModel.onDescripcionChanged("Licencias Anuales")
        viewModel.onFuenteInformacionChanged("Sitio Web")
        viewModel.onResponsableChanged("Pedro Ejecutivo")
        viewModel.onTipoOportunidadSelected("Software")
        viewModel.onPrioridadSelected("Alta")
        viewModel.onPotencialSelected("$50.000")
        viewModel.onOrganizacionSelected(5L)
        viewModel.onObservacionChanged("Prioritario")

        val state = viewModel.uiState.value
        assertEquals("Venta Licencias", state.nombre)
        assertEquals("Licencias Anuales", state.descripcion)
        assertEquals("Sitio Web", state.fuenteInformacion)
        assertEquals("Pedro Ejecutivo", state.responsableInterno)
        assertEquals("Software", state.tipoOportunidad)
        assertEquals("Alta", state.prioridad)
        assertEquals("$50.000", state.potencialEstimado)
        assertEquals(5L, state.organizacionIdSeleccionada)
        assertEquals("Prioritario", state.observacion)
        assertTrue(state.isFormValid)

        viewModel.saveOportunidad("2025-01-01")
        assertTrue(viewModel.uiState.value.isSavedSuccess)
        assertEquals(1, fakeDao.oportunidades.size)
        assertEquals("Venta Licencias", fakeDao.oportunidades[0].nombre)
    }
}
