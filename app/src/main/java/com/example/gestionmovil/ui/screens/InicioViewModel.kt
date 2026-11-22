package com.example.gestionmovil.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.gestionmovil.data.repository.OportunidadRepository
import com.example.gestionmovil.data.repository.OrganizacionRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class InicioViewModel(
    private val oportunidadRepository: OportunidadRepository,
    private val organizacionRepository: OrganizacionRepository? = null
) : ViewModel() {

    init {
        viewModelScope.launch {
            organizacionRepository?.sincronizarOrganizaciones()
            oportunidadRepository.sincronizarOportunidades()
        }
    }

    // Observa en tiempo real la cantidad total de oportunidades almacenadas en Room
    val cantidadOportunidades: StateFlow<Int> = oportunidadRepository
        .getCantidadOportunidades()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0
        )
}

class InicioViewModelFactory(
    private val oportunidadRepository: OportunidadRepository,
    private val organizacionRepository: OrganizacionRepository? = null
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(InicioViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return InicioViewModel(oportunidadRepository, organizacionRepository) as T
        }
        throw IllegalArgumentException("ViewModel clase desconocida: ${modelClass.name}")
    }
}