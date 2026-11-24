package com.example.gestionmovil.ui.screens.evento_form

import android.content.ContextWrapper
import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import com.example.gestionmovil.data.local.entities.EventoEntity
import com.example.gestionmovil.data.repository.EventoRepository
import com.example.gestionmovil.fakes.FakeEventoApiService
import com.example.gestionmovil.fakes.FakeEventoDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.io.File

@OptIn(ExperimentalCoroutinesApi::class)
class EventoFormViewModelTest {

    private lateinit var fakeDao: FakeEventoDao
    private lateinit var repository: EventoRepository
    private lateinit var viewModel: EventoFormViewModel
    private val testDispatcher = UnconfinedTestDispatcher()

    private fun createBitmapInstance(): Bitmap {
        val unsafeClass = Class.forName("sun.misc.Unsafe")
        val theUnsafeField = unsafeClass.getDeclaredField("theUnsafe")
        theUnsafeField.isAccessible = true
        val unsafe = theUnsafeField.get(null)
        val allocateInstance = unsafeClass.getMethod("allocateInstance", Class::class.java)
        return allocateInstance.invoke(unsafe, Bitmap::class.java) as Bitmap
    }

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        fakeDao = FakeEventoDao()
        repository = EventoRepository(fakeDao, FakeEventoApiService())
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

        viewModel.onPublicoEsperadoChange("")
        assertEquals("", viewModel.uiState.value.publicoEsperado)
    }

    @Test
    fun testGuardarEvento_FormInvalido() = runBlocking {
        viewModel.onTituloChange("")
        viewModel.onOrganizacionChange("Empresa ABC")

        val mockContext = object : ContextWrapper(null) {
            override fun getFilesDir(): File = File(System.getProperty("java.io.tmpdir") ?: ".")
        }

        var successCalled = false
        viewModel.guardarEvento(mockContext) { successCalled = true }

        assertFalse(successCalled)
        assertEquals("Completa los campos obligatorios y toma la foto", viewModel.uiState.value.errorMessage)
    }

    @Test
    fun testGuardarEvento_Exitoso() = runBlocking {
        viewModel.onTituloChange("Seminario")
        viewModel.onOrganizacionChange("Org XYZ")
        viewModel.onPublicoEsperadoChange("50")

        val bitmap = createBitmapInstance()
        viewModel.onFotoCapturada(bitmap)

        val mockContext = object : ContextWrapper(null) {
            override fun getFilesDir(): File = File(System.getProperty("java.io.tmpdir") ?: ".")
        }

        var savedEntity: EventoEntity? = null
        viewModel.guardarEvento(mockContext) { savedEntity = it }
        kotlinx.coroutines.delay(200)

        assertNotNull(savedEntity)
        assertEquals("Seminario", savedEntity?.titulo)
        assertTrue(viewModel.uiState.value.isEventoGuardado)
        assertEquals(1, fakeDao.eventos.size)

        val listFromState = viewModel.eventosList.first()
        assertEquals(1, listFromState.size)
    }

    @Test
    fun testCargarEventoParaEditarYActualizar() = runBlocking {
        val evento = EventoEntity(
            id = "EVT-10",
            titulo = "Seminario Original",
            tipoEvento = "Seminario",
            fecha = "2025-08-10",
            hora = "09:00",
            region = "Metropolitana",
            comuna = "Santiago",
            organizacionId = 1L,
            organizacionNombre = "Org Test",
            publicoEsperado = 80,
            condicionesParticipacion = "Ninguna",
            rutaFotoEvidencia = null
        )
        fakeDao.insertarEvento(evento)

        viewModel.cargarEventoParaEditar("EVT-10")
        kotlinx.coroutines.delay(100)

        val state = viewModel.uiState.value
        assertTrue(state.isEditing)
        assertEquals("EVT-10", state.id)
        assertEquals("Seminario Original", state.titulo)

        viewModel.onTituloChange("Seminario Modificado")

        val mockContext = object : ContextWrapper(null) {
            override fun getFilesDir(): File = File(System.getProperty("java.io.tmpdir") ?: ".")
        }

        var updatedEntity: EventoEntity? = null
        viewModel.guardarEvento(mockContext) { updatedEntity = it }
        kotlinx.coroutines.delay(200)

        assertNotNull(updatedEntity)
        assertEquals("Seminario Modificado", updatedEntity?.titulo)
    }

    @Test
    fun testEliminarEvento() = runBlocking {
        val evento = EventoEntity(
            id = "EVT-99",
            titulo = "Evento a borrar",
            tipoEvento = "Taller",
            fecha = "2025-09-01",
            hora = "10:00",
            region = "Valparaíso",
            comuna = "Valparaíso",
            organizacionId = 1L,
            organizacionNombre = "Org Valpo",
            publicoEsperado = 20,
            condicionesParticipacion = "",
            rutaFotoEvidencia = null
        )
        fakeDao.insertarEvento(evento)
        assertEquals(1, fakeDao.eventos.size)

        var eliminado = false
        viewModel.eliminarEvento("EVT-99") {
            eliminado = true
        }
        kotlinx.coroutines.delay(200)

        assertTrue(eliminado)
        assertEquals(0, fakeDao.eventos.size)
    }

    @Test
    fun testEventoFormViewModelFactory() {
        val factory = EventoFormViewModelFactory(repository)
        val created = factory.create(EventoFormViewModel::class.java)
        assertNotNull(created)
    }

    @Test(expected = IllegalArgumentException::class)
    fun testEventoFormViewModelFactory_Desconocida() {
        val factory = EventoFormViewModelFactory(repository)
        class DummyViewModel : ViewModel()
        factory.create(DummyViewModel::class.java)
    }
}
