package com.example.gestionmovil.ui.screens.contacto_form

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gestionmovil.data.local.entities.ContactoEntity
import com.example.gestionmovil.utils.Validaciones
import com.example.gestionmovil.data.repository.OrganizacionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ContactoFormViewModel(
    private val repository: OrganizacionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ContactoFormUiState())
    val uiState: StateFlow<ContactoFormUiState> = _uiState.asStateFlow()

    fun onOrganizacionSelected(orgId: Long?) {
        _uiState.update { it.copy(organizacionIdSeleccionada = orgId) }
    }

    fun onNombreContactoChanged(value: String) {
        val error = Validaciones.validarNotEmpty(value, "Nombre del contacto")
        _uiState.update { it.copy(nombreContacto = value, nombreContactoError = error) }
        validarFormulario()
    }

    fun onCargoChanged(value: String) {
        val error = Validaciones.validarNotEmpty(value, "Cargo")
        _uiState.update { it.copy(cargo = value, cargoError = error) }
        validarFormulario()
    }

    fun onCorreoChanged(value: String) {
        val error = Validaciones.validarCorreo(value)
        _uiState.update { it.copy(correo = value, correoError = error) }
        validarFormulario()
    }

    fun onTelefonoChanged(value: String) {
        val error = Validaciones.validarTelefono(value)
        _uiState.update { it.copy(telefono = value, telefonoError = error) }
        validarFormulario()
    }

    fun onAreaSelected(value: String) { _uiState.update { it.copy(area = value) } }
    fun onMedioPreferenteSelected(value: String) { _uiState.update { it.copy(medioPreferente = value) } }
    fun onObservacionChanged(value: String) { _uiState.update { it.copy(observacion = value) } }

    private fun validarFormulario() {
        val s = _uiState.value
        val isValid = s.nombreContactoError == null && s.nombreContacto.isNotBlank() &&
                      s.cargoError == null && s.cargo.isNotBlank() &&
                      s.correoError == null && s.correo.isNotBlank() &&
                      s.telefonoError == null && s.telefono.isNotBlank()

        _uiState.update { it.copy(isFormValid = isValid) }
    }

    fun saveContacto(currentDate: String) {
        if (!_uiState.value.isFormValid) return

        viewModelScope.launch {
            val s = _uiState.value
            val contact = ContactoEntity(
                organizacionId = s.organizacionIdSeleccionada ?: 1L,
                nombre = s.nombreContacto,
                cargo = s.cargo,
                area = s.area,
                correo = s.correo,
                telefono = s.telefono,
                medioPreferente = s.medioPreferente,
                estado = s.estado,
                fechaUltimoContacto = currentDate,
                observacion = s.observacion
            )
            repository.insertContacto(contact)
            _uiState.update { it.copy(isSavedSuccess = true) }
        }
    }
}