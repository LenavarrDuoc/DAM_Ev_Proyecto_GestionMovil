package com.example.gestionmovil.ui.screens.oportunidad_list

import androidx.lifecycle.ViewModel
import com.example.gestionmovil.data.local.entities.OportunidadEntity
import com.example.gestionmovil.data.local.entities.OrganizacionEntity
import com.example.gestionmovil.data.repository.OportunidadRepository
import com.example.gestionmovil.fakes.FakeOportunidadDao
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

@OptIn(ExperimentalCoroutinesApi::class)
class OportunidadListViewModelTest {

    private lateinit var fakeDao: FakeOportunidadDao
    private lateinit var repository: OportunidadRepository
    private lateinit var viewModel: OportunidadListViewModel
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() = runBlocking {
        Dispatchers.setMain(testDispatcher)
        fakeDao = FakeOportunidadDao()
        repository = OportunidadRepository(fakeDao)

        fakeDao.organizaciones.add(
            OrganizacionEntity(id = 1L, nombre = "Empresa ACME", tipoOrganizacion = "T", rubro = "R", comuna = "C", region = "R")
        )

        fakeDao.insertOportunidad(
            OportunidadEntity(
                id = 10L, nombre = "Licencias Cloud", tipoOportunidad = "T", descripcion = "D",
                fuenteInformacion = "F", fechaDeteccion = "F", responsableInterno = "R",
                prioridad = "P", potencialEstimado = "P", estado = "En Proceso", fechaUltimaActualizacion = "F",
                organizacionId = 1L
            )
        )
        fakeDao.insertOportunidad(
            OportunidadEntity(
                id = 20L, nombre = "Consultoria DB", tipoOportunidad = "T", descripcion = "D",
                fuenteInformacion = "F", fechaDeteccion = "F", responsableInterno = "R",
                prioridad = "P", potencialEstimado = "P", estado = "Nueva", fechaUltimaActualizacion = "F",
                organizacionId = 99L
            )
        )

        viewModel = OportunidadListViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testSearchQueryAndFilterState() = runBlocking {
        // Inicialmente con 2 elementos
        val initialList = viewModel.uiState.first().oportunidades
        assertEquals(2, initialList.size)

        // Búsqueda por nombre de organización
        viewModel.onSearchQueryChanged("ACME")
        val filteredByOrg = viewModel.uiState.first().oportunidades
        assertEquals(1, filteredByOrg.size)
        assertEquals("Licencias Cloud", filteredByOrg[0].oportunidad.nombre)

        // Búsqueda por nombre de oportunidad
        viewModel.onSearchQueryChanged("Consultoria")
        val filteredByOppName = viewModel.uiState.first().oportunidades
        assertEquals(1, filteredByOppName.size)
        assertEquals("Consultoria DB", filteredByOppName[0].oportunidad.nombre)

        // Filtro por estado
        viewModel.onSearchQueryChanged("")
        viewModel.onEstadoFiltroSelected("Nueva")
        val filteredByState = viewModel.uiState.first().oportunidades
        assertEquals(1, filteredByState.size)
        assertEquals("Consultoria DB", filteredByState[0].oportunidad.nombre)

        // Visibilidad del filtro sheet
        assertFalse(viewModel.uiState.first().showFilterSheet)
        viewModel.toggleFilterVisibility()
        assertTrue(viewModel.uiState.first().showFilterSheet)
    }

    @Test
    fun testOportunidadListViewModelFactory() {
        val factory = OportunidadListViewModelFactory(repository)
        val created = factory.create(OportunidadListViewModel::class.java)
        assertNotNull(created)
    }

    @Test(expected = IllegalArgumentException::class)
    fun testOportunidadListViewModelFactory_Desconocida() {
        val factory = OportunidadListViewModelFactory(repository)
        class DummyViewModel : ViewModel()
        factory.create(DummyViewModel::class.java)
    }
}
