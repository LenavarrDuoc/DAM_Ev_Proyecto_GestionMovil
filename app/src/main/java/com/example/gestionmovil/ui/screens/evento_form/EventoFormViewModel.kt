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

    // Flujo con la lista de eventos guardados en Room (Single Source of Truth)
    val eventosList: StateFlow<List<EventoEntity>> = repository.todosLosEventos
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    init {
        // Al inicializar el ViewModel, sincronizamos los eventos desde Spring Boot hacia Room
        sincronizarEventos()
    }

    fun sincronizarEventos() {
        viewModelScope.launch {
            repository.sincronizarEventos()
        }
    }

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

    fun cargarEventoParaEditar(eventoId: String) {
        viewModelScope.launch {
            val evento = repository.obtenerEventoPorId(eventoId)
            if (evento != null) {
                _uiState.update {
                    it.copy(
                        id = evento.id,
                        titulo = evento.titulo,
                        tipoEvento = evento.tipoEvento,
                        fecha = evento.fecha,
                        hora = evento.hora,
                        region = evento.region,
                        comuna = evento.comuna,
                        organizacionNombre = evento.organizacionNombre,
                        publicoEsperado = if (evento.publicoEsperado > 0) evento.publicoEsperado.toString() else "",
                        condicionesParticipacion = evento.condicionesParticipacion,
                        isEditing = true,
                        errorMessage = null
                    )
                }
                validarFormulario()
            }
        }
    }

    fun limpiarFormulario() {
        _uiState.value = EventoFormUiState()
    }

    // ─── VALIDACIONES Y GUARDADO ─────────────────────────────────

    private fun validarFormulario() {
        val estado = _uiState.value
        val esValido = estado.titulo.isNotBlank() &&
                estado.organizacionNombre.isNotBlank() &&
                (estado.fotoEvidencia != null || estado.isEditing)

        _uiState.update { it.copy(isFormValid = esValido) }
    }

    fun guardarEvento(context: Context, onSuccess: (EventoEntity) -> Unit) {
        val estado = _uiState.value

        if (!estado.isFormValid) {
            _uiState.update { it.copy(errorMessage = "Completa los campos obligatorios y toma la foto") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            try {
                // Guardamos la foto en disco si se capturó una nueva
                val nombreArchivo = "foto_evento_${System.currentTimeMillis()}.jpg"
                val nuevaRuta = estado.fotoEvidencia?.let { bitmap ->
                    com.example.gestionmovil.utils.ImageUtils.guardarBitmapEnAlmacenamiento(
                        context,
                        bitmap,
                        nombreArchivo
                    )
                }

                val eventoExistente = if (estado.isEditing && estado.id != null) {
                    repository.obtenerEventoPorId(estado.id)
                } else null

                val eventoParaGuardar = EventoEntity(
                    id = estado.id ?: java.util.UUID.randomUUID().toString(),
                    titulo = estado.titulo.trim(),
                    tipoEvento = estado.tipoEvento,
                    fecha = estado.fecha.trim(),
                    hora = estado.hora.trim(),
                    region = estado.region.trim(),
                    comuna = estado.comuna.trim(),
                    organizacionId = eventoExistente?.organizacionId ?: 0L,
                    organizacionNombre = estado.organizacionNombre.trim(),
                    publicoEsperado = estado.publicoEsperado.toIntOrNull() ?: 0,
                    condicionesParticipacion = estado.condicionesParticipacion.trim(),
                    rutaFotoEvidencia = nuevaRuta ?: eventoExistente?.rutaFotoEvidencia
                )

                val resultado = if (estado.isEditing) {
                    repository.actualizarEvento(eventoParaGuardar)
                } else {
                    repository.guardarEvento(eventoParaGuardar)
                }

                resultado.onSuccess { eventoGuardado ->
                    _uiState.value = EventoFormUiState(isEventoGuardado = true)
                    onSuccess(eventoGuardado)
                }.onFailure { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = error.message ?: "Error al guardar el evento"
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message) }
            }
        }
    }

    fun eliminarEvento(eventoId: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val res = repository.eliminarEventoRemoto(eventoId)
            res.onSuccess {
                _uiState.update { it.copy(isLoading = false) }
                onSuccess()
            }.onFailure { err ->
                _uiState.update {
                    it.copy(isLoading = false, errorMessage = err.message ?: "Error al eliminar el evento")
                }
            }
        }
    }
}