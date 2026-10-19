package com.example.gestionmovil.ui.screens.evento_form

import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gestionmovil.data.local.entities.EventoEntity
import com.example.gestionmovil.data.repository.EventoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EventoFormViewModel(
    private val repository: EventoRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(EventoFormUiState())
    val uiState: StateFlow<EventoFormUiState> = _uiState.asStateFlow()

    // Flujo con la lista de eventos guardados en Room
    val eventosList: StateFlow<List<EventoEntity>> = repository.todosLosEventos
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // ─── HANDLERS DE CAMBIOS DEL FORMULARIO ─────────────────────

    fun onTituloChange(nuevoTitulo: String) {
        _uiState.update { it.copy(titulo = nuevoTitulo, errorMessage = null) }
        validarFormulario()
    }

    fun onTipoEventoChange(nuevoTipo: String) {
        _uiState.update { it.copy(tipoEvento = nuevoTipo) }
    }

    fun onFechaChange(nuevaFecha: String) {
        _uiState.update { it.copy(fecha = nuevaFecha) }
    }

    fun onHoraChange(nuevaHora: String) {
        _uiState.update { it.copy(hora = nuevaHora) }
    }

    fun onRegionChange(nuevaRegion: String) {
        _uiState.update { it.copy(region = nuevaRegion) }
    }

    fun onComunaChange(nuevaComuna: String) {
        _uiState.update { it.copy(comuna = nuevaComuna) }
    }

    fun onOrganizacionChange(nuevaOrg: String) {
        _uiState.update { it.copy(organizacionNombre = nuevaOrg, errorMessage = null) }
        validarFormulario()
    }

    fun onPublicoEsperadoChange(nuevoPublico: String) {
        if (nuevoPublico.isEmpty() || nuevoPublico.all { it.isDigit() }) {
            _uiState.update { it.copy(publicoEsperado = nuevoPublico) }
        }
    }

    fun onCondicionesChange(nuevasCondiciones: String) {
        _uiState.update { it.copy(condicionesParticipacion = nuevasCondiciones) }
    }

    fun onFotoCapturada(bitmap: Bitmap) {
        _uiState.update { it.copy(fotoEvidencia = bitmap, errorMessage = null) }
        validarFormulario()
    }

    // ─── VALIDACIONES Y GUARDADO ─────────────────────────────────

    private fun validarFormulario() {
        val estado = _uiState.value
        val esValido = estado.titulo.isNotBlank() &&
                estado.organizacionNombre.isNotBlank() &&
                estado.fotoEvidencia != null

        _uiState.update { it.copy(isFormValid = esValido) }
    }

    fun guardarEvento(context: Context, onSuccess: (EventoEntity) -> Unit) {
        val estado = _uiState.value

        if (!estado.isFormValid) {
            _uiState.update { it.copy(errorMessage = "Completa los campos obligatorios y toma la foto") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            try {
                // Guardamos la foto en disco y obtenemos la ruta real del archivo
                val nombreArchivo = "foto_evento_${System.currentTimeMillis()}.jpg"
                val rutaAbsoluta = estado.fotoEvidencia?.let { bitmap ->
                    com.example.gestionmovil.utils.ImageUtils.guardarBitmapEnAlmacenamiento(context, bitmap, nombreArchivo)
                }

                val nuevoEvento = EventoEntity(
                    titulo = estado.titulo,
                    tipoEvento = estado.tipoEvento,
                    fecha = estado.fecha,
                    hora = estado.hora,
                    region = estado.region,
                    comuna = estado.comuna,
                    organizacionId = "ORG-${System.currentTimeMillis()}",
                    organizacionNombre = estado.organizacionNombre,
                    publicoEsperado = estado.publicoEsperado.toIntOrNull() ?: 0,
                    condicionesParticipacion = estado.condicionesParticipacion,
                    rutaFotoEvidencia = rutaAbsoluta // <-- Guardamos la ruta persistente
                )

                repository.guardarEvento(nuevoEvento)
                _uiState.update { it.copy(isLoading = false, isEventoGuardado = true) }
                onSuccess(nuevoEvento)
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message) }
            }
        }
    }
}