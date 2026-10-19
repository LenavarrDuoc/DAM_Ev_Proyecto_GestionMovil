package com.example.gestionmovil.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.gestionmovil.data.repository.OportunidadRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class InicioViewModel(
    private val oportunidadRepository: OportunidadRepository
) : ViewModel() {

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
    private val oportunidadRepository: OportunidadRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(InicioViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return InicioViewModel(oportunidadRepository) as T
        }
        throw IllegalArgumentException("ViewModel clase desconocida: ${modelClass.name}")
    }
}