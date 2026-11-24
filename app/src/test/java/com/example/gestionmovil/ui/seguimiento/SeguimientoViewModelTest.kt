package com.example.gestionmovil.ui.seguimiento

import androidx.lifecycle.ViewModel
import com.example.gestionmovil.data.local.entities.SeguimientoEntity
import com.example.gestionmovil.data.repository.SeguimientoRepository
import com.example.gestionmovil.fakes.FakeSeguimientoApiService
import com.example.gestionmovil.fakes.FakeSeguimientoDao
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
class SeguimientoViewModelTest {

    private lateinit var fakeDao: FakeSeguimientoDao
    private lateinit var repository: SeguimientoRepository
    private lateinit var viewModel: SeguimientoViewModel
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        fakeDao = FakeSeguimientoDao()
        repository = SeguimientoRepository(fakeDao, FakeSeguimientoApiService())
        val app = TestUtils.createMockApplication()
        viewModel = SeguimientoViewModel(app, repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testCargarSeguimientosPorOportunidad() = runBlocking {
        val seg = SeguimientoEntity(
            id = 1L, oportunidadId = 10L, contactoId = 1L, tipoGestion = "Mail", propuesta = "P",
            respuestaCliente = "R", proximaAccion = "A", fechaGestion = "2025-01-01", fechaProximaAccion = "2025-01-05", observacion = ""
        )
        fakeDao.insertarSeguimiento(seg)

        viewModel.cargarSeguimientosPorOportunidad(10L)

        val state = viewModel.uiState.value
        assertEquals(10L, state.oportunidadId)
        assertEquals(1, state.listaSeguimientos.size)
    }

    @Test
    fun testFormInputAndGuardarGestion_Nuevo() = runBlocking {
        viewModel.onTipoGestionChanged("Llamada")
        viewModel.onPropuestaChanged("Propuesta X")
        viewModel.onRespuestaClienteChanged("Cliente interesado")
        viewModel.onProximaAccionChanged("Enviar mail")
        viewModel.onFechaGestionChanged("2025-01-10")
        viewModel.onFechaProximaAccionChanged("2025-01-15")
        viewModel.onObservacionChanged("Nota de seguimiento")

        val state = viewModel.uiState.value
        assertEquals("Llamada", state.tipoGestion)
        assertEquals("Propuesta X", state.propuesta)
        assertEquals("Cliente interesado", state.respuestaCliente)
        assertEquals("Enviar mail", state.proximaAccion)
        assertEquals("2025-01-10", state.fechaGestion)
        assertEquals("2025-01-15", state.fechaProximaAccion)
        assertEquals("Nota de seguimiento", state.observacion)

        viewModel.guardarGestion()
        kotlinx.coroutines.delay(200)
        assertTrue(viewModel.uiState.value.guardadoExitoso)
        assertEquals(1, fakeDao.seguimientos.size)
    }

    @Test
    fun testGuardarGestion_FechaGestionEnBlanco() = runBlocking {
        viewModel.onTipoGestionChanged("Llamada")
        viewModel.onFechaGestionChanged("") // Prueba el fallback ifBlank

        viewModel.guardarGestion()
        kotlinx.coroutines.delay(200)
        assertTrue(viewModel.uiState.value.guardadoExitoso)
        assertEquals(1, fakeDao.seguimientos.size)
        assertTrue(fakeDao.seguimientos[0].fechaGestion.isNotBlank())
    }

    @Test
    fun testGuardarGestion_ActualizarExistente() = runBlocking {
        val seg = SeguimientoEntity(
            id = 5L, oportunidadId = 1L, contactoId = 1L, tipoGestion = "Llamada", propuesta = "P1",
            respuestaCliente = "R1", proximaAccion = "A1", fechaGestion = "2025-01-01", fechaProximaAccion = "", observacion = ""
        )
        fakeDao.insertarSeguimiento(seg)

        viewModel.seleccionarParaEditar(seg)
        viewModel.onPropuestaChanged("Propuesta Modificada")
        viewModel.guardarGestion()
        kotlinx.coroutines.delay(200)

        assertTrue(viewModel.uiState.value.guardadoExitoso)
        val modificado = fakeDao.obtenerSeguimientoPorId(5L)
        assertEquals("Propuesta Modificada", modificado?.propuesta)
    }

    @Test
    fun testGuardarGestion_SinTipoGestion() = runBlocking {
        viewModel.onTipoGestionChanged("")
        viewModel.guardarGestion()

        assertEquals("Debes seleccionar un tipo de gestión", viewModel.uiState.value.mensajeError)
        assertFalse(viewModel.uiState.value.guardadoExitoso)
    }

    @Test
    fun testSeleccionarParaEditarYEliminar() = runBlocking {
        val seg = SeguimientoEntity(
            id = 5L,
            oportunidadId = 1L,
            contactoId = 1L,
            tipoGestion = "Reunión",
            propuesta = "Propuesta B",
            respuestaCliente = "Respuesta B",
            proximaAccion = "Acción B",
            fechaGestion = "2025-01-01",
            fechaProximaAccion = "2025-01-05",
            observacion = "Obs"
        )
        fakeDao.insertarSeguimiento(seg)

        viewModel.seleccionarParaEditar(seg)

        val state = viewModel.uiState.value
        assertEquals(5L, state.idSeleccionado)
        assertEquals("Reunión", state.tipoGestion)
        assertEquals("Propuesta B", state.propuesta)

        viewModel.eliminarSeguimiento(seg)
        kotlinx.coroutines.delay(200)
        assertEquals(0, fakeDao.seguimientos.size)
    }

    @Test
    fun testLimpiarFormulario() {
        viewModel.onTipoGestionChanged("Reunión")
        viewModel.onPropuestaChanged("Test")
        viewModel.limpiarFormulario()

        val state = viewModel.uiState.value
        assertNull(state.idSeleccionado)
        assertEquals("Llamada", state.tipoGestion)
        assertEquals("", state.propuesta)
    }

    @Test
    fun testSeguimientoViewModelFactory() {
        val app = TestUtils.createMockApplication()
        val factory = SeguimientoViewModelFactory(app, repository)
        val created = factory.create(SeguimientoViewModel::class.java)
        assertNotNull(created)
    }

    @Test(expected = IllegalArgumentException::class)
    fun testSeguimientoViewModelFactory_Desconocida() {
        val app = TestUtils.createMockApplication()
        val factory = SeguimientoViewModelFactory(app, repository)
        class DummyViewModel : ViewModel()
        factory.create(DummyViewModel::class.java)
    }
}
