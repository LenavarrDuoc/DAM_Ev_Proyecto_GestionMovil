package com.example.gestionmovil.data.remote.dto

// Equivale a SeguimientoInputDTO
data class SeguimientoRequestDto(
    val oportunidadId: Long, // FK requerida
    val contactoId: Long?,   // FK opcional
    val usuarioEmail: String?,
    val tipoGestion: String?,
    val propuesta: String?,
    val respuestaCliente: String?,
    val proximaAccion: String?,
    val fechaGestion: String?,
    val fechaProximaAccion: String?,
    val observacion: String?
)