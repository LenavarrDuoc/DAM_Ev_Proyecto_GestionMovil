package com.example.gestionmovil.ui.screens.oportunidad_form

import androidx.lifecycle.ViewModel
import com.example.gestionmovil.data.repository.OportunidadRepository
import com.example.gestionmovil.fakes.FakeOportunidadApiService
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
        repository = OportunidadRepository(fakeDao, FakeOportunidadApiService())
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
        kotlinx.coroutines.delay(200)
        assertTrue(viewModel.uiState.value.isSavedSuccess)
        assertEquals(1, fakeDao.oportunidades.size)
        assertEquals("Venta Licencias", fakeDao.oportunidades[0].nombre)
    }

    @Test
    fun testSaveOportunidad_FormInvalido() = runBlocking {
        viewModel.onNombreChanged("") // Inválido
        viewModel.onDescripcionChanged("Desc")
        viewModel.onFuenteInformacionChanged("Web")
        viewModel.onResponsableChanged("Resp")

        assertFalse(viewModel.uiState.value.isFormValid)

        viewModel.saveOportunidad("2025-01-01")
        assertFalse(viewModel.uiState.value.isSavedSuccess)
        assertEquals(0, fakeDao.oportunidades.size)
    }

    @Test
    fun testCargarOportunidadParaEditarYActualizar() = runBlocking {
        val op = com.example.gestionmovil.data.local.entities.OportunidadEntity(
            id = 50L,
            nombre = "Oportunidad Original",
            tipoOportunidad = "Servicio",
            descripcion = "Desc original",
            fuenteInformacion = "Contacto directo",
            fechaDeteccion = "2025-01-01",
            responsableInterno = "Carlos",
            prioridad = "Media",
            potencialEstimado = "Medio",
            estado = "Evento identificado",
            fechaUltimaActualizacion = "2025-01-01",
            organizacionId = 2L
        )
        fakeDao.insertOportunidad(op)

        viewModel.cargarOportunidadParaEditar(50L)
        kotlinx.coroutines.delay(100)

        val state = viewModel.uiState.value
        assertTrue(state.isEditing)
        assertEquals(50L, state.id)
        assertEquals("Oportunidad Original", state.nombre)

        viewModel.onNombreChanged("Oportunidad Editada")
        viewModel.onPrioridadSelected("Alta")

        viewModel.saveOportunidad("2025-01-10")
        kotlinx.coroutines.delay(200)

        assertTrue(viewModel.uiState.value.isSavedSuccess)

        val updated = fakeDao.getOportunidadById(50L)
        assertNotNull(updated)
        assertEquals("Oportunidad Editada", updated?.nombre)
        assertEquals("Alta", updated?.prioridad)
    }

    @Test
    fun testOportunidadFormViewModelFactory() {
        val factory = OportunidadFormViewModelFactory(repository)
        val created = factory.create(OportunidadFormViewModel::class.java)
        assertNotNull(created)
    }

    @Test(expected = IllegalArgumentException::class)
    fun testOportunidadFormViewModelFactory_Desconocida() {
        val factory = OportunidadFormViewModelFactory(repository)
        class DummyViewModel : ViewModel()
        factory.create(DummyViewModel::class.java)
    }
}
