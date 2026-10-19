package com.example.gestionmovil.ui.screens.oportunidad_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.gestionmovil.data.repository.OportunidadRepository

class OportunidadListViewModelFactory(
    private val repository: OportunidadRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(OportunidadListViewModel::class.java)) {
            return OportunidadListViewModel(repository) as T
        }
        throw IllegalArgumentException("ViewModel Class no encontrada: ${modelClass.name}")
    }
}