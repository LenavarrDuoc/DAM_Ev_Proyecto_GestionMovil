package com.example.gestionmovil.ui.seguimiento

import com.example.gestionmovil.data.local.entities.SeguimientoEntity

data class SeguimientoUiState(
    val listaSeguimientos: List<SeguimientoEntity> = emptyList(),
    val idSeleccionado: Long? = null,
    val oportunidadId: Long = 0L,
    val contactoId: Long? = null,
    val tipoGestion: String = "Llamada", // Valor por defecto
    val propuesta: String = "",
    val respuestaCliente: String = "",
    val proximaAccion: String = "",
    val fechaGestion: String = "",
    val fechaProximaAccion: String = "",
    val observacion: String = "",
    val isLoading: Boolean = false,
    val mensajeError: String? = null,
    val guardadoExitoso: Boolean = false
)