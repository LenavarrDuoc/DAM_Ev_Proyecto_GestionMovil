package com.example.gestionmovil.ui.screens.evento_form

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.gestionmovil.data.repository.EventoRepository

class EventoFormViewModelFactory(
    private val repository: EventoRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EventoFormViewModel::class.java)) {
            return EventoFormViewModel(repository) as T
        }
        throw IllegalArgumentException("ViewModel desconocido: ${modelClass.name}")
    }
}