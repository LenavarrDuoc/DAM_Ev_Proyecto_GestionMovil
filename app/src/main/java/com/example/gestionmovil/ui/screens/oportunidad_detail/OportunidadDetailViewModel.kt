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

    /**
     * Carga la oportunidad por su ID desde el repositorio
     */
    fun loadOportunidad(id: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val item = repository.getOportunidadById(id)
                _uiState.update { it.copy(oportunidad = item, isLoading = false) }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "Error al cargar la oportunidad"
                    )
                }
            }
        }
    }

    /**
     * Actualiza el estado de la oportunidad (RBAC: Ejecutivo vs Supervisor)
     */
    fun updateEstado(newStatus: String, currentDate: String) {
        val currentOpp = _uiState.value.oportunidad ?: return
        viewModelScope.launch {
            try {
                repository.updateEstadoOportunidad(currentOpp.id, newStatus, currentDate)
                // Recargar datos actualizados desde Room
                loadOportunidad(currentOpp.id)
                _uiState.update { it.copy(isStatusUpdated = true) }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(errorMessage = e.message ?: "Error al actualizar estado")
                }
            }
        }
    }

    /**
     * Reasigna el ejecutivo responsable de la oportunidad (Uso exclusivo del Supervisor - Requerimiento 1.4)
     */
    fun reorganizarResponsable(oportunidadId: Long, nuevoResponsable: String) {
        viewModelScope.launch {
            try {
                val currentOpp = _uiState.value.oportunidad
                if (currentOpp != null) {
                    val oportunidadActualizada = currentOpp.copy(
                        responsableInterno = nuevoResponsable
                    )
                    // Actualizar entidad en base de datos
                    repository.updateOportunidad(oportunidadActualizada)
                    loadOportunidad(oportunidadId)
                    _uiState.update { it.copy(isResponsableUpdated = true) }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(errorMessage = e.message ?: "Error al reasignar responsable")
                }
            }
        }
    }
}