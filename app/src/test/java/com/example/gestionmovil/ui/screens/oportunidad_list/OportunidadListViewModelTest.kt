package com.example.gestionmovil.ui.screens.oportunidad_list

import com.example.gestionmovil.data.local.entities.OportunidadEntity
import com.example.gestionmovil.data.local.entities.OrganizacionEntity
import com.example.gestionmovil.data.repository.OportunidadRepository
import com.example.gestionmovil.fakes.FakeOportunidadDao
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
class OportunidadListViewModelTest {

    private lateinit var fakeDao: FakeOportunidadDao
    private lateinit var repository: OportunidadRepository
    private lateinit var viewModel: OportunidadListViewModel
    private val testDispatcher: TestDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        fakeDao = FakeOportunidadDao()
        repository = OportunidadRepository(fakeDao)

        fakeDao.organizaciones.add(
            OrganizacionEntity(id = 1L, nombre = "Empresa ACME", tipoOrganizacion = "T", rubro = "R", comuna = "C", region = "R")
        )
        fakeDao.oportunidades.add(
            OportunidadEntity(
                id = 10L, nombre = "Licencias Cloud", tipoOportunidad = "T", descripcion = "D",
                fuenteInformacion = "F", fechaDeteccion = "F", responsableInterno = "R",
                prioridad = "P", potencialEstimado = "P", estado = "En Proceso", fechaUltimaActualizacion = "F",
                organizacionId = 1L
            )
        )

        viewModel = OportunidadListViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testSearchQueryAndFilterState() {
        viewModel.onSearchQueryChanged("Cloud")
        viewModel.onEstadoFiltroSelected("En Proceso")

        viewModel.toggleFilterVisibility()
    }
}
