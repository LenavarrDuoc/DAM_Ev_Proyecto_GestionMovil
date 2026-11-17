package com.example.gestionmovil.ui.screens.oportunidad_detail

import com.example.gestionmovil.data.local.entities.OportunidadEntity
import com.example.gestionmovil.data.repository.OportunidadRepository
import com.example.gestionmovil.fakes.FakeOportunidadDao
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
class OportunidadDetailViewModelTest {

    private lateinit var fakeDao: FakeOportunidadDao
    private lateinit var repository: OportunidadRepository
    private lateinit var viewModel: OportunidadDetailViewModel
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        fakeDao = FakeOportunidadDao()
        repository = OportunidadRepository(fakeDao)
        viewModel = OportunidadDetailViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun loadOportunidad_Success() = runBlocking {
        val op = OportunidadEntity(
            id = 100L, nombre = "Licencia CRM", tipoOportunidad = "Soft", descripcion = "Desc",
            fuenteInformacion = "Web", fechaDeteccion = "2025-01-01", responsableInterno = "Juan",
            prioridad = "Alta", potencialEstimado = "$1000", estado = "Nueva", fechaUltimaActualizacion = "2025-01-01",
            organizacionId = 1L
        )
        fakeDao.insertOportunidad(op)

        viewModel.loadOportunidad(100L)

        val loaded = viewModel.uiState.value.oportunidad
        assertNotNull(loaded)
        assertEquals("Licencia CRM", loaded?.nombre)
    }

    @Test
    fun updateEstado() = runBlocking {
        val op = OportunidadEntity(
            id = 100L, nombre = "Licencia CRM", tipoOportunidad = "Soft", descripcion = "Desc",
            fuenteInformacion = "Web", fechaDeteccion = "2025-01-01", responsableInterno = "Juan",
            prioridad = "Alta", potencialEstimado = "$1000", estado = "Nueva", fechaUltimaActualizacion = "2025-01-01",
            organizacionId = 1L
        )
        fakeDao.insertOportunidad(op)
        viewModel.loadOportunidad(100L)

        viewModel.updateEstado("Ganada", "2025-01-05")

        val res = fakeDao.getOportunidadById(100L)
        assertEquals("Ganada", res?.estado)
    }

    @Test
    fun reorganizarResponsable() = runBlocking {
        val op = OportunidadEntity(
            id = 100L, nombre = "Licencia CRM", tipoOportunidad = "Soft", descripcion = "Desc",
            fuenteInformacion = "Web", fechaDeteccion = "2025-01-01", responsableInterno = "Juan",
            prioridad = "Alta", potencialEstimado = "$1000", estado = "Nueva", fechaUltimaActualizacion = "2025-01-01",
            organizacionId = 1L
        )
        fakeDao.insertOportunidad(op)
        viewModel.loadOportunidad(100L)

        viewModel.reorganizarResponsable(100L, "Maria Supervisora")

        val res = fakeDao.getOportunidadById(100L)
        assertEquals("Maria Supervisora", res?.responsableInterno)
    }
}
