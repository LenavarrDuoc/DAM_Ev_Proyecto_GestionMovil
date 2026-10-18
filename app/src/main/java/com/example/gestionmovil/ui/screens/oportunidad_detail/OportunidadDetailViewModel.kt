package com.example.gestionmovil.ui.screens.oportunidad_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gestionmovil.data.repository.OportunidadRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class OportunidadDetailViewModel(
    private val repository: OportunidadRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(OportunidadDetailUiState())
    val uiState: StateFlow<OportunidadDetailUiState> = _uiState.asStateFlow()

    fun loadOportunidad(id: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val item = repository.getOportunidadById(id)
            _uiState.update { it.copy(oportunidad = item, isLoading = false) }
        }
    }

    fun updateEstado(newStatus: String, currentDate: String) {
        val currentOpp = _uiState.value.oportunidad ?: return
        viewModelScope.launch {
            repository.updateEstadoOportunidad(currentOpp.id, newStatus, currentDate)
            // Recargar datos actualizados
            loadOportunidad(currentOpp.id)
            _uiState.update { it.copy(isStatusUpdated = true) }
        }
    }
}