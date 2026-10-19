package com.example.gestionmovil.ui.seguimiento

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.gestionmovil.data.repository.SeguimientoRepository

class SeguimientoViewModelFactory(
    private val application: Application,
    private val repository: SeguimientoRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SeguimientoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SeguimientoViewModel(application, repository) as T
        }
        throw IllegalArgumentException("Clase ViewModel desconocida")
    }
}