package com.example.gestionmovil.ui.seguimiento

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.gestionmovil.data.local.entities.SeguimientoEntity
import com.example.gestionmovil.data.repository.SeguimientoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SeguimientoViewModel(
    application: Application,
    private val repository: SeguimientoRepository
) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(SeguimientoUiState())
    val uiState: StateFlow<SeguimientoUiState> = _uiState.asStateFlow()

    init {
        val fechaHoy = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        _uiState.update { it.copy(fechaGestion = fechaHoy) }
    }

    fun cargarSeguimientosPorOportunidad(oportunidadId: Long) {
        _uiState.update { it.copy(oportunidadId = oportunidadId, isLoading = true) }
        viewModelScope.launch {
            repository.obtenerSeguimientosPorOportunidad(oportunidadId).collect { lista ->
                _uiState.update {
                    it.copy(listaSeguimientos = lista, isLoading = false)
                }
            }
        }
    }

    fun onTipoGestionChanged(nuevoTipo: String) {
        _uiState.update { it.copy(tipoGestion = nuevoTipo) }
    }

    fun onPropuestaChanged(nuevaPropuesta: String) {
        _uiState.update { it.copy(propuesta = nuevaPropuesta) }
    }

    fun onRespuestaClienteChanged(nuevaRespuesta: String) {
        _uiState.update { it.copy(respuestaCliente = nuevaRespuesta) }
    }

    fun onProximaAccionChanged(nuevaAccion: String) {
        _uiState.update { it.copy(proximaAccion = nuevaAccion) }
    }

    fun onFechaGestionChanged(nuevaFecha: String) {
        _uiState.update { it.copy(fechaGestion = nuevaFecha) }
    }

    fun onFechaProximaAccionChanged(nuevaFecha: String) {
        _uiState.update { it.copy(fechaProximaAccion = nuevaFecha) }
    }

    fun onObservacionChanged(nuevaObservacion: String) {
        _uiState.update { it.copy(observacion = nuevaObservacion) }
    }

    fun guardarGestion() {
        val state = _uiState.value

        if (state.tipoGestion.isBlank()) {
            _uiState.update { it.copy(mensajeError = "Debes seleccionar un tipo de gestión") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, mensajeError = null) }

            val seguimiento = SeguimientoEntity(
                id = state.idSeleccionado ?: 0L,
                oportunidadId = state.oportunidadId,
                contactoId = state.contactoId,
                tipoGestion = state.tipoGestion,
                propuesta = state.propuesta.trim(),
                respuestaCliente = state.respuestaCliente.trim(),
                proximaAccion = state.proximaAccion.trim(),
                fechaGestion = state.fechaGestion.ifBlank {
                    SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                },
                fechaProximaAccion = state.fechaProximaAccion.trim(),
                observacion = state.observacion.trim()
            )

            if (state.idSeleccionado == null) {
                repository.guardarSeguimiento(seguimiento)
            } else {
                repository.actualizarSeguimiento(seguimiento)
            }

            limpiarFormulario()
            _uiState.update { it.copy(guardadoExitoso = true, isLoading = false) }
        }
    }

    fun seleccionarParaEditar(seguimiento: SeguimientoEntity) {
        _uiState.update {
            it.copy(
                idSeleccionado = seguimiento.id,
                tipoGestion = seguimiento.tipoGestion,
                propuesta = seguimiento.propuesta,
                respuestaCliente = seguimiento.respuestaCliente,
                proximaAccion = seguimiento.proximaAccion,
                fechaGestion = seguimiento.fechaGestion,
                fechaProximaAccion = seguimiento.fechaProximaAccion,
                observacion = seguimiento.observacion
            )
        }
    }

    fun eliminarSeguimiento(seguimiento: SeguimientoEntity) {
        viewModelScope.launch {
            repository.eliminarSeguimiento(seguimiento)
        }
    }

    fun limpiarFormulario() {
        val fechaHoy = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        _uiState.update {
            it.copy(
                idSeleccionado = null,
                tipoGestion = "Llamada",
                propuesta = "",
                respuestaCliente = "",
                proximaAccion = "",
                fechaGestion = fechaHoy,
                fechaProximaAccion = "",
                observacion = "",
                mensajeError = null,
                guardadoExitoso = false
            )
        }
    }
}