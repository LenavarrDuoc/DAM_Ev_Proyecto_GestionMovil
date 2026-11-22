package com.example.gestionmovil.data.remote.dto

// Equivale a EventoInputDTO
data class EventoRequestDto(
    val titulo: String,
    val tipoEvento: String,
    val fecha: String,
    val hora: String,
    val region: String,
    val comuna: String,
    val organizacionId: Long?,
    val organizacionNombre: String,
    val publicoEsperado: Int?,
    val condicionesParticipacion: String,
    val rutaFotoEvidencia: String
)