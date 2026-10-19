package com.example.gestionmovil.ui.screens.oportunidad_form

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.gestionmovil.data.repository.OportunidadRepository

class OportunidadFormViewModelFactory(
    private val repository: OportunidadRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(OportunidadFormViewModel::class.java)) {
            return OportunidadFormViewModel(repository) as T
        }
        throw IllegalArgumentException("Clase ViewModel no encontrada: ${modelClass.name}")
    }
}