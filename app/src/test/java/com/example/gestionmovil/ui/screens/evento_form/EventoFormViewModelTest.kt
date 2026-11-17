package com.example.gestionmovil.ui.screens.evento_form

import com.example.gestionmovil.data.repository.EventoRepository
import com.example.gestionmovil.fakes.FakeEventoDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class EventoFormViewModelTest {

    private lateinit var fakeDao: FakeEventoDao
    private lateinit var repository: EventoRepository
    private lateinit var viewModel: EventoFormViewModel
    private val testDispatcher: TestDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        fakeDao = FakeEventoDao()
        repository = EventoRepository(fakeDao)
        viewModel = EventoFormViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testFormInputs() {
        viewModel.onTituloChange("Conferencia Tech")
        viewModel.onTipoEventoChange("Conferencia")
        viewModel.onFechaChange("2025-06-01")
        viewModel.onHoraChange("14:30")
        viewModel.onRegionChange("Metropolitana")
        viewModel.onComunaChange("Las Condes")
        viewModel.onOrganizacionChange("Empresa ABC")
        viewModel.onPublicoEsperadoChange("200")
        viewModel.onCondicionesChange("Entrada liberada")

        val state = viewModel.uiState.value
        assertEquals("Conferencia Tech", state.titulo)
        assertEquals("Conferencia", state.tipoEvento)
        assertEquals("2025-06-01", state.fecha)
        assertEquals("14:30", state.hora)
        assertEquals("Metropolitana", state.region)
        assertEquals("Las Condes", state.comuna)
        assertEquals("Empresa ABC", state.organizacionNombre)
        assertEquals("200", state.publicoEsperado)
        assertEquals("Entrada liberada", state.condicionesParticipacion)
    }

    @Test
    fun testPublicoEsperadoOnlyDigits() {
        viewModel.onPublicoEsperadoChange("100")
        assertEquals("100", viewModel.uiState.value.publicoEsperado)

        viewModel.onPublicoEsperadoChange("100abc")
        assertEquals("100", viewModel.uiState.value.publicoEsperado)
    }
}
