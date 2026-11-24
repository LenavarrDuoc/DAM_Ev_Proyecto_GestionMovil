package com.example.gestionmovil.ui.screens.oportunidad_form

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gestionmovil.data.local.entities.OportunidadEntity
import com.example.gestionmovil.data.repository.OportunidadRepository
import com.example.gestionmovil.utils.Validaciones
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class OportunidadFormViewModel(
    private val repository: OportunidadRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(OportunidadFormUiState())
    val uiState: StateFlow<OportunidadFormUiState> = _uiState.asStateFlow()

    fun onNombreChanged(value: String) {
        val error = Validaciones.validarNotEmpty(value, "Nombre de la oportunidad")
        _uiState.update { it.copy(nombre = value, nombreError = error) }
        validarFormulario()
    }

    fun onDescripcionChanged(value: String) {
        val error = Validaciones.validarNotEmpty(value, "Descripción")
        _uiState.update { it.copy(descripcion = value, descripcionError = error) }
        validarFormulario()
    }

    fun onFuenteInformacionChanged(value: String) {
        val error = Validaciones.validarNotEmpty(value, "Fuente de información")
        _uiState.update { it.copy(fuenteInformacion = value, fuenteInformacionError = error) }
        validarFormulario()
    }

    fun onResponsableChanged(value: String) {
        val error = Validaciones.validarNotEmpty(value, "Responsable interno")
        _uiState.update { it.copy(responsableInterno = value, responsableInternoError = error) }
        validarFormulario()
    }

    fun onTipoOportunidadSelected(value: String) { _uiState.update { it.copy(tipoOportunidad = value) } }
    fun onPrioridadSelected(value: String) { _uiState.update { it.copy(prioridad = value) } }
    fun onPotencialSelected(value: String) { _uiState.update { it.copy(potencialEstimado = value) } }
    fun onOrganizacionSelected(id: Long?) { _uiState.update { it.copy(organizacionIdSeleccionada = id) } }
    fun onObservacionChanged(value: String) { _uiState.update { it.copy(observacion = value) } }

    fun cargarOportunidadParaEditar(id: Long) {
        viewModelScope.launch {
            val op = repository.getOportunidadById(id)
            if (op != null) {
                _uiState.update {
                    it.copy(
                        id = op.id,
                        nombre = op.nombre,
                        tipoOportunidad = op.tipoOportunidad,
                        descripcion = op.descripcion,
                        fuenteInformacion = op.fuenteInformacion,
                        responsableInterno = op.responsableInterno,
                        prioridad = op.prioridad,
                        potencialEstimado = op.potencialEstimado,
                        estado = op.estado,
                        organizacionIdSeleccionada = op.organizacionId,
                        observacion = op.observacion,
                        isEditing = true
                    )
                }
                validarFormulario()
            }
        }
    }

    fun limpiarFormulario() {
        _uiState.value = OportunidadFormUiState()
    }

    private fun validarFormulario() {
        val s = _uiState.value
        val isValid = s.nombreError == null && s.nombre.isNotBlank() &&
                s.descripcionError == null && s.descripcion.isNotBlank() &&
                s.fuenteInformacionError == null && s.fuenteInformacion.isNotBlank() &&
                s.responsableInternoError == null && s.responsableInterno.isNotBlank()

        _uiState.update { it.copy(isFormValid = isValid) }
    }

    fun saveOportunidad(currentDate: String) {
        if (!_uiState.value.isFormValid) return

        viewModelScope.launch {
            val s = _uiState.value
            val opExistente = if (s.isEditing && s.id != 0L) {
                repository.getOportunidadById(s.id)
            } else null

            val entity = OportunidadEntity(
                id = s.id,
                nombre = s.nombre.trim(),
                tipoOportunidad = s.tipoOportunidad,
                descripcion = s.descripcion.trim(),
                fuenteInformacion = s.fuenteInformacion.trim(),
                fechaDeteccion = opExistente?.fechaDeteccion ?: currentDate,
                responsableInterno = s.responsableInterno.trim(),
                prioridad = s.prioridad,
                potencialEstimado = s.potencialEstimado,
                estado = s.estado,
                fechaUltimaActualizacion = currentDate,
                organizacionId = s.organizacionIdSeleccionada,
                observacion = s.observacion.trim()
            )

            if (s.isEditing) {
                repository.updateOportunidad(entity)
            } else {
                repository.crearOportunidad(entity)
            }
            _uiState.update { it.copy(isSavedSuccess = true) }
        }
    }
}