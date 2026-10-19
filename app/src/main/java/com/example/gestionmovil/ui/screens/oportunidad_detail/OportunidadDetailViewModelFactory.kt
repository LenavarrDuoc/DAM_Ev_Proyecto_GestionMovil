package com.example.gestionmovil.ui.screens.oportunidad_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.gestionmovil.data.repository.OportunidadRepository

class OportunidadDetailViewModelFactory(
    private val repository: OportunidadRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(OportunidadDetailViewModel::class.java)) {
            return OportunidadDetailViewModel(repository) as T
        }
        throw IllegalArgumentException("Clase ViewModel no encontrada: ${modelClass.name}")
    }
}