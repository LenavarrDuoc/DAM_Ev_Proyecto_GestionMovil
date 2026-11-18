package com.example.gestionmovil.ui.screens

import androidx.lifecycle.ViewModel
import com.example.gestionmovil.data.local.entities.OportunidadEntity
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
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class InicioViewModelTest {

    private lateinit var fakeDao: FakeOportunidadDao
    private lateinit var repository: OportunidadRepository
    private lateinit var viewModel: InicioViewModel
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        fakeDao = FakeOportunidadDao()
        repository = OportunidadRepository(fakeDao)
        viewModel = InicioViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun cantidadOportunidades_InicialCero() = runBlocking {
        val count = viewModel.cantidadOportunidades.value
        assertEquals(0, count)
    }

    @Test
    fun cantidadOportunidades_SeActualizaAlInsertar() = runBlocking {
        fakeDao.insertOportunidad(
            OportunidadEntity(
                id = 1L, nombre = "Op Test", tipoOportunidad = "T", descripcion = "D",
                fuenteInformacion = "F", fechaDeteccion = "F", responsableInterno = "R",
                prioridad = "P", potencialEstimado = "P", estado = "E", fechaUltimaActualizacion = "F",
                organizacionId = 1L
            )
        )

        val count = repository.getCantidadOportunidades().first()
        assertEquals(1, count)
    }

    @Test
    fun testInicioViewModelFactory() {
        val factory = InicioViewModelFactory(repository)
        val created = factory.create(InicioViewModel::class.java)
        assertNotNull(created)
    }

    @Test(expected = IllegalArgumentException::class)
    fun testInicioViewModelFactory_Desconocida() {
        val factory = InicioViewModelFactory(repository)
        class DummyViewModel : ViewModel()
        factory.create(DummyViewModel::class.java)
    }
}
