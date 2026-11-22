package com.example.gestionmovil.data.remote.dto

// Equivale a SeguimientoResponseDTO
data class SeguimientoResponseDto(
    val id: Long,
    val oportunidadId: Long?,
    val contactoId: Long?,
    val usuarioEmail: String?,
    val tipoGestion: String?,
    val propuesta: String?,
    val respuestaCliente: String?,
    val proximaAccion: String?,
    val fechaGestion: String?,
    val fechaProximaAccion: String?,
    val observacion: String?
)
